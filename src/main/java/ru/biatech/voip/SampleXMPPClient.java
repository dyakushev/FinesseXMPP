package ru.biatech.voip;
import java.security.Timestamp;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;


public class SampleXMPPClient {

	 private  XMPPConnection connection = null;
	 private volatile PubSubManager subscriptionManager = null;
	 private String username = "agent1";
	 private String passwd = "123654";
	 private String resource = "agent1";
	 private String nodeId = "/finesse/api/User/" + username;
	 private String hostname = "u-succx-1.dellin.local";
	
	
	 public void login() throws XMPPException {

		 ConnectionConfiguration config = new ConnectionConfiguration(hostname, 5222);
		 config.setSASLAuthenticationEnabled(false);
		 config.setReconnectionAllowed(false);
		 connection = new XMPPConnection(config);
		 subscriptionManager = new PubSubManager(connection, "pubsub."+hostname);

		 try {
			 connection.connect();
			 connection.login(username, passwd);
			 //connection.login(username, passwd, resource);
			 System.out.println("Successfully Connected");
			 subscribeImpl();
			 System.out.println("Successfully Obtained the node");
		 } catch (XMPPException e) {
			System.out.println("Error during xmpp connection"+e.getMessage());
			 throw e;
		 }
	 }
	
	 private void subscribeImpl() throws XMPPException
	 {
		 String logPrefix = "XMPPClient.subscribe on nodeId = " + nodeId + " for user = " + username;
		 Node node = subscriptionManager.getNode(nodeId);
		
		 // ItemEventListener required by Smack library
		 ItemEventListener<Item> listener = new ItemEventListener<Item>() {

			 public void handlePublishedItems(ItemPublishEvent<Item> itemsPublished) {

				 List<Item> items = itemsPublished.getItems();
				 for(Item item:items)
				 {
					 System.out.println(item.toString());
				 }
				 
			 }
		 };

		 // Add the event listener for this node
		 node.addItemEventListener(listener);
		 
	 }
	 
	 public void logout(){
		 
		 connection.disconnect();
		 System.out.println("Successfully disconnected");
	 }
	 
	 public static void main(String[] args) throws XMPPException {
			
		 SampleXMPPClient client = new  SampleXMPPClient();
		 client.login();
//	        subscriptionManager = new PubSubManager(connection, pubsubDomain);
		 
		 // This infinite loop is to make the client wait for events
		 System.out.println("Waiting for event");
		 while(true){
			 //waiting for new events
		 }
		 
	}
}
