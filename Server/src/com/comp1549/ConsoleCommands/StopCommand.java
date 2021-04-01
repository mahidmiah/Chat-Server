package com.comp1549.ConsoleCommands;

import com.comp1549.ServerMain;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class StopCommand {

    public static void run() throws IOException {
        ServerMain.serverOpen = false; // Sets server to closed so no messages will be handled and no more users/clients can join.
        System.out.println(Messages.PrependTimeStamp(Messages.ClosingChatServer));
        System.out.println(Messages.PrependTimeStamp(Messages.DisconnectingClients));
        ServerMain.handleDisconnectAll(); // Disconnects all users.
        System.out.println(Messages.PrependTimeStamp(Messages.DisconnectedAllClients));
        System.out.println(Messages.PrependTimeStamp(Messages.ServerShutdown));
        System.exit(0); // Exits program.
    }

}
