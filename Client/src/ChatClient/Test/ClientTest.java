package ChatClient.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.comp1459.ClientSocket;

class ClientTest {
	
	public static String mainClientUsername = "daniel";
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

	public static class CreateMainClientSocket {
		public static LoggerClientSocket run() throws Exception {
			LoggerClientSocket loggerClientSocket = new LoggerClientSocket(defaultServerAddress, defaultServerPort);
			loggerClientSocket.isUnitTest = true;
			loggerClientSocket.runClient();
			loggerClientSocket.sendMessageToServer(mainClientUsername);
			TimeUnit.SECONDS.sleep(2);
			return loggerClientSocket;

		}
	}

	public static class CreateClientSocket {
		public static ClientSocket run(String clientUsername) throws Exception {
			ClientSocket clientSocket = new ClientSocket(defaultServerAddress, defaultServerPort);
			clientSocket.isUnitTest = true;
			clientSocket.runClient();
			clientSocket.sendMessageToServer(clientUsername);
			TimeUnit.SECONDS.sleep(2);
			return clientSocket;

		}
	}
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		//mainClientSocket.disconnect();
	}
	
	void test() {
		fail("Not yet implemented");
	}

	/**
	 *
	 * Tests the ClientSocket connection to the Server
	 */
	@Test
	void testConnect() throws Exception {

		System.out.println(":::::::::::::::::::::::::::::::::::::::");
		System.out.println("EXECUTING: testConnect");
		System.out.println(":::::::::::::::::::::::::::::::::::::::");

		LoggerClientSocket mainClientSocket = new CreateMainClientSocket().run();

		boolean isConnected =mainClientSocket.isConnected();
		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(2);
		assertTrue(isConnected);
		
	}
	/**
	 *
	 * Tests if the user can login to the server.
	 */
	@Test
	void testLogin() throws Exception {
		System.out.println(":::::::::::::::::::::::::::::::::::::::");
		System.out.println("EXECUTING: testLogin");
		System.out.println(":::::::::::::::::::::::::::::::::::::::");

		LoggerClientSocket mainClientSocket = new CreateMainClientSocket().run();
		mainClientSocket.sendMessageToServer("/info");
		TimeUnit.SECONDS.sleep(2);

		// If the user can retrieve its information by executing the /info command
		// that means the server recognized the user and it's logged in.
		boolean containsUsername = mainClientSocket.lastMessageReceivedFromServer.contains(mainClientUsername);

		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(3);
		assertTrue(containsUsername);
	}

	/**
	 *
	 * Test Public Messaging to the server
	 */
	@Test
	void testSendMessageToServer() throws Exception {

		System.out.println(":::::::::::::::::::::::::::::::::::::::");
		System.out.println("EXECUTING: testSendMessageToServer");
		System.out.println(":::::::::::::::::::::::::::::::::::::::");

		LoggerClientSocket mainClientSocket = new CreateMainClientSocket().run();
		String message = "Hello, My name is " + mainClientUsername;
		mainClientSocket.sendMessageToServer(message);
		TimeUnit.SECONDS.sleep(5);
		boolean didServerReceiveMessage = mainClientSocket.lastMessageReceivedFromServer.contains(message);
		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(2);
		assertTrue(didServerReceiveMessage);
	}


	/**
	 *
	 * Test Private messaging
	 */
	@Test
	void testPrivateMessaging() throws Exception {

		System.out.println(":::::::::::::::::::::::::::::::::::::::");
		System.out.println("EXECUTING: testPrivateMessaging");
		System.out.println(":::::::::::::::::::::::::::::::::::::::");

		LoggerClientSocket mainClientSocket = new CreateMainClientSocket().run();

		ClientSocket johnSocket = new CreateClientSocket().run("john");

		String receiverId = mainClientSocket.getUUIDfromClientsListByUsername("john");
		String emitterId = mainClientSocket.getUUIDfromClientsListByUsername(mainClientUsername);

		System.out.println("receiverID: " + receiverId);
		System.out.println("emitterID: " + emitterId);
		String message = "This is a private message";
		String messageToSend = "/msg " + receiverId + " " + message;
		mainClientSocket.sendMessageToServer(messageToSend);
		TimeUnit.SECONDS.sleep(2);
		System.out.println("mainClientSocket.lastMessageReceivedFromServer");
		System.out.println(mainClientSocket.lastMessageReceivedFromServer);

		boolean emitterContainsMessage = mainClientSocket.lastMessageReceivedFromServer.contains(message);
		boolean emittercontainsReceiverId = mainClientSocket.lastMessageReceivedFromServer.contains(receiverId);

		System.out.println("johnSocket.lastMessageReceivedFromServer");
		System.out.println(johnSocket.lastMessageReceivedFromServer);

		boolean receiverContainsMessage = johnSocket.lastMessageReceivedFromServer.contains(message);
		boolean receiverContainsEmitterId = johnSocket.lastMessageReceivedFromServer.contains(emitterId);

		boolean didSend = (emitterContainsMessage && emittercontainsReceiverId);
		boolean didReceive = (receiverContainsMessage && receiverContainsEmitterId);


		johnSocket.disconnect();
		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(3);

		// If the server replies with the message sent and the receiver ID that means the
		assertTrue(didSend);
		assertTrue(didReceive);

	}

	/**
	 * Tests the coordinator ability to send clients's information to each client.
	 *
	 * @throws Exception
	 */
	@Test
	void testSendingClientsOnlineStatusToOtherClients() throws Exception {

		System.out.println(":::::::::::::::::::::::::::::::::::::::");
		System.out.println("EXECUTING: testSendingClientsOnlineStatusToOtherClients");
		System.out.println(":::::::::::::::::::::::::::::::::::::::");

		// Create multiple client sockets
		LoggerClientSocket mainClientSocket = new CreateMainClientSocket().run();
		ClientSocket johnSocket = new CreateClientSocket().run("john");
		ClientSocket samanthaSocket = new CreateClientSocket().run("samantha");
		ClientSocket kyleSocket = new CreateClientSocket().run("kyle");
		
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
			clientSocket.disconnect();
		}
		
		TimeUnit.SECONDS.sleep(2); // Wait for disconnections
		
		
		assertTrue(everyoneUpdatedClientsList);
		
	}

	/**
	 * Tests the behaviour of replacing the coordinator, once the previous becomes unavailable
	 */
	@Test
	void testCoordinatorSubstitution() throws Exception {
		System.out.println(":::::::::::::::::::::::::::::::::::::::");
		System.out.println("EXECUTING: testCoordinatorSubstitution");
		System.out.println(":::::::::::::::::::::::::::::::::::::::");

		// The mainClientSocket(daniel) is the coordinator because he's the only one connected.
		// Once John connects and Daniel disconnects, John should be the new coordinator
		// We test this by running the /list command as John
		// If the server replies with a message that contains "Clients online and their info", it means he has
		// permission to execute the /list command, which only the coordinator can use.
		LoggerClientSocket mainClientSocket = new CreateMainClientSocket().run();
		ClientSocket johnSocket = new CreateClientSocket().run("john");
		
		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(3); // Wait for disconnection
		johnSocket.sendMessageToServer("/list");
		TimeUnit.SECONDS.sleep(3);
		boolean isAllowed = johnSocket.lastMessageReceivedFromServer.contains("Clients online and their info");
		System.out.println("[testCoordinatorSubstitution] john's last message: " + johnSocket.lastMessageReceivedFromServer);
		
		mainClientSocket.runClient();
		mainClientSocket.sendMessageToServer(mainClientUsername);
		johnSocket.disconnect();
		mainClientSocket.disconnect();
		TimeUnit.SECONDS.sleep(3);
		assertTrue(isAllowed);
		
		
	}
	

}
