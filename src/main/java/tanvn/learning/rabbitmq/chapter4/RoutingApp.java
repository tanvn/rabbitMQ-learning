package tanvn.learning.rabbitmq.chapter4;

public class RoutingApp {
	public static void main(String[] args) {
		new Thread(new ReceiveLogsDirect()).start();
		new Thread(new EmitLogDirect()).start();
	}

}
