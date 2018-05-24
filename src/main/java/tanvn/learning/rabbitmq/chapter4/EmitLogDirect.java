package tanvn.learning.rabbitmq.chapter4;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import tanvn.learning.util.RandomString;

public class EmitLogDirect implements Runnable {

	private static final String EXCHANGE_NAME = "direct_logs";
	public static final String[] SEVERITY_LEVELS = { "info", "debug", "error" };

	private String getRandomSeverity() {
		Random rand = new Random();
		int index = rand.nextInt(SEVERITY_LEVELS.length);
		return SEVERITY_LEVELS[index];
	}

	@Override
	public void run() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
			RandomString randStr = new RandomString();
			for (int i = 0; i < 10; i++) {
				String severity = this.getRandomSeverity();
				String message = randStr.nextString();
				// publish to exchange with a routing key
				channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
				System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
				Thread.sleep(2000);

			}

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}