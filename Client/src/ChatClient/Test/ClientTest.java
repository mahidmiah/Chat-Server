package ChatClient.Test;
import com.comp1459.ChatClient;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
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
	
	public static class LoggerClientSocket extends ClientSocket {

		public LoggerClientSocket(String serverIp, int serverPort) throws Exception {
			super(serverIp, serverPort);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onServerMessageReceived(String message) {
			System.out.println("-- DANIEL RECEIVED LINE --");
			System.out.println(message);
			System.out.println("--------------------------");
		}
		
	}
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		mainClientSocket = new LoggerClientSocket(defaultServerAddress, defaultServerPort);
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
		System.out.println("mainClientSocket.lastMessageReceivedFromServer");
		System.out.println(mainClientSocket.lastMessageReceivedFromServer);
		boolean containsMessage = mainClientSocket.lastMessageReceivedFromServer.contains(message);
		boolean containsReceiverId = mainClientSocket.lastMessageReceivedFromServer.contains(receiverId);
		johnSocket.disconnect();
		assertTrue((containsMessage && containsReceiverId));
	}
	
	@Test
	void testSendingClientsOnlineStatusToOtherClients() throws Exception {
		
		
		// Create multiple client sockets
		ClientSocket johnSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
		johnSocket.runClient();
		johnSocket.sendMessageToServer("john");
		TimeUnit.SECONDS.sleep(2);
		ClientSocket samanthaSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
		samanthaSocket.runClient();
		samanthaSocket.sendMessageToServer("samantha");
		TimeUnit.SECONDS.sleep(2);
		ClientSocket kyleSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
		kyleSocket.runClient();
		kyleSocket.sendMessageToServer("kyle");
		TimeUnit.SECONDS.sleep(2);
		
		
		
		HashSet<ClientSocket> socketSet = new HashSet<ClientSocket>();
		socketSet.add(mainClientSocket);
		socketSet.add(johnSocket);
		socketSet.add(samanthaSocket);
		socketSet.add(kyleSocket);
		
		
		// CLEAN CLIENTS LIST OF EACH CLIENT
		for (ClientSocket clientSocket : socketSet) {
			clientSocket.clientsList = new HashSet<>(); 
		}
		
		mainClientSocket.sendMessageToServer("/list");
		TimeUnit.SECONDS.sleep(6);
		
		boolean everyoneUpdatedClientsList = true;
		
		
		// Verify if the clientsList was populated for each client after emitting the /list command
		for (ClientSocket clientSocket : socketSet) {
			if (clientSocket.clientsList.isEmpty()) {
				clientSocket.sendMessageToServer("/info");
				TimeUnit.SECONDS.sleep(2);
				System.out.println("CLIENT WHO DID NOT RECEIVE");
				System.out.println(clientSocket.lastMessageReceivedFromServer);
				everyoneUpdatedClientsList = false;
				break;
			}
		}
		
		// Disconnect temporary sockets
		for (ClientSocket clientSocket : socketSet) {
			if (clientSocket != mainClientSocket) {
				clientSocket.disconnect();
			}
		}
		
		TimeUnit.SECONDS.sleep(2); // Wait for disconnections
		
		
		assertTrue(everyoneUpdatedClientsList);
		
	}
	
	@Test
	void testCoordinatorSubstitution() throws Exception {
		
		// The mainClientSocket(daniel) is the coordinator because he's the only one connected.
		// Once John connects and Daniel disconnects, John should be the new coordinator
		// We test this by running the /list command as John
		// If the server message contains "Clients online and their info", it means he has
		// permission to execute the /list command, which only the coordinator can use.
		
		ClientSocket johnSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
		johnSocket.runClient();
		johnSocket.sendMessageToServer("john");
		
		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(3); // Wait for disconnections
		johnSocket.sendMessageToServer("/list");
		TimeUnit.SECONDS.sleep(3);
		boolean isAllowed = johnSocket.lastMessageReceivedFromServer.contains("Clients online and their info");
		
		
		mainClientSocket.runClient();
		mainClientSocket.sendMessageToServer(username);
		johnSocket.disconnect();
		System.out.println("IS CONNECTED" + mainClientSocket.isConnected());
		
		
		assertTrue(isAllowed);
		
		
	}
	

}
