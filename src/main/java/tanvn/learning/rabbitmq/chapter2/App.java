package tanvn.learning.rabbitmq.chapter2;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		(new Thread(new RecvWork("Worker 1"))).run();
		// (new Thread(new RecvWork("Worker 2"))).run();
		// (new Thread(new SendWork())).run();
	}
}
