package bgu.spl.mics.impl;
import bgu.spl.app.TerminationBroadcast;
import bgu.spl.app.TickBroadcast;
import bgu.spl.mics.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;


/**
 * The Class MessageBusImpl implements MessageBus.
 */
public class MessageBusImpl implements MessageBus {
	
	private HashMap<String,MessageRegistrationManager> messageTypeList;
	private HashMap<MicroService, LinkedList<Message>> microServiceList;
	private HashMap<Request<?>, MicroService> uncompletedRequests;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");
	
	    /**
    	 * The Class MessageBusImplHolder is a tool to make MessageBusImpl a singelton.
    	 */
    	private static class MessageBusImplHolder {
	        
        	/** The instance. */
        	private static MessageBusImpl instance = new MessageBusImpl();
	    }
	    
    	/**
    	 * Instantiates a new message bus impl.
    	 */
    	private MessageBusImpl() {
	    	messageTypeList=new HashMap<String,MessageRegistrationManager>();
	    	microServiceList=new HashMap<MicroService, LinkedList<Message>>();
	    	uncompletedRequests=new HashMap<Request<?> , MicroService>();
	    }
	    
    	/**
    	 * Gets the single instance of MessageBusImpl.
    	 *
    	 * @return single instance of MessageBusImpl
    	 */
    	public static MessageBusImpl getInstance() {
	        return MessageBusImplHolder.instance;
	    }
	
	 /**
     * subscribes {@code m} to receive {@link Request}s of type {@code type}.
     * <p>
     * @param type the type to subscribe to
     * @param m    the subscribing micro-service
     */
   public void subscribeRequest(Class<? extends Request> type, MicroService m) {
	   subscribeMessage(type, m);
    }

    /**
     * subscribes {@code m} to receive {@link Broadcast}s of type {@code type}.
     * <p>
     * @param type the type to subscribe to
     * @param m    the subscribing micro-service
     */
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
    	subscribeMessage(type, m);
    }

    /**
     * subscribes {@code m} to receive {@link Message}s of type {@code type}.
     * <p>
     * @param type the type to subscribe to
     * @param m    the subscribing micro-service
     */
    private void subscribeMessage(Class<? extends Message> type, MicroService m) {
 	    register(m);
    	synchronized(messageTypeList) {
        	if(!(messageTypeList.containsKey(type.getName()))) {
        		messageTypeList.put(type.getName(), new  MessageRegistrationManager());
        	}
        	messageTypeList.get(type.getName()).add(m);	
    	}
    }
    
    /**
     * Notifying the MessageBus that the request {@code r} is completed and its
     * result was {@code result}.
     * When this method is called, the message-bus will implicitly add the
     * special {@link RequestCompleted} message to the queue
     * of the requesting micro-service, the RequestCompleted message will also
     * contain the result of the request ({@code result}).
     * <p>
     * @param <T>    the type of the result expected by the completed request
     * @param r      the completed request
     * @param result the result of the completed request
     */
   public <T> void complete(Request<T> r, T result){
	   MicroService m=uncompletedRequests.get(r);
	   LinkedList qOfm= microServiceList.get(m);
	   if(qOfm!=null) {		  
		   synchronized(qOfm) {
			   qOfm.add(new RequestCompleted(r,result));
			   qOfm.notifyAll();   
		   }
	   }
	   
    }
    
    /**
     * add the {@link Broadcast} {@code b} to the message queues of all the
     * micro-services subscribed to {@code b.getClass()}.
     * <p>
     * @param b the message to add to the queues.
     */
    public void sendBroadcast(Broadcast b) {
    	MessageRegistrationManager messageRegistrationManager=messageTypeList.get(b.getClass().getName());
    	if(messageRegistrationManager!=null) {				//once a MicroService is added it will not be removed
    		synchronized(messageRegistrationManager) {
    			MicroService[] microServices=messageRegistrationManager.getMicroServices();
    			for(MicroService m : microServices) {
    				LinkedList<Message> qOfm=microServiceList.get(m);
    				if(b.getClass()==TerminationBroadcast.class) {
    					qOfm.addFirst(b);
    				} else {    					
    					qOfm.add(b);
    				}
    					synchronized(qOfm) {
        				qOfm.notifyAll();	
        			}    			
    			}
    		}
    	}
    }

    /**
     * add the {@link Request} {@code r} to the message queue of one of the
     * micro-services subscribed to {@code r.getClass()} in a round-robin
     * fashion.
     * <p>
     * @param r         the request to add to the queue.
     * @param requester the {@link MicroService} sending {@code r}.
     * @return true if there was at least one micro-service subscribed to
     *         {@code r.getClass()} and false otherwise.
     */
    public boolean sendRequest(Request<?> r, MicroService requester) {
    	
    	if(!(messageTypeList.containsKey(r.getClass().getName()))) {
    		return false;
    	}else {
    		MessageRegistrationManager messageRegistrationManager=messageTypeList.get(r.getClass().getName());
    		synchronized(messageRegistrationManager){
    			if(messageRegistrationManager.getNumberOfMicroServices()<1) {
        			return false;
        		} else {
            		uncompletedRequests.put(r, requester);		//need to check that no thread completes the request before we add it to uncompleted
            		MicroService m=messageRegistrationManager.next();
            		LinkedList<Message> qOfm=microServiceList.get(m);
        			qOfm.add(r);
        			synchronized(qOfm) {
        				qOfm.notifyAll();	
        			}
            		return true;	
        		}
    		}
    	}
    }

    /**
     * allocates a message-queue for the {@link MicroService} {@code m}.
     * <p>
     * @param m the micro-service to create a queue for.
     */
   public void register(MicroService m) {
    	synchronized(microServiceList) {
 		   if(!(microServiceList.containsKey(m))) {
 	    		microServiceList.put(m, new LinkedList<Message>());
 	    		logger.info("Registering "+ m.getName());
 	    	}
    	}
    }

    /**
     * remove the message queue allocated to {@code m} via the call to
     * {@link #register(bgu.spl.mics.MicroService)} and clean all references
     * related to {@code m} in this message-bus. If {@code m} was not
     * registered, nothing should happen.
     * <p>
     * @param m the micro-service to unregister.
     */
    public void unregister(MicroService m) {
    	Collection<MessageRegistrationManager> values=messageTypeList.values();
    	for(MessageRegistrationManager v : values) {
    		v.remove(m);
    	}
    	if((microServiceList.containsKey(m))) {
  	    	microServiceList.remove(m);
  	    	logger.info(m.getName()+" unregistered successfully");
  	    }
    }

    /**
     * using this method, a <b>registered</b> micro-service can take message
     * from its allocated queue.
     * This method is blocking -meaning that if no messages
     * are available in the micro-service queue it
     * should wait until a message became available.
     * The method should throw the {@link IllegalStateException} in the case
     * where {@code m} was never registered.
     * <p>
     * @param m the micro-service requesting to take a message from its message
     *          queue
     * @return the next message in the {@code m}'s queue (blocking)
     * @throws InterruptedException if interrupted while waiting for a message
     *                              to became available.
     */
    public Message awaitMessage(MicroService m) throws InterruptedException {
    	logger.info(m.getName()+" waiting for message");
    	LinkedList<Message> qOfm=microServiceList.get(m);
    	if(qOfm==null) {
    		throw new IllegalStateException();
    	} else {
    		synchronized(qOfm) {
	    		while(qOfm.isEmpty()) {
	        				qOfm.wait();	
	    		}
	    		logger.info(m.getName()+" recieved "+qOfm.getFirst().getClass().getName());
	    		return qOfm.removeFirst();
    		}    			
    	}
    }

}
