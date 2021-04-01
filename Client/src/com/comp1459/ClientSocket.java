package com.comp1459;

//import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.comp1549.ServerWorker;


public class ClientSocket {
	public static class ConnectionException extends Exception
	{
		public ConnectionException(String message)
		{
			super(message);
		}
	}
	
	public static class VariableException extends Exception
	{
		public String variableName;
		public VariableException(String message, String variableName)
		{
			super(message);
			this.variableName = variableName;
		}
	}
	
	String serverName; // This is used to store the server IP address
	int serverPort = 19132;
	public Socket socket;
    public BufferedReader ServerBufferedInputReader;
    public PrintWriter serverOutputWriter;
    
    public void onServerMessageReceived(String message) {
		
	}
    
	public void onServerRequestedTermination() {
		
	}
	public void onServerNormalMessageReceived(String message) {
		
	};
	public void onServerReadError(Exception e) {
		
	}
	
    
    public boolean isUnitTest;
    public String lastMessageReceivedFromServer = null;
    public Set<String> clientsList = new HashSet<>();
    
    public ClientSocket(String serverIp, int serverPort) throws Exception {
        // This runs continuously.
    	
    	if (serverIp == null || serverIp == "") {
    		throw new VariableException("Please provide a server IP.", "serverIp");
    	}
    	if (serverPort < 1) {
    		throw new VariableException("Server Port must be greater than 0", "serverPort");
    	}
        this.serverName = serverIp;
        this.serverPort = serverPort;
	}
    
    public void runClient() throws ConnectionException{
        // When called the connect() method will be called which simply tries to connect to the provided IP.

        // If the connect method fails to connect, then the user will be informed and the user will have to enter another IP.
    	
        if(!connect()){
        	throw new ConnectionException("Could not connect to server: " + this.serverName + ":" + this.serverPort);
        }
        
        startMessageReader(); // Used to read messages from the server.
    }
        
    
    
    public boolean connect(){
        try {
            socket = new Socket(this.serverName, this.serverPort);
            
            OutputStream serverOutputStream = socket.getOutputStream();
            InputStream serverInputStream = socket.getInputStream();
            ServerBufferedInputReader = new BufferedReader(new InputStreamReader(serverInputStream));
            serverOutputWriter = new PrintWriter(serverOutputStream, true);
            return true;
        } catch (IOException ignored) {
        }
        
        // If the connect method fails to connect then it will return false.
        return false;
        
    }
    
    public void disconnect() throws IOException {
    	this.sendMessageToServer("/quit");
    	Thread thread = new Thread(){
            @Override
            public void run(){
            	try {
            		TimeUnit.SECONDS.sleep(30);
					if (socket.isClosed() == false) {
						socket.close();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };
        thread.start();
    }
    
    public boolean isConnected() {
    	return socket.isConnected();
    }
    
    private void startMessageReader(){
        // This method is a thread which continuously runs the readMessageLoop() method.
        Thread thread = new Thread(){
            @Override
            public void run(){
                readMessageLoop();
            }
        };
        thread.start();
    }
    
    private void readMessageLoop(){
        String line;
        byte[] closedBytes1 = {27, 91, 48, 109, 67, 76, 79, 83, 69, 68}; //Bytes for string: CLOSED + ANSI_RESET
        byte[] closedBytes2 = {67, 76, 79, 83, 69, 68}; // Bytes for string: CLOSED
        try {
            while ( (line = ServerBufferedInputReader.readLine()) != null){
            	lastMessageReceivedFromServer = line;
            	onServerMessageReceived(line);
                if (line.contains("Clients online and their info")) {
                    String[] tokens = line.split("#");
                    String formattedLine = "";
                    for (int i = 0; i < tokens.length; i++) {
                    	
                    	String token = tokens[i];
                    	formattedLine = formattedLine + token + "\n";
                    	if (i > 0) {
                    		clientsList.add(token);
                    	}
                    	
                    }
                    this.onServerNormalMessageReceived(formattedLine);
                } else if (line.contains("has disconnected")) {
                	// Remove Disconnected user from the clientsList
                	
                	for (Iterator<String> clientsListIT = clientsList.iterator(); clientsListIT.hasNext(); ) {
                	    String clientInfo = clientsListIT.next();
                	    
                	    // Expected input: [john (e82cfa84-76a0-4118-8c33-86c1a39aebdc)] has disconnected
                	    // We use Regex to capture the username and UUID;
                	    Pattern p = Pattern.compile("\\[([a-zA-Z0-9!@#$&()\\\\\\-`\\.\\+\\,\\/\\\"]+)\\s\\(([a-zA-Z0-9\\\\-]+)\\)\\]");
                	    Matcher m = p.matcher(line);
                	    
                	    if (m.find()) {
                	    	String disconnectedUsername = m.group(1);
                	    	String disconnectedUUID = m.group(2);
                	    	if (clientInfo.contains(disconnectedUUID)) {
                	    		clientsListIT.remove();
                    	    }
                	    }
                	    
                	}
                	this.onServerNormalMessageReceived(line);
                	
                } else if (line.contains("has joined the server")) {
                	// Add connected user to the clientsList
                	
                	
                	// Expected input: [john (e82cfa84-76a0-4118-8c33-86c1a39aebdc)] has joined the server
            	    // We use Regex to capture the username and UUID;
                	Pattern p = Pattern.compile("\\[([a-zA-Z0-9!@#$&()\\\\\\-`\\.\\+\\,\\/\\\"]+)\\s\\(([a-zA-Z0-9\\\\-]+)\\)\\]");
            	    Matcher m = p.matcher(line);
            	    if (m.find()) {
            	    	String newElement = "UUID: " + m.group(2) + ", Username: " + m.group(1);
            	    	// Add to the clients List with the format: "UUID: <uuid>, Username: <username>"
            	    	clientsList.add(newElement);
            	    }
                	this.onServerNormalMessageReceived(line);
                } else if(Arrays.equals(line.getBytes(), closedBytes1) || Arrays.equals(line.getBytes(), closedBytes2)) {
                	// If the received message in bytes is equivalent to closedBytes1 or 2, it will run the code below and close.
                    this.onServerRequestedTermination();
                } else {
                    // If the received message in bytes is not equivalent to closedBytes1 or 2, then it is recognised as a normal message and will be outputted to the user.
                    line = line.replaceFirst("^\\s+", "");
                    this.onServerNormalMessageReceived(line);
                    
                }
            }
        }
        catch (Exception e){
            // If an error is thrown when trying to read a line from the server, then the user will be informed and the program will be closed.
            this.onServerReadError(e);
        }
    }
    
    public void sendMessageToServer(String message) {
    	serverOutputWriter.println(message);
    }
	public static String escapeSpecialRegexChars(String str) {
		Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
		return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
	}
    public String getUUIDfromClientsListByUsername(String targetUsername) throws Exception {
    	
    	String targetClientInfo = null;

    	for(String clientInfo : clientsList) {
    		String usernameRegex = "Username:\\s" + escapeSpecialRegexChars(targetUsername);
    		Pattern p = Pattern.compile(usernameRegex);

    		Matcher m = p.matcher(clientInfo);
    		if (m.find()) {
				targetClientInfo = clientInfo;
				break;
    		}
    	}
    	
    	if (targetClientInfo == null) {
    		throw new Exception("User '" + targetUsername + "' not found.");	
    	}
    	System.out.println("FOUND TARGET INFO: " + targetUsername + " = " + targetClientInfo);
    	String uuidRegex = "UUID:\\s([a-zA-Z0-9\\-]+)";
    	Pattern p = Pattern.compile(uuidRegex);
		Matcher m = p.matcher(targetClientInfo);
		
		String clientUUID = null; 
		if (m.find()) {
			clientUUID = m.group(1);
		} else {
			throw new Exception("User '" + targetUsername + "' not found.");
		}
		return clientUUID;
    	
    }
    
    
    

}
