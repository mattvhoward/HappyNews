package hackathon.winfo.HappyNews;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import hackathon.winfo.*;

public class App {
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "ACabf08afd0a07faaea90c126c498c7f1b";
  public static final String AUTH_TOKEN = "b35a84f350cf00da601fa6c92a148ff8";
  public static HappyDataStore hds;

  public static void main(String[] args) throws InterruptedException, IOException {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    hds = new HappyDataStore("poop");
    Set<String> notHappy = new HashSet<String>(hds.getNumbers());
    System.out.println("Getting old messages...");
    ResourceSet<Message> messages = getMessages();
    try {
		deleteMessages(messages);
		System.out.println("Old messages deleted");
	} catch(Exception e) {
		e.printStackTrace();
	}
    Thread.sleep(2000);
    while(true) {
    		System.out.println("Getting new messages...");
    		messages = getMessages();
    		
    		for (Message msg : messages) {
    			if (msg.getTo().toString().equals("+12532631761")) {
    				System.out.println("New message from: " + msg.getFrom().toString());
	    			PhoneNumber pn = msg.getFrom();
	    			String phoneNumber = pn.toString();
	    			if (!hds.getNumbers().contains(phoneNumber)) {
	    				hds.addNumber(phoneNumber);
	    				System.out.println("New user message from " + phoneNumber + ", sending welcome..");
	    				Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber("+12532631761"),
	    						"Welcome to Happy News! Curated happy news delivered to you daily.").create();
	    			}
	    			Message.deleter(msg.getSid()).delete();
	    			notHappy.add(phoneNumber);
	    			System.out.println("New unhappy user " + phoneNumber);
    			}
    		}
    		
    		System.out.println("No more new messages");
    		for (String num : notHappy) {
    			int randomNum = (int) (Math.random() * 100);
    			Message.creator(new PhoneNumber(num), new PhoneNumber("+12532631761"), hds.getNews()[randomNum]).create();
    			System.out.println("Message sent to unhappy user: " + num);
    		}
    		notHappy.clear();
    		hds.save();
    		/*try {
    			//deleteMessages(messages);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}*/
    		Thread.sleep(5000);
    }
  }
  
  public static ResourceSet<Message> getMessages() {
	  return Message.reader().read();
  }
  
  public static void deleteMessages(ResourceSet<Message> messages) {
	  messages = getMessages();
	  for (Message msg : messages) {
		  Message.deleter(msg.getSid()).delete();
	  }
  }
}
