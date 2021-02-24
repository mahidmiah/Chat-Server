package com.comp1549.WorkerCommands;

import com.comp1549.ServerMain;
import com.comp1549.ServerWorker;
import com.comp1549.Utils.ColouredText;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class ListCommand {

    public static void run(ServerWorker worker) throws IOException {
        if(worker.isAdmin){ // This checks if the user is an admin (Coordinator)
            ServerMain.broadcastMessage(Messages.Message_33);
            StringBuilder message = new StringBuilder();
            // The workers HashSet will be looped though, for each worker, the message string variable will be appended with the current worker in loops formatted username, IP, PORT and username.
            for (ServerWorker worker_ : ServerMain.workers){
                if (worker_.LoggedIn){
                    message.append(String.format(Messages.Message_34, worker_.formattedUsername, worker_.IP, worker_.Port, worker_.uniqueID, worker_.userName));
                }
            }
            ServerMain.broadcastMessage(message + ColouredText.ANSI_RESET); // The concatenated message is broadcast to all users on the server.
        }
        else{
            worker.outputStream.write(Messages.Message_35.getBytes());
        }
    }

}
