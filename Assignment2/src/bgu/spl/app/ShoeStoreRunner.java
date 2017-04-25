package bgu.spl.app;
import java.util.logging.*;

import org.omg.Messaging.SyncScopeHelper;

import java.util.*;
import com.google.gson.*;

import bgu.spl.mics.impl.MessageBusImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;  
import java.io.*;
import java.util.concurrent.*;

/**
 * The Class ShoeStoreRunner is the class with the main function.
 * The Class reads from a file and than initializes the store and all the MicroServices.
 * when all {@link MicroService}s terminated invokes the {@link Store}'s {@code print} method
 * At the end, the Class terminates the program.
 * 
 */
public class ShoeStoreRunner {

	/**
	 * The main method.
	 *
	 * @param args 		the file with all the details
	 */
	public static void main(String[] args) {
		Store store=Store.getInstance();
		Logger logger=Logger.getLogger("ShoeStoreLogger");
		 int counter=0;
		 try{
			 JsonParser parser = new JsonParser();
			 FileReader f=new FileReader("src/bgu/spl/app/sample.json");
			 JsonObject parsed =(JsonObject)(parser.parse(f));
			 
			 //initializing store
			 ArrayList<ShoeStorageInfo> shoes=new ArrayList<ShoeStorageInfo>();
			 JsonObject shoeStorageInfo;
			 JsonArray initialStorage=parsed.getAsJsonArray("initialStorage");
			 for(int i=0;i<initialStorage.size();i++) {
				 shoeStorageInfo=(JsonObject)(initialStorage.get(i));
				 shoes.add(new ShoeStorageInfo(shoeStorageInfo.get("shoeType").getAsString(),shoeStorageInfo.get("amount").getAsInt()));
			 }
			 
			 store.load(shoes.toArray(new ShoeStorageInfo[shoes.size()] ));
			 logger.info("Store initialized");
			 
			 //initializing MicroServices
			 JsonObject services=parsed.getAsJsonObject("services");
			 
			 //initializing timer
			 JsonObject time=services.getAsJsonObject("time");
			 TimeService timer=new TimeService(time.get("speed").getAsInt(),time.get("duration").getAsInt());
			 counter++;
			 logger.info("Timer created");
			
			 //initializing manager
			 JsonObject manager=services.getAsJsonObject("manager");
			 JsonArray discountSchedule=manager.getAsJsonArray("discountSchedule");
			 ArrayList<DiscountSchedule> discountArray= new ArrayList<DiscountSchedule>();
			 JsonObject discount;
			 for(int i=0;i<discountSchedule.size();i++) {
				 discount=(JsonObject)(discountSchedule.get(i));
				 discountArray.add(new DiscountSchedule(discount.get("shoeType").getAsString(),discount.get("tick").getAsInt(),discount.get("amount").getAsInt()));	 
			 }
			 
			 ManagmentService managment=new ManagmentService(discountArray);
			 counter++;
			 logger.info("Manager created");
			 
			 //remembering number of factories
			 
			 int numOfFactories=services.get("factories").getAsInt();
			 counter+=numOfFactories;
			 
			 //remembering number of selling services
			 
			 int numOfSellingServices=services.get("sellers").getAsInt();
			 counter+=numOfSellingServices;
			 
			 //initializing customers
			 JsonArray customers=services.getAsJsonArray("customers");
			 ArrayList<WebsiteClientService> customersArray= new ArrayList<WebsiteClientService>();
			 String name;
			 LinkedList<String> wishList;
			 ArrayList<PurchaseSchedule> purchaseSchedule;
			 JsonObject customer;
			 JsonArray wishArray;
			 JsonArray purchArray;
			 JsonObject purchase;
			 for(int i=0 ; i<customers.size() ; i++) {
				 wishList=new LinkedList<String>();
				 purchaseSchedule=new ArrayList<PurchaseSchedule>();
				 customer=(JsonObject)(customers.get(i));
				 name=customer.get("name").getAsString();
				 wishArray=customer.getAsJsonArray("wishList");
				 for(int j=0 ; j<wishArray.size() ; j++) {
					 wishList.add(wishArray.get(j).getAsString());
				 }
				 purchArray=customer.getAsJsonArray("purchaseSchedule");
				 for(int j=0 ; j<purchArray.size() ; j++) {
					 purchase=(JsonObject)(purchArray.get(j));
					 purchaseSchedule.add(new PurchaseSchedule(purchase.get("shoeType").getAsString(),purchase.get("tick").getAsInt()));
				 }
				 customersArray.add(new WebsiteClientService(name,purchaseSchedule,wishList));
			 }
			 counter+=customers.size();
			 logger.info("Customers created");
			 f.close();
			 
			 ExecutorService executor = Executors.newFixedThreadPool(counter+1);
			 executor.execute(new StartService(counter));
			 synchronized(MessageBusImpl.getInstance()) {				 
				 MessageBusImpl.getInstance().wait();
			 }
			 
			 executor.execute(timer);
			 executor.execute(managment);
			 for(int i=0 ; i< numOfFactories ; i++) {
				 executor.execute(new ShoeFactoryService(i+1+""));
			 }
			 for(int i=0 ; i< numOfSellingServices ; i++) {
				 executor.execute(new SellingService(i+1+""));
			 }
			 for(int i=0 ; i< customersArray.size() ; i++) {
				 executor.execute(customersArray.get(i));
			 }
			 
			 logger.info("Factories created");
			 logger.info("Sellers created");
			 logger.info("Threads created");
			 
			executor.shutdown();
			while(!executor.isTerminated()) { 
			}
			
			 Store.getInstance().print();
				 
		 }  catch(FileNotFoundException  e){System.out.println("File not found");}
		 	catch(IOException ioe) {System.out.println("FileReader was not closed");}
		 	catch(InterruptedException ex){}
	}
}
