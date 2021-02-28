package com.comp1549.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Messages {

    // This class contains all the messages displayed in the server.
    // By storing them here it makes it easier to alter messages and also make it easier to read the code in the other classes.

    public static String ServerPortOpen = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Chat server running on port: %s!" + ColouredText.ANSI_RESET;

    public static String HelpCommandSuggestion = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Use the /help command to find out more." + ColouredText.ANSI_RESET;

    public static String AcceptedClientConnection = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Accepted connection from client: IP: %s, Port: %s" + ColouredText.ANSI_RESET;

    public static String SentPrivateMessage = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Me] --> %s: %s" + ColouredText.ANSI_RESET + "\n";

    public static String ReceivedPrivateMessage = getTime() + " %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " --> [Me]: " + ColouredText.ANSI_RESET + " %s\n";

    public static String RecipientNoLongerOnline = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, recipient: %s is no longer online!" + ColouredText.ANSI_RESET + "\n";

    public static String RecipientNotFoundOnServer = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, recipient: %s could not be found on the server!" + ColouredText.ANSI_RESET + "\n";

    public static String FullConsoleInput = getTime() + ColouredText.ANSI_PURPLE + ColouredText.ANSI_BOLD + " [Console]: %s" + ColouredText.ANSI_RESET + "\n";

    public static String ClosingChatServer = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Closing chat server..." + ColouredText.ANSI_RESET;

    public static String DisconnectingClients = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Disconnecting all clients..." + ColouredText.ANSI_RESET;

    public static String DisconnectedAllClients = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Disconnected all clients." + ColouredText.ANSI_RESET;

    public static String ServerShutdown = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Server shutdown." + ColouredText.ANSI_RESET;

    public static String ServerCommands = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] You can use the following commands: \n/help - View list of commands. \n/info - View your client info. (Client only) \n/quit - Disconnect from chat server. (Client only) \n/stop - Close server. (Console only) \n/list - View list of all online users. (Console only)" + ColouredText.ANSI_RESET;

    public static String CurrentClientsOnline = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Current Players online:" + ColouredText.ANSI_RESET;

    public static String ZeroUsersOnline = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " 0 users online!" + ColouredText.ANSI_RESET;

    public static String UnknownCommand = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Console] Error, Unknown command, use /help to find out more." + ColouredText.ANSI_RESET;

    public static String ClientDisconnected = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has disconnected!" + ColouredText.ANSI_RESET;

    public static String NewCoordinator = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " is now the server Coordinator." + ColouredText.ANSI_RESET + "\n";

    public static String YouAreConnectedToTheChatServer = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[Server] You are currently connected to the chat server, current time/date: " + new Date() + ColouredText.ANSI_RESET + "\n";

    public static String ClientDisconnectedWithoutQuitCommand = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Client (IP: %s, Port: %s) has disconnected without using /quit command!" + ColouredText.ANSI_RESET;

    public static String ServerClientDisconnected = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has disconnected!" + ColouredText.ANSI_RESET + "\n";

    public static String UsernamePrompt = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[Server] Please enter your username:" + ColouredText.ANSI_RESET + "\n";

    public static String ClientUsernameConfirmation = ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + "[Server] Connected as: %s" + ColouredText.ANSI_RESET + "\n";

    public static String ClientJoinedTheServer = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " has joined the server." + ColouredText.ANSI_RESET + "\n";

    public static String UsernameNotProvided = ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + "[Server] Error, no username provided! Enter username:" + ColouredText.ANSI_RESET + "\n";

    public static String ClientDisconnectedWithoutLoggingIn = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Client (IP: %s, Port: %s) has disconnected without logging in!" + ColouredText.ANSI_RESET;

    public static String ClosingClientConnection = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Closing client connection from client: IP: %s, Port: %s" + ColouredText.ANSI_RESET;

    public static String ClosedClientConnection = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] Client connection from client: IP: %s, Port: %s has been closed" + ColouredText.ANSI_RESET;

    public static String ConfirmedClientDisconnection = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has disconnected!" + ColouredText.ANSI_RESET + "\n";

    public static String ClientAvailableCommands = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] You can use the following commands: \n" + ColouredText.ANSI_GREEN + "/help - View list of commands. \n /info - View your client info. \n /quit - Disconnect from chat server. \n /msg - Private message other users. \n /list - Broadcast all clients info. (Coordinator only)" + ColouredText.ANSI_RESET + "\n";

    public static String YourClientInformation = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Your client information: \n" + ColouredText.ANSI_GREEN + "IP: %s" + "\n Port: %s" + "\n UUID: %s" + "\n Username: %s" + ColouredText.ANSI_RESET + "\n";

    public static String MessageCommandError = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, Command Usage: /msg <client UUID> <message>" + ColouredText.ANSI_RESET + "\n";

    public static String OnlineClientsListTitle = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Server] Clients online and their info:" + ColouredText.ANSI_RESET + "\n";

    public static String ClientInfo = ColouredText.ANSI_BOLD + ColouredText.ANSI_GREEN + "* %s" + ColouredText.ANSI_GREEN + ColouredText.ANSI_BOLD + " - IP: %s, Port: %s, UUID: %s, Username: %s.\n";

    public static String UnauthorizedCommand = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, You do not have permission to run this command!" + ColouredText.ANSI_RESET + "\n";

    public static String ClientUnknownCommand = getTime() + ColouredText.ANSI_RED + ColouredText.ANSI_BOLD + " [Server] Error, Unknown command, use /help to find out more." + ColouredText.ANSI_RESET + "\n";

    public static String ConsoleClientTimeout = getTime() + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " [Console] %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + " has timed out!";

    public static String ChatClientTimeout = getTime() + " %s" + ColouredText.ANSI_YELLOW + ColouredText.ANSI_BOLD + ": has timed out!" + ColouredText.ANSI_RESET + "\n";

    //This method simply just returns the current time in a sting format.
    public static String getTime(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

}
