package com.comp1549.WorkerCommands;

import com.comp1549.ServerWorker;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class InfoCommand {

    public static void run(ServerWorker worker) throws IOException {
        // This method will output a message with all the user/clients info to the worker running the command.
        worker.outputStream.write(String.format(Messages.Message_31, worker.IP, worker.Port, worker.uniqueID, worker.userName).getBytes());
    }

}
