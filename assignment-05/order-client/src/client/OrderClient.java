package client;

import java.util.Hashtable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import weblogic.nodemanager.common.Config;

public class OrderClient {
	// connection factory
    private QueueConnectionFactory qconFactory;

    // connection to a queue
    private QueueConnection qcon;

    // session within a connection
    private QueueSession qsession;

    // queue sender that sends a message to the queue
    private QueueSender qsender;

    // queue where the message will be sent to
    private Queue queue;

    // a message that will be sent to the queue
    private TextMessage msg;
    
    // create a connection to the WLS using a JNDI context
    public void init(Context ctx, String queueName)
        throws NamingException, JMSException {

        // create connection factory based on JNDI and a connection
        qconFactory = (QueueConnectionFactory) ctx.lookup("jms/mdw-6-cf");
        qcon = qconFactory.createQueueConnection();

        // create a session within a connection
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // lookups the queue using the JNDI context
        queue = (Queue) ctx.lookup(queueName);

        // create sender and message
        qsender = qsession.createSender(queue);
        msg = qsession.createTextMessage();
    }

    // close sender, connection and the session
    public void close() throws JMSException {
        qsender.close();
        qsession.close();
        qcon.close();
    }

    // sends the message to the queue
    public void send(String queueName, String message) throws Exception {

        // create a JNDI context to lookup JNDI objects (connection factory and queue)
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, "t3://localhost:7001");

        InitialContext ic = new InitialContext(env);
        init(ic, queueName);

        // send the message and close
        try {
            msg.setText(message);
            qsender.send(msg, DeliveryMode.PERSISTENT, 8, 0);
            System.out.println("OrderClient sent: " + message);
        } finally {
            close();
        }
    }

    public static void main(String[] args) throws Exception {
        String queueName = "jms/mdw-6-order-queue" ;

        // create the producer object and send the message
        OrderClient client = new OrderClient();
        client.send(queueName, "order");
        client.send(queueName, "new trip");
    }
}
