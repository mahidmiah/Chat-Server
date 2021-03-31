package com.comp1459;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ChatClient {

    private static String serverName; // This is used to store the server IP address
    private static BufferedReader ServerBufferedInputReader;
    private static Scanner systemInputScanner;
    private static PrintWriter serverOutputWriter;


    public static boolean isUnitTest;
    public static String lastMessageReceivedFromServer = null;
    public static Set<String> clientsList = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // This runs continuously.

        while (true){
            System.out.println(ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[ChatClient] Enter chat server IP to connect:" + ColouredText.ANSI_RESET);
            serverName = reader.readLine();
            // After an input has been read into the runClient() method.

            runClient();
        }
    }

    public static void setServerName(String name) {
        serverName = name;
    }

    public static void runClient(){
        // When called the connect() method will be called which simply tries to connect to the provided IP.

        // If the connect method fails to connect, then the user will be informed and the user will have to enter another IP.
        if(!connect()){
            System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Could not connect!" + ColouredText.ANSI_RESET);
            /*connectionEventHandler.onConnectionFailed();*/
            /* ConnectionEventHandler connectionEventHandler*/
        }
        else {
            // If the user is able to connect then the user will be informed, the startMessageReader() method and startInputReader() method will be called.
            /*connectionEventHandler.onConnected();*/
            System.out.println(ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[ChatClient] Connected Successfully!" + ColouredText.ANSI_RESET);
            startMessageReader(); // Used to read messages from the server.
            startInputReader(); // Used to handle user input into the client.
        }
    }

    public static boolean connect(){
        try {
            int serverPort = 19132;
            Socket socket = new Socket(serverName, serverPort);
            OutputStream serverOutputStream = socket.getOutputStream();
            InputStream serverInputStream = socket.getInputStream();
            ServerBufferedInputReader = new BufferedReader(new InputStreamReader(serverInputStream));
            systemInputScanner = new Scanner(System.in);
            serverOutputWriter = new PrintWriter(serverOutputStream, true);
            return true;
        } catch (IOException ignored) {
        }

        // If the connect method fails to connect then it will return false.
        return false;

    }

    public static boolean isConnected() {
        return (serverOutputWriter != null);
    }

    private static void startMessageReader(){
        // This method is a thread which continuously runs the readMessageLoop() method.
        Thread thread = new Thread(){
            @Override
            public void run(){
                readMessageLoop();
            }
        };
        thread.start();
    }

    private static void readMessageLoop(){
        String line;
        byte[] closedBytes1 = {27, 91, 48, 109, 67, 76, 79, 83, 69, 68}; //Bytes for string: CLOSED + ANSI_RESET
        byte[] closedBytes2 = {67, 76, 79, 83, 69, 68}; // Bytes for string: CLOSED
        try {
            while ( (line = ServerBufferedInputReader.readLine()) != null){
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
                    System.out.println(formattedLine);
                }
                // If the received message in bytes is equivalent to closedBytes1 or 2, it will run the code below and close.
                else if (Arrays.equals(line.getBytes(), closedBytes1) || Arrays.equals(line.getBytes(), closedBytes2)){
                    System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "Disconnected from server!" + ColouredText.ANSI_RESET);
                    System.exit(0); //Exits the program.
                }
                else {
                    // If the received message in bytes is not equivalent to closedBytes1 or 2, then it is recognised as a normal message and will be outputted to the user.
                    line = line.replaceFirst("^\\s+", "");
                    System.out.println(line);
                }
            }
        }
        catch (Exception e){
            // If an error is thrown when trying to read a line from the server, then the user will be informed and the program will be closed.
            System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Server connection lost!" + ColouredText.ANSI_RESET);
            System.exit(0);
        }
    }

    private static void startInputReader(){
        // Everytime the user inputs a string into the console the input will be written to the server and handled on the server side.

        if (!isUnitTest) {
            while (systemInputScanner.hasNextLine()) {
                String line = systemInputScanner.nextLine();
                if (line.equalsIgnoreCase(".list")){
                    System.out.println("Locally saved list of users:");
                    for (String token : clientsList){
                        System.out.println(token);
                    }
                }
                else {
                    sendMessageToServer(line);
                }
            }

        }
    }

    public static void sendMessageToServer(String message) {
        serverOutputWriter.println(message);
    }

}