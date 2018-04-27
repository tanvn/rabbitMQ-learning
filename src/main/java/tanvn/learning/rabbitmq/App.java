package tanvn.learning.rabbitmq;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        (new Thread(new Recv())).run();
        (new Thread(new Send())).run();
    }
}
