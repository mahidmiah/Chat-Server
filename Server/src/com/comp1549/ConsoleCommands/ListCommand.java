package com.comp1549.ConsoleCommands;

import com.comp1549.ServerMain;
import com.comp1549.ServerWorker;
import com.comp1549.Utils.Messages;

public class ListCommand {

    public static void run(){
        //This command outputs all current online users on the console.

        System.out.println(Messages.CurrentClientsOnline);
        if (ServerMain.workers.isEmpty()){
            // if there are not users online it will inform the user/console that no one is online.
            System.out.println(Messages.ZeroUsersOnline);
        }
        else {
            // If there are players online then the workers HashSet will be looped through and for each worker their formatted username will be displayed.
            for (ServerWorker worker : ServerMain.workers){
                if (worker.LoggedIn){ // Ensures the user is logged in, if the used is not logged in they wont have a formatted username.
                    System.out.println(" " + worker.formattedUsername);
                }
            }
        }
    }

}
