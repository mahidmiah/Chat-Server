package com.comp1549;

import com.comp1549.Utils.ColouredText;
import com.comp1549.Utils.Messages;
import com.comp1549.WorkerCommands.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class ServerWorker extends Thread {

    public final Socket clientSocket;
    public UUID uniqueID;
    public OutputStream outputStream;
    public InputStream inputStream;
    public String IP;
    public String Port;
    public String userName;
    public boolean LoggedIn = false; // Used to check if the client has logged in (has entered a username)
    public String formattedUsername;
    private BufferedReader reader;
    public boolean Disconnected = false; // Once a user removed/quits/disconnected the thread is still running, so if this boolean is true the handleClientSocket will cease to execute.
    public boolean isAdmin = false;

    public ServerWorker(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        // The ServerWorker class extends the Thread class and therefore requires the run() method.
        try {
            handleClientSocket(); // The thread will continuously call the handleClientSocket method.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        // This method handles everything that the client does.

        this.outputStream = this.clientSocket.getOutputStream();
        this.inputStream = this.clientSocket.getInputStream();
        this.reader = new BufferedReader(new InputStreamReader(inputStream)); // This is used to read all incoming messages from the client/user
        this.uniqueID = UUID.randomUUID(); //Generates a random ID for all instances
        this.IP = this.clientSocket.getInetAddress().toString(); // Clients IP
        this.Port = String.valueOf(clientSocket.getPort()); // Clients Port
        this.outputStream.write(Messages.Message_19.getBytes());
        String line;

        // If the user is not logged in, the handleClientLogin() will be called.
        if(!this.LoggedIn){
            handleClientLogin();
        }

        // If the client is not disconnected and the server is still open, the instance will continue to handle all client input.
        while (!this.Disconnected && ServerMain.serverOpen){
            try {
                if((line = reader.readLine()) != null){ // Ensures the input from the client is not blank/null
                    String[] tokens = line.split(" ", 3); //Splits the input into 3 tokens.
                    handleClientChat(tokens, line); // the handleClientChat method takes the tokens and full string (line) as input.
                }
            }
            catch (SocketException e){
                // The code block tries to handle the input in a try/catch block. If the method is unable to read from the clients socket it means the user has disconnected incorrectly.
                // Therefore, this is ERROR HANDLING: the catch block will close the clients socket using the handleClientSocket method and broadcast to the server that the client has disconnected.
                handleClientClose(); // Closes the client socket
                System.out.printf((Messages.Message_20) + "%n", this.IP, this.Port);
                ServerMain.broadcastMessage(String.format(Messages.Message_21, this.formattedUsername));
                removeAdmin(); // ERROR HANDLING: Everytime a user disconnects regularly/irregularly this method will be run to remove the user as the Coordinator (only if the user already was/is the coordinator)
            }
        }
    }

    private void handleClientLogin() {
        // This method is used to login the user, in other words it asks the user to input a valid username before they are able to communicate with other users on the server.
        try {
            this.outputStream.write(Messages.Message_22.getBytes());
            String line;
            while ((line = this.reader.readLine()) != null){
                String[] tokens = line.split(" ", 3);
                if(!tokens[0].equals("") && !String.valueOf(tokens[0].charAt(0)).equals("/")){
                    this.userName = tokens[0];
                    this.outputStream.write(String.format(Messages.Message_23, this.userName).getBytes());
                    // The below line sets the users formatted username which consists of their name and unique ID.
                    this.formattedUsername = (ColouredText.ANSI_CYAN + ColouredText.ANSI_BOLD + "[" + this.userName + " (" + this.uniqueID.toString() + ")]" + ColouredText.ANSI_RESET);
                    ServerMain.broadcastMessage(String.format(Messages.Message_24, this.formattedUsername));
                    this.LoggedIn = true;
                    StringBuilder message = new StringBuilder();
                    // The workers HashSet will be looped though, for each worker, the message string variable will be appended with the current worker in loops formatted username, IP, PORT and username.
                    for (ServerWorker worker_ : ServerMain.workers){
                        if (worker_.LoggedIn){
                            message.append(String.format(Messages.Message_34, worker_.formattedUsername, worker_.IP, worker_.Port, worker_.uniqueID, worker_.userName));
                        }
                    }
                    this.outputStream.write(message.toString().getBytes());
                    break;
                }
                else {
                    this.outputStream.write(Messages.Message_25.getBytes());
                }
            }
        }
        catch (IOException e){
            // ERROR HANDLING: If the try block cannot run it means the user has disconnected/closed without logging in.

            // The try/catch block embedded within another try/catch block as the handleClientClose throws an IOException.
            try {
                //The user has disconnected without logging in so the clients socket is closed with handleClientSocket, and the console is informed.
                handleClientClose();
                System.out.printf((Messages.Message_26) + "%n", this.IP, this.Port);
            }
            catch (IOException ignored){
                ;
            }
        }
    }

    private void handleClientChat(String[] tokens, String fullToken) throws IOException {
        // This method is used to handle the clients input/chat
        if(!tokens[0].equals("")){ //Ensures the input is valid and not null
            if("/".equalsIgnoreCase(String.valueOf(tokens[0].charAt(0)))){ // Checks if the message starts with a slash which would indicate its a command being inputted.
                handleOnCommand(tokens, fullToken); // If the message does start with a slash, then the handleOnCommand method will take the tokens and fullToken as parameters and handle it.
            }
            else{
                // if this line es executed it means that the input is not a command but rather a normal message which is to be broadcast.
                ServerMain.broadcastMessage(Messages.getTime() + " " + this.formattedUsername + ": " + fullToken + "\n"); // Broadcasts the message formatted with the users message.
            }
        }
    }

    private void handleOnCommand(String[] tokens, String fullToken) throws IOException {
        if("/quit".equalsIgnoreCase(tokens[0])){
            QuitCommand.run(this); //If the token[0] is equal to quit then the quit command method will be called.
        }
        else if("/help".equalsIgnoreCase(tokens[0])){
            HelpCommand.run(this); //If the token[0] is equal to help then the help command method will be called.
        }
        else if("/info".equalsIgnoreCase(tokens[0])){
            InfoCommand.run(this); //If the token[0] is equal to info then the info command method will be called.
        }
        else if("/msg".equalsIgnoreCase(tokens[0])){
            MessageCommand.run(this, tokens); //If the token[0] is equal to msg then the msg command method will be called.
        }
        else if("/list".equalsIgnoreCase(tokens[0])){
            ListCommand.run(this); //If the token[0] is equal to list then the list command method will be called.
        }
        else {
            //If the token[0] is not valid then the user will be informed they have inputted an unknown command.
            this.outputStream.write(Messages.Message_36.getBytes());
        }
    }

    public void handleClientClose() throws IOException {
        this.clientSocket.close(); // Closes the client socket.
        this.Disconnected = true; // Sets the Disconnected boolean to true.
        ServerMain.removeWorker(this); // Removes the clients instance from the workers hashset on the ServerMain class.
    }

    private void handleClientTimeout() throws IOException {
        System.out.printf((Messages.Message_37) + "%n", this.formattedUsername);
        handleClientClose();
        ServerMain.broadcastMessage(String.format(Messages.Message_38, this.formattedUsername));
        removeAdmin();
    }

    public boolean isStillConnected() throws IOException {
        // Checks if the user is still connected by writing to it twice, the input is byte value 0, so nothing is displayed to the user if the user is still connected.

        boolean status = true;
        try
        {
            // The output stream must be written to twice in order to check if the socket is still connected.
            this.outputStream.write(0);
            this.outputStream.write(0);
        }
        catch (IOException e)
        {
            // If an error is thrown it means that the user is not longer is connected and calls the handleClientTimeout class.
            status = false;
            handleClientTimeout();
        }
        return status;
    }

    public void removeAdmin(){
        // This method checks if the worker is an admin, if true it will set the boolean to false and the corresponding boolean on the main class to false too.

        if(isAdmin){
            isAdmin = false;
            ServerMain.adminExists = false;
        }
    }

}