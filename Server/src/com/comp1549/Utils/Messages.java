package com.comp1549.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Messages {

    // This class contains all the messages displayed in the server.
    // By storing them here it makes it easier to alter messages and also make it easier to read the code in the other classes.

    public static String ServerPortOpen = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Chat server running on port: %s!" + ColouredText.ANSI_RESET;

    public static String HelpCommandSuggestion = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Use the /help command to find out more." + ColouredText.ANSI_RESET;

    public static String AcceptedClientConnection = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Accepted connection from client: IP: %s, Port: %s" + ColouredText.ANSI_RESET;

    public static String SentPrivateMessage = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Me] --> %s: %s" + ColouredText.ANSI_RESET + "\n";

    public static String ReceivedPrivateMessage = " %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " --> [Me]: " + ColouredText.ANSI_RESET + " %s\n";

    public static String RecipientNoLongerOnline = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, recipient: %s is no longer online!" + ColouredText.ANSI_RESET + "\n";

    public static String RecipientNotFoundOnServer = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, recipient: %s could not be found on the server!" + ColouredText.ANSI_RESET + "\n";

    public static String FullConsoleInput = ColouredText.ANSI_PURPLE + ColouredText.ANSI_BOLD + " [Console]: %s" + ColouredText.ANSI_RESET + "\n";

    public static String ClosingChatServer = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Closing chat server..." + ColouredText.ANSI_RESET;

    public static String DisconnectingClients = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Disconnecting all clients..." + ColouredText.ANSI_RESET;

    public static String DisconnectedAllClients = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Disconnected all clients." + ColouredText.ANSI_RESET;

    public static String ServerShutdown = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Server shutdown." + ColouredText.ANSI_RESET;

    public static String ServerCommands = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] You can use the following commands: \n/help - View list of commands. \n/info - View your client info. (Client only) \n/quit - Disconnect from chat server. (Client only) \n/stop - Close server. (Console only) \n/list - View list of all online users. (Console only)" + ColouredText.ANSI_RESET;

    public static String CurrentClientsOnline = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Current Players online:" + ColouredText.ANSI_RESET;

    public static String ZeroUsersOnline = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " 0 users online!" + ColouredText.ANSI_RESET;

    public static String UnknownCommand = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Console] Error, Unknown command, use /help to find out more." + ColouredText.ANSI_RESET;

    public static String ClientDisconnected = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has disconnected!" + ColouredText.ANSI_RESET;

    public static String NewCoordinator = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " is now the server Coordinator." + ColouredText.ANSI_RESET + "\n";

    public static String YouAreConnectedToTheChatServer = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] You are currently connected to the chat server, current time/date: " + new Date() + ColouredText.ANSI_RESET + "\n";

    public static String ClientDisconnectedWithoutQuitCommand = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Client (IP: %s, Port: %s) has disconnected without using /quit command!" + ColouredText.ANSI_RESET;

    public static String ServerClientDisconnected = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has disconnected!" + ColouredText.ANSI_RESET + "\n";

    public static String UsernamePrompt = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Please enter your username:" + ColouredText.ANSI_RESET + "\n";

    public static String ClientUsernameConfirmation = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Connected as: %s" + ColouredText.ANSI_RESET + "\n";

    public static String ClientJoinedTheServer = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " has joined the server." + ColouredText.ANSI_RESET + "\n";

    public static String UsernameNotProvided = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[Server] Error, no username provided! Enter username:" + ColouredText.ANSI_RESET + "\n";

    public static String ClientDisconnectedWithoutLoggingIn = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Client (IP: %s, Port: %s) has disconnected without logging in!" + ColouredText.ANSI_RESET;

    public static String ClosingClientConnection = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Closing client connection from client: IP: %s, Port: %s" + ColouredText.ANSI_RESET;

    public static String ClosedClientConnection = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Client connection from client: IP: %s, Port: %s has been closed" + ColouredText.ANSI_RESET;

    public static String ConfirmedClientDisconnection = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has disconnected!" + ColouredText.ANSI_RESET + "\n";

    public static String ClientAvailableCommands = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] You can use the following commands: \n" + ColouredText.ANSI_GREEN + "/help - View list of commands. \n /info - View your client info. \n /quit - Disconnect from chat server. \n /msg - Private message other users. \n /list - Broadcast all clients info. (Coordinator only)" + ColouredText.ANSI_RESET + "\n";

    public static String YourClientInformation = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Your client information: \n" + ColouredText.ANSI_GREEN + "IP: %s" + "\n Port: %s" + "\n UUID: %s" + "\n Username: %s" + ColouredText.ANSI_RESET + "\n";

    public static String MessageCommandError = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, Command Usage: /msg <client UUID> <message>" + ColouredText.ANSI_RESET + "\n";

    public static String OnlineClientsListTitle = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Clients online and their info:" + ColouredText.ANSI_RESET;

    public static String ClientInfo = ColouredText.ANSI_BOLD + ColouredText.ANSI_GREEN + "# %s" + ColouredText.ANSI_GREEN + ColouredText.ANSI_BOLD + " - IP: %s, Port: %s, UUID: %s, Username: %s.";

    public static String UnauthorizedCommand = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, You do not have permission to run this command!" + ColouredText.ANSI_RESET + "\n";

    public static String ClientUnknownCommand = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, Unknown command, use /help to find out more." + ColouredText.ANSI_RESET + "\n";

    public static String ConsoleClientTimeout = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " has timed out!";

    public static String ChatClientTimeout = " %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has timed out!" + ColouredText.ANSI_RESET + "\n";

    //This method simply just returns the current time in a sting format.
    public static String getTime(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static String PrependTimeStamp(String message){
        return getTime() + message;
    }

}
