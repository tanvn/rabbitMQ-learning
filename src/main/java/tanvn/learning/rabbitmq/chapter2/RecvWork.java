package tanvn.learning.rabbitmq.chapter2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RecvWork implements Runnable {
	private final static String QUEUE_NAME = "work_queue";
	private String name;

	public RecvWork(String name) {
		this.name = name;
	}

	public void start() throws IOException, InterruptedException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.basicQos(1);

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				System.out.println(" [" + RecvWork.this.name + "] Received '" + message + "'");
				try {
					doWork(message);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					System.out.println(" [" + RecvWork.this.name + "] Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		boolean autoAck = false; // acknowledgment is covered below
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);

		// channel.basicConsume(QUEUE_NAME, true, consumer);

	}

	private void doWork(String task) throws InterruptedException {
		int count = 0;
		for (char ch : task.toCharArray()) {
			if (ch == '.') {
				Thread.sleep(1000);
				count++;
			}
		}
		System.out.println(" [" + RecvWork.this.name + "] Slept for " + count + " secs");
	}

	public void run() {
		try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}