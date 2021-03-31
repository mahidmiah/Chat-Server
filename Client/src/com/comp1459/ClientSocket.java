package com.comp1459;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
    public BufferedReader ServerBufferedInputReader;
    public PrintWriter serverOutputWriter;
    
	public void onServerRequestedTermination() {
		
	}
	public void onServerNormalMessageReceived(String message) {
		
	};
	public void onServerReadError(Exception e) {
		
	}
	
    
    public boolean isUnitTest;
    public String lastMessageReceivedFromServer = null;
    public String clientsList = null;
    
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
            Socket socket = new Socket(this.serverName, this.serverPort);
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
            	if (line.contains("Clients online and their info:")) {
            		clientsList = line;
            	}
                // If the received message in bytes is equivalent to closedBytes1 or 2, it will run the code below and close.
                if(Arrays.equals(line.getBytes(), closedBytes1) || Arrays.equals(line.getBytes(), closedBytes2)){
                    this.onServerRequestedTermination();
                }
                else {
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
    
    
    

}
