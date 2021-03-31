package com.comp1459.ClientCLI;
import java.util.Scanner;

import com.comp1459.ClientSocket;
import com.comp1459.ColouredText;
import com.comp1459.ClientSocket.ConnectionException;

public class ClientCLI {
	
	private static Scanner systemInputScanner;
	
	public static class ClientSocketCLI extends ClientSocket {

		public ClientSocketCLI(String serverIp, int serverPort) throws Exception {
			super(serverIp, serverPort);
		}
		
		/**
		 * :::: ClientSocket and Client-Server communication events ::::
		 * Override methods that handle Client-Server communication events
		 */
		
		@Override
		public void onServerRequestedTermination() {
			System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "Disconnected from server!" + ColouredText.ANSI_RESET);
            System.exit(0); //Exits the program.
		}
		
		@Override
		public void onServerNormalMessageReceived(String message) {
			System.out.println(message);
		}
		
		@Override
		public void onServerReadError(Exception e) {
			e.printStackTrace();
			System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Server connection lost!" + ColouredText.ANSI_RESET);
	        System.exit(0);
			
		}
		// :::: ClientSocket and Client-Server communication events ::::
		
		
		
	}
	
	public static ClientSocketCLI clientSocketCLI;
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int port = -1;
		String serverAddress = null;
		
		/**
		 * Find the parameters(e.g --port, --server-address) and its values(e.g --port {value} --server-address {value})
		 * that are in the CLI arguments
		 * 
		 */
		for (int i = 0; i < args.length; i++) {
			
			String value = args[i];
			if (value.equals("--port") && (i + 1) < args.length) {
				port = Integer.parseInt(args[i+1]);
			}
			if (value.equals("--server-address") && (i + 1) < args.length) {
				serverAddress = args[i+1];
			}
		}
		
		
		
		try {
			clientSocketCLI = new ClientSocketCLI(serverAddress, port);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			if (e1 instanceof ClientSocket.VariableException) {
				// Handles errors induced by incorrect types
				switch(((ClientSocket.VariableException) e1).variableName) {
					case "serverIp": {
						System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Please provide a valid serverIp with the --server-address <address> argument." + ColouredText.ANSI_RESET);
						System.exit(0);
						break;
					}
					case "serverPort": {
						System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Please provide a valid server port with the --port <port> argument." + ColouredText.ANSI_RESET);
						System.exit(0);
						break;
					}
					default: {
						System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] An error has occurred." + ColouredText.ANSI_RESET);
						System.exit(0);
						break;
					}
				}
				
			} else {
				System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] An error has occurred." + ColouredText.ANSI_RESET);
				System.exit(0);
			}
		}
		
		// Connect to the server
		try {
			clientSocketCLI.runClient();
			System.out.println(ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[ChatClient] Connected Successfully!" + ColouredText.ANSI_RESET);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Could not connect!" + ColouredText.ANSI_RESET);
			System.exit(0);
		}
		
		startInputReader();

	}
	
	private static void startInputReader(){
        // Everytime the user inputs a string into the console the input will be written to the server and handled on the server side.
		systemInputScanner = new Scanner(System.in);
//		while (systemInputScanner.hasNextLine()) {
//   		 	clientSocketCLI.sendMessageToServer(systemInputScanner.nextLine());
//        }
		while (systemInputScanner.hasNextLine()) {
			String line = systemInputScanner.nextLine();
			if (line.equalsIgnoreCase(".list")){
				System.out.println("Locally saved list of users:");
				for (String token : clientSocketCLI.clientsList){
					System.out.println(token);
				}
			}
			else {
				clientSocketCLI.sendMessageToServer(line);
			}
		}
    }

}
