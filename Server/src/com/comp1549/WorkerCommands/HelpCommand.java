package com.comp1549.WorkerCommands;

import com.comp1549.ServerWorker;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class HelpCommand {

    public static void run(ServerWorker worker) throws IOException {
        // Outputs a message to the user which contains all the commands which the user can use.
            worker.outputStream.write(Messages.ClientAvailableCommands.getBytes());
    }

}
