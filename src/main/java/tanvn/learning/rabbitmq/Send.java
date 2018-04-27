package tanvn.learning.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class Send  implements Runnable {
	 private final static String QUEUE_NAME = "hello";

	  public void start()
	      throws java.io.IOException, TimeoutException {
		  ConnectionFactory factory = new ConnectionFactory();
		  factory.setHost("localhost");
		  Connection connection = factory.newConnection();
		  Channel channel = connection.createChannel();
		  channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		  String message = "Hello World!";
		  channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		  System.out.println(" [x] Sent '" + message + "'");
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
