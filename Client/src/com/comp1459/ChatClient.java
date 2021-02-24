package com.comp1459;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class ChatClient {

    private static String serverName; // This is used to store the server IP address
    private static BufferedReader ServerBufferedInputReader;
    private static Scanner systemInputScanner;
    private static PrintWriter serverOutputWriter;

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

    public static void runClient(){
        // When called the connect() method will be called which simply tries to connect to the provided IP.

        // If the connect method fails to connect, then the user will be informed and the user will have to enter another IP.
        if(!connect()){
            System.out.println(ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[ChatClient] Could not connect!" + ColouredText.ANSI_RESET);
        }
        else {
            // If the user is able to connect then the user will be informed, the startMessageReader() method and startInputReader() method will be called.
            System.out.println(ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[ChatClient] Connected Successfully!" + ColouredText.ANSI_RESET);
            startMessageReader(); // Used to read messages from the server.
            startInputReader(); // Used to handle user input into the client.
        }
    }

    private static boolean connect(){
        try {
            int serverPort = 19132;
            Socket socket = new Socket(serverName, serverPort);
            OutputStream serverOutputStream = socket.getOutputStream();
            InputStream serverInputStream = socket.getInputStream();
            ServerBufferedInputReader = new BufferedReader(new InputStreamReader(serverInputStream));
            systemInputScanner = new Scanner(System.in);
            serverOutputWriter = new PrintWriter(serverOutputStream, true);
            return true;
        }
        catch (IOException ignored){
            ;
        }
        // If the connect method fails to connect then it will return false.
        return false;
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
        byte[] closedBytes1 = {27, 91, 48, 109, 67, 76, 79, 83, 69, 68};
        byte[] closedBytes2 = {67, 76, 79, 83, 69, 68};
        try {
            while ( (line = ServerBufferedInputReader.readLine()) != null){
                // If the received message in bytes is equivalent to closedBytes1 or 2, it will run the code below and close.
                if(Arrays.equals(line.getBytes(), closedBytes1) || Arrays.equals(line.getBytes(), closedBytes2)){
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
        while (systemInputScanner.hasNextLine()) {
            serverOutputWriter.println(systemInputScanner.nextLine());
        }
    }

}
