package tanvn.learning.rabbitmq.chapter2;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import tanvn.learning.util.RandomString;

public class SendWork implements Runnable {

	private final static String QUEUE_NAME = "work_queue";

	public void start() throws java.io.IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.confirmSelect();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		final RandomString stringGenerator = new RandomString();
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			String message = stringGenerator.nextStringWithDot(rand.nextInt(10));
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		}
		try {
			channel.waitForConfirms();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" [x] Confirmed !");

		channel.close();
		connection.close();
	}

	public void run() {
		try {
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
