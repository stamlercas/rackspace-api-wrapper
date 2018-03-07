package com.rackspace.api_wrapper;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * API Wrapper for the Rackspace Mailbox Rest API
 * @author Chris
 *
 */
public class RackspaceApi {
	protected Client client;
	protected String customerAccountNumber, domainName, format;
	
	/**
	 * Class constructor
	 * @param apiKey
	 * @param secretKey
	 * @param format
	 */
	public RackspaceApi(String apiKey, String secretKey, String customerAccountNumber, String domainName) {
		client = new Client(apiKey, secretKey);
		this.customerAccountNumber = customerAccountNumber;
		this.domainName = domainName;
		this.format = "application/json";
	}
	
	/**
	 * Returns list of all mailboxes
	 * @return	JSON object of mailboxes
	 * @see <a href="http://api-wiki.apps.rackspace.com/api-wiki/index.php?title=Rackspace_Mailbox_(Rest_API)#Index">Rackspace Mailbox (Rest API) Index</a>
	 */
	public JSONObject list() {
		try {
			String response = Client.responseToString(client.get("/customers/" + customerAccountNumber + "/domains/" 
														+ domainName + "/rs/mailboxes", format));
			return new JSONObject(response);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns one mailbox
	 * @param mailboxName	ID of mailbox to display
	 * @return	JSON object of mailbox
	 * @see <a href="http://api-wiki.apps.rackspace.com/api-wiki/index.php?title=Rackspace_Mailbox_(Rest_API)#Show">Rackspace Mailbox (Rest API) Show</a>
	 */
	public JSONObject show(String mailboxName) {
		try {
			String response = Client.responseToString(client.get("/customers/" + customerAccountNumber + "/domains/" 
														+ domainName + "/rs/mailboxes/" + mailboxName, format));
			return new JSONObject(response);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Adds new mailbox
	 * @param mailboxName	ID of mailbox
	 * @param data			data to send
	 * @return	boolean
	 * @see <a href="http://api-wiki.apps.rackspace.com/api-wiki/index.php?title=Rackspace_Mailbox_(Rest_API)#Add.2FEdit">Rackspace Mailbox (Rest API) Add/Edit</a>
	 */
	public boolean add(String mailboxName, HashMap<String, String> data) {
		try {
			client.post("/customers/" + customerAccountNumber + "/domains/" + domainName + "/rs/mailboxes/" + mailboxName, data, format);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Edits specified mailbox
	 * @param mailboxName	ID of mailbox
	 * @param data			data to send
	 * @return	boolean
	 * @see <a href="http://api-wiki.apps.rackspace.com/api-wiki/index.php?title=Rackspace_Mailbox_(Rest_API)#Add.2FEdit">Rackspace Mailbox (Rest API) Add/Edit</a>
	 */
	public boolean edit(String mailboxName, HashMap<String, String> data) {
		try {
			client.put("/customers/" + customerAccountNumber + "/domains/" + domainName + "/rs/mailboxes/" + mailboxName, data, format);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds specified mailbox
	 * @param mailboxName	ID of mailbox
	 * @return	boolean
	 * @see <a href="http://api-wiki.apps.rackspace.com/api-wiki/index.php?title=Rackspace_Mailbox_(Rest_API)#Delete">Rackspace Mailbox (Rest API) Delete</a>
	 */
	public boolean delete(String mailboxName) {
		try {
			client.delete("/customers/" + customerAccountNumber + "/domains/" + domainName + "/rs/mailboxes/" + mailboxName, format);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
