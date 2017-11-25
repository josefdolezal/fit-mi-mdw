package processor;

import java.util.Hashtable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class OrderProcessor implements MessageListener {
	// connection factory
    private QueueConnectionFactory qconFactory;

    // connection to a queue
    private QueueConnection qcon;

    // session within a connection
    private QueueSession qsession;

    // queue sender that sends a message to the queue
    private QueueSender bookingSender;
    
    private QueueSender newTripSender;
    
    private QueueReceiver orderReciever;

    // queue where the message will be sent to
    private Queue incommingQueue;
    
    private Queue outcommingBookingQueue;
    
    private Queue outcommingNewTripQueue;

    // a message that will be sent to the queue
    private TextMessage msg;

    // create a connection to the WLS using a JNDI context
    public void init(Context ctx, String incommingQueueName, String outcommingBookingQueueName, String outcommingNewTripQueueName)
        throws NamingException, JMSException {

        // create connection factory based on JNDI and a connection
        qconFactory = (QueueConnectionFactory) ctx.lookup("jms/mdw-6-cf");
        qcon = qconFactory.createQueueConnection();

        // create a session within a connection
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // lookups the queue using the JNDI context
        incommingQueue = (Queue) ctx.lookup(incommingQueueName);
        outcommingBookingQueue = (Queue) ctx.lookup(outcommingBookingQueueName);
        outcommingNewTripQueue = (Queue) ctx.lookup(outcommingNewTripQueueName);
        
        orderReciever = qsession.createReceiver(incommingQueue);
        orderReciever.setMessageListener(this);

        // create sender and message
        bookingSender = qsession.createSender(outcommingBookingQueue);
        newTripSender = qsession.createSender(outcommingNewTripQueue);
        
        msg = qsession.createTextMessage();
        qcon.start();
    }

    // close sender, connection and the session
    public void close() throws JMSException {
        bookingSender.close();
        newTripSender.close();
        orderReciever.close();
        qsession.close();
        qcon.close();
    }

    // sends the message to the queue
    public void send(QueueSender sender, String message) {
        try {
            msg.setText(message);
            sender.send(msg, DeliveryMode.PERSISTENT, 8, 0);
            System.out.println("OrderProcessor: sending message: " + message);
        } catch (Exception e) { }
    }
    
 // start receiving messages from the queue
    public void receive() throws Exception {
        System.out.println("OrderProcessor: receiving messages.");
        
        try {
            synchronized (this) {
                while (true) {
                    this.wait();
                }
            }
        } finally {
            close();
            System.out.println("Finished.");
        }
    }
    
    @Override
	public void onMessage(Message msg) {
    	if(msg instanceof TextMessage) {
    		try {
				String text = ((TextMessage) msg).getText();
				
				if(text.equals("order")) {
					send(bookingSender, "book trip");
				} else if(text.equals("new trip")) {
					send(newTripSender, "create new trip");
				} else {
					System.out.println("OrderProcessor: Unexpected message: " + text);
				}
			} catch (JMSException e) { }
    	} else {
    		System.out.println("OrderProcessor: unexpected message " + msg.toString());
    	}
	}

    public static void main(String[] args) throws Exception {
        String incommingQueue = "jms/mdw-6-order-queue";
        String outcommingBookingQueue = "jms/mdw-6-booking-queue";
        String outcommingNewTripQueue = "jms/mdw-6-trip-queue";

        // create a JNDI context to lookup JNDI objects (connection factory and queue)
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, "t3://localhost:7001");
 
        InitialContext ic = new InitialContext(env);

        // create the producer object and send the message
        OrderProcessor processor = new OrderProcessor();
    
        processor.init(ic, incommingQueue, outcommingBookingQueue, outcommingNewTripQueue);
        processor.receive();
    }
}
