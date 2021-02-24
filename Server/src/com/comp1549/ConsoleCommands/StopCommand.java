package com.comp1549.ConsoleCommands;

import com.comp1549.ServerMain;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class StopCommand {

    public static void run() throws IOException {
        ServerMain.serverOpen = false; // Sets server to closed so no messages will be handled and no more users/clients can join.
        System.out.println(Messages.Message_9);
        System.out.println(Messages.Message_10);
        ServerMain.handleDisconnectAll(); // Disconnects all users.
        System.out.println(Messages.Message_11);
        System.out.println(Messages.Message_12);
        System.exit(0); // Exits program.
    }

}
