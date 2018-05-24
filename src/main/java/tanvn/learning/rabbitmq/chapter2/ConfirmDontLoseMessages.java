//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//
//  The Initial Developer of the Original Code is VMware, Inc.
//  Copyright (c) 2007-2011 VMware, Inc.  All rights reserved.
//

package tanvn.learning.rabbitmq.chapter2;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

public class ConfirmDontLoseMessages {
	static int msgCount = 100;
	final static String QUEUE_NAME = "confirm-test";
	static ConnectionFactory connectionFactory;

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length > 0) {
			msgCount = Integer.parseInt(args[0]);
		}

		connectionFactory = new ConnectionFactory();

		// Consume msgCount messages.
		(new Thread(new MyConsumer())).start();
		// Publish msgCount messages and wait for confirms.
		(new Thread(new Publisher())).start();
	}

	static class Publisher implements Runnable {
		public void run() {
			try {
				long startTime = System.currentTimeMillis();

				// Setup
				Connection conn = connectionFactory.newConnection();
				Channel ch = conn.createChannel();
				ch.queueDeclare(QUEUE_NAME, true, false, false, null);
				ch.confirmSelect();

				// Publish
				for (long i = 0; i < msgCount; ++i) {
					ch.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, "nop".getBytes());
				}

				ch.waitForConfirmsOrDie();

				// Cleanup
				ch.queueDelete(QUEUE_NAME);
				ch.close();
				conn.close();

				long endTime = System.currentTimeMillis();
				System.out.printf("Test took %.3fs\n", (float) (endTime - startTime) / 1000);
			} catch (Throwable e) {
				System.out.println("foobar :(");
				System.out.print(e);
			}
		}
	}

	static class MyConsumer implements Runnable {

		private int count = 0;

		public int getCount() {
			return this.count;
		}

		public void increaseCount() {
			this.count++;
		}

		public void run() {
			try {
				// Setup
				Connection conn = connectionFactory.newConnection();
				Channel ch = conn.createChannel();
				ch.queueDeclare(QUEUE_NAME, true, false, false, null);

				// Consume
				final Consumer qc = new DefaultConsumer(ch) {

					@Override
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {
						MyConsumer.this.increaseCount();
						System.out.println(MyConsumer.this.getCount());

					}
				};
				ch.basicConsume(QUEUE_NAME, true, qc);
				// for (int i = 0; i < msgCount; ++i) {
				// // qc.n;
				// }

				// Cleanup
				// ch.close();
				// conn.close();
				System.out.println(this.getCount());
			} catch (Throwable e) {
				System.out.println("Whoosh!");
				System.out.print(e);
			}
		}
	}
}