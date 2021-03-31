package com.comp1549.WorkerCommands;

import com.comp1549.ServerMain;
import com.comp1549.ServerWorker;
import com.comp1549.Utils.Messages;

import java.io.IOException;

public class QuitCommand {

    public static void run(ServerWorker worker) throws IOException {
        byte[] closedBytes = {67, 76, 79, 83, 69, 68}; // The close client message in bytes.
        worker.outputStream.write(closedBytes);
        System.out.printf((Messages.PrependTimeStamp(Messages.ClosingClientConnection)) + "%n", worker.IP, worker.Port);
        worker.handleClientClose(); // Closes the client socket
        System.out.printf((Messages.PrependTimeStamp(Messages.ClosedClientConnection)) + "%n", worker.IP, worker.Port);
        ServerMain.broadcastMessage(String.format(Messages.PrependTimeStamp(Messages.ConfirmedClientDisconnection), worker.formattedUsername)); // Broadcasts the user/client has disconnected.
        worker.removeAdmin(); // Checks and removes the user as a Coordinator.
    }

}
