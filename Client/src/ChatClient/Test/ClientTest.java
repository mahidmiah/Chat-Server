package ChatClient.Test;
import com.comp1459.ChatClient;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import com.comp1459.ClientSocket;
import com.comp1459.ClientSocket.ConnectionException;
class ClientTest {
	
	public static String username = "daniel";
	public static ClientSocket mainClientSocket = null;
	public static String defaultServerAddress = "127.0.0.1";
	public static int defaultServerPort = 19132;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		mainClientSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
		mainClientSocket.isUnitTest = true;
		try {
			mainClientSocket.runClient();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		mainClientSocket.disconnect();
	}
	
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	@Timeout(30)
	void testConnect() {
		
		
		
		assertTrue(mainClientSocket.isConnected());
		
	}
	
	@Test
	void testLogin() throws InterruptedException {
		
		
		mainClientSocket.sendMessageToServer(username);
		mainClientSocket.sendMessageToServer("/info");
		TimeUnit.SECONDS.sleep(2);
		boolean containsUsername = mainClientSocket.lastMessageReceivedFromServer.contains(username);
		assertTrue(containsUsername);
	}
	
	
	@Test
	void testSendMessageToServer() throws InterruptedException {
		String message = "Hello, My name is " + username;
		mainClientSocket.sendMessageToServer(message);
		TimeUnit.SECONDS.sleep(5);
		boolean didServerReceiveMessage = mainClientSocket.lastMessageReceivedFromServer.contains(message);
		
		assertTrue(didServerReceiveMessage);
	}
	
	@Test
	void testPrivateMessaging() throws Exception {
		ClientSocket johnSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
		johnSocket.runClient();
		johnSocket.sendMessageToServer("john");
		TimeUnit.SECONDS.sleep(5);
		String receiverId = mainClientSocket.getUUIDfromClientsListByUsername("john");
		System.out.println("receiverID: " + receiverId);
		String message = "This is a private message";
		String messageToSend = "/msg " + receiverId + " " + message;
		mainClientSocket.sendMessageToServer(messageToSend);
		TimeUnit.SECONDS.sleep(2);
		boolean containsMessage = mainClientSocket.lastMessageReceivedFromServer.contains(message);
		boolean containsReceiverId = mainClientSocket.lastMessageReceivedFromServer.contains(receiverId);
		johnSocket.disconnect();
		assertTrue((containsMessage && containsReceiverId));
	}
	
	@Test
	void testSendingClientsOnlineStatusToOtherClients() throws InterruptedException {
		mainClientSocket.sendMessageToServer("/list");
		TimeUnit.SECONDS.sleep(2);
		assertFalse((mainClientSocket.clientsList.isEmpty()));
		
	}
	
	
	

}
