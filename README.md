## Client Server Communication

Members:
- Abdullah Mahmood 001065350
- Lucas Gomes 001051104
- Mahid Miah 001063915
- Shan Shah Alam 001087719
- Shane Joel 001074473

### Running the Client-Server setup
Before running the client, a server must be online.

#### Server Setup
The server can be initiated by running the script with the `--port <port>` argument:


`Server/src/com/comp1549/ServerMain.java --port 19132`


In this example we used the port 19132 but any port value is valid.

#### Client Setup
Once the server is online, a client can be initiated.

To run the CLI, run the script with the `--port <port> --server-address <ip>` arguments:


`Client/src/com/comp1459/ClientCLI/ClientCLI.java --port 19132 --server-address 127.0.0.1`


The `--port` argument specifies the server port, the `--server-address` specifies the server's IP address.


### Running Tests
- JUnit 5 is a requirement to run the tests.
- A Server must be online, listening to the port 19132

The tests can be executed by running the script:

`Client/src/ChatClient/Test/ClientTest.java`



