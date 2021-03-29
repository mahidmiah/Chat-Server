package ChatClient.Test;
import com.comp1459.ChatClient;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ClientTest {
	
	public static String username = "daniel";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ChatClient.isUnitTest = true;
		ChatClient.setServerName("127.0.0.1");
		ChatClient.runClient();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	@Timeout(30)
	void testConnect() {
		
		
		//assertTrue(ChatClient.connect());
		
		
		assertTrue(ChatClient.isConnected());
	}
	
	@Test
	void testLogin() throws InterruptedException {
		
		ChatClient.sendMessageToServer(username);
		ChatClient.sendMessageToServer("/info");
		TimeUnit.SECONDS.sleep(5);
		boolean containsUsername = ChatClient.lastMessageReceivedFromServer.contains(username);
		assertTrue(containsUsername);
	}
	
	
	@Test
	void testSendMessageToServer() throws InterruptedException {
		String message = "Hello, My name is " + username;
		ChatClient.sendMessageToServer(message);
		TimeUnit.SECONDS.sleep(5);
		boolean didServerReceiveMessage = ChatClient.lastMessageReceivedFromServer.contains(message);
		
		assertTrue(didServerReceiveMessage);
	}
	
	@Test
	void testPrivateMessaging() throws InterruptedException {
		System.out.println("");
		System.out.println("");
		System.out.println("::::::::::: JUnit: testPrivateMessaging :::::::::::::::");
		System.out.println("YOU MUST HAVE ANOTER ChatClient RUNNING!");
		String message = "This is a private message";
		Scanner scanner = new Scanner(System.in);
		System.out.println("INSERT THE RECEIVER ID:");
		String receiverId = scanner.nextLine();
		String messageToSend = "/msg " + receiverId + " " + message;
		ChatClient.sendMessageToServer(messageToSend);
		TimeUnit.SECONDS.sleep(5);
		boolean containsMessage = ChatClient.lastMessageReceivedFromServer.contains(message);
		boolean containsReceiverId = ChatClient.lastMessageReceivedFromServer.contains(receiverId);
		
		assertTrue((containsMessage && containsReceiverId));
	}
	
	@Test
	void testSendingClientsOnlineStatusToOtherClients() throws InterruptedException {
		System.out.println("::::::::::: JUnit: testPrivateMessaging :::::::::::::::");
		System.out.println("YOU MUST BE THE COORDINATOR");
		ChatClient.sendMessageToServer("/list");
		TimeUnit.SECONDS.sleep(5);
		
		assertTrue((ChatClient.clientsList != null));
	}
	
	
	

}
