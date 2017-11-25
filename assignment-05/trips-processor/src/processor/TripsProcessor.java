package processor;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TripsProcessor implements MessageListener {
	// connection factory
    private QueueConnectionFactory qconFactory;
 
    // connection to a queue
    private QueueConnection qcon;
 
    // session within a connection
    private QueueSession qsession;
 
    // queue receiver that receives a message to the queue
    private QueueReceiver qreceiver;
 
    // queue where the message will be sent to
    private Queue queue;
 
    // callback when the message exist in the queue
    public void onMessage(Message msg) {
        try {
            String msgText;
            if (msg instanceof TextMessage) {
            	String text = ((TextMessage) msg).getText();
            	
                if(text.equals("create new trip")) {
                	msgText = "TripsProcessor: new trip created";
                } else {
                	msgText = "TripsProcessor: unexpected message content:" + text;
                }
            } else {
                msgText = "TripsProcessor: unexpected message content:" + msg.toString();
            }
            System.out.println(msgText);
        } catch (JMSException jmse) {
            System.err.println("An exception occurred: " + jmse.getMessage());
        }
    }
 
    // create a connection to the WLS using a JNDI context
    public void init(Context ctx, String queueName)
            throws NamingException, JMSException {
 
        qconFactory = (QueueConnectionFactory) ctx.lookup("jms/mdw-6-cf");
        qcon = qconFactory.createQueueConnection();
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = (Queue) ctx.lookup(queueName);
 
        qreceiver = qsession.createReceiver(queue);
        qreceiver.setMessageListener(this);
 
        qcon.start();
    }
 
    // close sender, connection and the session
    public void close() throws JMSException {
        qreceiver.close();
        qsession.close();
        qcon.close();
    }
 
    // start receiving messages from the queue
    public void receive(String queueName) throws Exception {
    	Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, "t3://localhost:7001");
 
        InitialContext ic = new InitialContext(env);
 
        init(ic, queueName);
 
        System.out.println("TripsProcessor: Receiving messages");
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
 
    public static void main(String[] args) throws Exception {
        // input arguments
        String queueName = "jms/mdw-6-trip-queue";
 
        // create the producer object and receive the message
        TripsProcessor processor = new TripsProcessor();
        processor.receive(queueName);
    }
}
