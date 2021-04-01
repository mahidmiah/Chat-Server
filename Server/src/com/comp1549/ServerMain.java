package com.comp1549;

import com.comp1549.ConsoleCommands.HelpCommand;
import com.comp1549.ConsoleCommands.ListCommand;
import com.comp1549.ConsoleCommands.StopCommand;
import com.comp1549.Utils.ColouredText;
import com.comp1549.Utils.Messages;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {

    public static Set<ServerWorker> workers = new HashSet<>(); //This HashSet will store all instances of ServerWorkers (clients).
    private static Scanner systemInputScanner; //This input scanner is used to read console input.
    public static boolean serverOpen = true; //This will be set to true by default, whilst true the server will accept new connections and handle communication between clients.
    public static boolean adminExists = false; //This boolean is used to check if an Admin (Coordinator) exists.

    public static void main(String[] args) {
        int port = -1;

        /**
         * Find the port parameter (--port) and its value (--port {value})
         * that are in the arguments
         *
         */
        for (int i = 0; i < args.length; i++) {

            String value = args[i];
            if (value.equals("--port") && (i + 1) < args.length) {
                port = Integer.parseInt(args[i+1]);
            }

        }

        //int port = 19132; //This is the default/fixed random port used for the server.
        systemInputScanner = new Scanner(System.in);
        startSetAdminLoop(); //This method extends the Thread class in order to continuously run a method which checks if an Admin (Coordinator) exists, if not, one will be selected by the method.
        startInputReader(); //This methods also extends the Thread class to continuously call another method to check for user/console input.
        try {
            ServerSocket serverSocket = new ServerSocket(port); //Server socket, server is now join-able (As long as serverOpen is true).
            System.out.printf((Messages.PrependTimeStamp(Messages.ServerPortOpen)) + "%n", port);
            System.out.println(Messages.PrependTimeStamp(Messages.HelpCommandSuggestion));
            while (serverOpen){
                Socket clientSocket = serverSocket.accept(); //Accepts any clients trying to join.
                System.out.printf((Messages.PrependTimeStamp(Messages.AcceptedClientConnection)) + "%n", clientSocket.getInetAddress(), clientSocket.getPort());

                //Everytime a new client connection is accepted, a new ServerWorker instance will be created and added to the workers hashset.
                ServerWorker serverWorker = new ServerWorker(clientSocket);
                workers.add(serverWorker);
                serverWorker.start(); //The serverWorker class extends the Thread class therefore we must start() it.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeWorker(ServerWorker worker){
        workers.remove(worker); //Removes worker from the hashset.
    }

    public static void broadcastMessage(String message) throws IOException {
        // This method is used to broadcast messages to all users (including the console) onm the server.
        //message is the message being sent.

        if (serverOpen){ // Will only run if the server is open, this is FAULT HANDLING: as while the server is closing (serverOpen is false) the server should not broadcast messages, as users can be disconnected the same moment they are sent the message which will cause errors.
            System.out.print(message);
            for (ServerWorker worker : workers){ //Loops through all ServerWorkers in the workers hashset.
                if (worker.isStillConnected()){ //Checks if the user/client is still connected (hasn't timed out).
                    if(worker.LoggedIn){ //Checks if the user/client is logged in, the user shouldn't receive messages if they are not logged in.
                        worker.outputStream.write(message.getBytes()); //The user is sent the message which is being broadcast. (message is the parameter)
                    }
                }
            }
        }
    }

    public static void sendDirectMessage(ServerWorker senderWorker, String receiverUUID, String message) throws IOException {
        //This method is used to send private messaged from one user/client to another specific user/client on the server.
        //senderWorker is the client sending the message.
        //receiverUUID the unique ID of the client receiving the message.
        //message is the private message being sent.

        if (serverOpen){ // Will only run if the server is open, this is FAULT HANDLING: as while the server is closing (serverOpen is false) the server should not send private messages, as users can be disconnected the same moment they are sent the message which will cause errors.
            boolean userFound = false; // This boolean will be used to record if the user has been found on the server.
            for (ServerWorker worker : workers){ //Loops through all the ServerWorkers in the workers hashset.
                if (worker.uniqueID.toString().equals(receiverUUID)){ //Checks if the workers unique ID matches that of the specified one.
                    if(worker.isStillConnected()){ // if the user is found, this will check that the user is still connected (hasn't timed out). ERROR HANDLING: if the user has timed out, sending the user private messages will cause errors.
                        //Sends the private messages between the two users/client.
                        senderWorker.outputStream.write(String.format(Messages.PrependTimeStamp(Messages.SentPrivateMessage), worker.formattedUsername, message).getBytes());
                        worker.outputStream.write(String.format(Messages.PrependTimeStamp(Messages.ReceivedPrivateMessage), senderWorker.formattedUsername, message).getBytes());
                        userFound = true; //Sets to true so that the user not found message will not be displayed.
                    }
                    else {
                        // If the user is no longer connected (timed out), the sender worker will be informed.
                        userFound = true; //Set to true so the user not found message will not be displayed.
                        senderWorker.outputStream.write(String.format(Messages.PrependTimeStamp(Messages.RecipientNoLongerOnline), receiverUUID).getBytes());
                    }
                    break;
                }
            }
            if(!userFound){ // If after the loop is finished, and no user is found the sender worker will be informed.
                senderWorker.outputStream.write(String.format(Messages.PrependTimeStamp(Messages.RecipientNotFoundOnServer), receiverUUID).getBytes());
            }
        }
    }

    private static void startInputReader(){
        //Runs thread which calls the readInputReaderLoop method.
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    readInputReaderLoop();
                } catch (IOException ignored) {
                    ;
                }
            }
        };
        thread.start();
    }

    private static void readInputReaderLoop() throws IOException {
        //this method continuously checks for console input.
        while (systemInputScanner.hasNextLine()) {
            String line = systemInputScanner.nextLine();
            String[] tokens = line.split(" ", 3);
            handleConsoleInput(tokens, line); //if there is input the input will handled by the handleConsoleInput method.

        }
    }

    private static void handleConsoleInput(String[] tokens, String fullToken) throws IOException {
        //This method checks whether the console input is just a message to be broadcast or a command.
        if(!tokens[0].equals("")){ //Checks that the input is valid
            if("/".equalsIgnoreCase(String.valueOf(tokens[0].charAt(0)))){
                //If the input starts with a slash, it is recognised as a command and handled by the handleOnConsoleCommand method.
                handleOnConsoleCommand(tokens, fullToken);
            }
            else{
                //If the message doesn't start with a slash is recognised as normal message and broadcast.
                broadcastMessage(String.format(Messages.PrependTimeStamp(Messages.FullConsoleInput), fullToken));
            }
        }
    }

    private static void handleOnConsoleCommand(String[] tokens, String fullToken) throws IOException {
        //This method handles all console commands.
        if("/stop".equalsIgnoreCase(tokens[0])){
            StopCommand.run(); //Runs stop command.
        }
        else if("/help".equalsIgnoreCase(tokens[0])){
            HelpCommand.run(); //Runs help command.
        }
        else if("/list".equalsIgnoreCase(tokens[0])){
            ListCommand.run(); //Runs list command.
        }
        else {
            //If no valid command is inputted the console is informed by an error message.
            System.out.println(Messages.PrependTimeStamp(Messages.UnknownCommand));
        }
    }

    public static void handleDisconnectAll() throws IOException {
        //This method disconnects all users from the server.

        for(ServerWorker worker : workers){
            System.out.printf((Messages.PrependTimeStamp(Messages.ClientDisconnected)) + "%n", worker.formattedUsername);
            worker.outputStream.write("CLOSED".getBytes()); //Sends the client the bytes to close/disconnect.

            //The code below is the same has the handleClientClose() function from the ServerWorker class excluding 'workers.remove(worker)' as this is not possible and threw an error.
            worker.clientSocket.close();
            worker.Disconnected = true;
            //workers.remove(worker); This is not possible, it is not possible to remove the object from the hash set while looping through it. Although this does not work,the system functions as planned, disconnecting/closing all clients successfully.
        }
    }

    private static void startSetAdminLoop(){
        //This thread continuously calls the setAdminLoop method.
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    setAdminLoop();
                } catch (IOException ignored) {
                    ;
                }
            }
        };
        thread.start();
    }

    private static void setAdminLoop() throws IOException {
        //This method will continuously run and check whether a admin (coordinator) exists or not, if one does not exists then it at random will pick a new coordinator.

        while (serverOpen){ // Checks if the server is open. ERROR HANDLING: The server should not pick a new admin (Coordinator) while the server is closing (serverOpen set to false).
            if (!adminExists && (workers.size() > 0)){ // Checks if and admin doesn't exist, and also checks if there are any users connected, a new admin cant be picked if there are not connected clients.
                for (ServerWorker worker: workers){
                    if (worker.LoggedIn){ //Ensures the user is logged in, as the user cannot become the admin if they are not logged in.
                        worker.isAdmin = true; //Sets the isAdmin boolean to true
                        adminExists = true; //Sets the adminExists boolean to true
                        worker.formattedUsername = ColouredText.ANSI_PURPLE + ColouredText.ANSI_BOLD + "[Coordinator] " + worker.formattedUsername; //Alters the clients username to include [Coordinator].
                        System.out.println(String.format(Messages.PrependTimeStamp(Messages.NewCoordinator), worker.formattedUsername));
                        broadcastMessage(String.format(Messages.PrependTimeStamp(Messages.NewCoordinator), worker.formattedUsername)); //Broadcast to the server that this user is now the new Coordinator.
                        break;
                    }
                }
            }
            else{
                //Unless System.out.* method is run for the else part of the if statement, the loop does not run at all, I don't understand why.
                //A solution found is to run System.out.flush() which doesn't affect the systems operations.
                System.out.flush();
            }
        }
    }

}