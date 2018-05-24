package tanvn.learning.rabbitmq.chapter3;

public class PubSubApp {
	public static void main(String[] args) {
		// new Thread(new ReceiveLogs("Logger-1")).start();
		// new Thread(new ReceiveLogs("Logger-2")).start();

		// if there is no ReceiveLogs , there will be no queues bound to the exchange,
		// then the EmitLog will publish to the exchange but because there are no queues
		// to route, then the message will be discarded. One ReceiveLogs will bind one
		// queue to the exchange,
		// so each ReceiveLogs will have its own queue -> receive all messages
		// published.
		new Thread(new EmitLog()).start();

	}

}
