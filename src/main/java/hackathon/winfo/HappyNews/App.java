package hackathon.winfo.HappyNews;

import java.io.FileNotFoundException;
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

  public static void main(String[] args) throws InterruptedException, FileNotFoundException {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    hds = new HappyDataStore("/");
    Set<String> notHappy = new HashSet<String>(hds.getNumbers());
    while(true) {
    		ResourceSet<Message> messages = getMessages();
    		
    		for (Message msg : messages) {
    			if (msg.getTo().toString().equals("+12532631761")) {
	    			PhoneNumber pn = msg.getFrom();
	    			String phoneNumber = pn.toString();
	    			hds.addNumber(phoneNumber);
	    			notHappy.add(phoneNumber);
    			}
    		}
    		try {
    			deleteMessages(messages);
    		} catch(Exception e) {
    			
    		}
    		for (String num : notHappy) {
    			int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
    			System.out.println(randomNum);
    			System.out.println(hds.getNews()[randomNum]);
    			Message message = Message.creator(new PhoneNumber(num), new PhoneNumber("+12532631761"), hds.getNews()[randomNum]).create();
    		}
    		notHappy.clear();
    		Thread.sleep(10000);
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
