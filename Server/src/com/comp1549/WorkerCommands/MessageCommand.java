package com.comp1549.WorkerCommands;

import com.comp1549.ServerMain;
import com.comp1549.ServerWorker;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class MessageCommand {

    public static void run(ServerWorker worker, String[] Tokens) throws IOException {
        // This will ensure that the correct number of parameters are inputted.
        if(Tokens.length == 3){
            ServerMain.sendDirectMessage(worker, Tokens[1], Tokens[2]); // The sendDirectMessage method from the Main Method will be called with the relevant parameters.
        }
        else {
            // If there are not the number of correct parameters inputted, then the user/client will be informed.
            worker.outputStream.write(Messages.Message_32.getBytes());
        }
    }

}
