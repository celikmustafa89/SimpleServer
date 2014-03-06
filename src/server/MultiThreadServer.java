package server;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author b730
 */
public class MultiThreadServer {

    /**
     * WebServer nesnesi oluşturulur ve başlatılır.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        MultiThreadServer server = new MultiThreadServer();
        server.startServer();
    }

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static int connectionNumber = 0;
    private static final int MAX_CLIENT_NUMBER = 10;
    private static final int PORT = 8080;

    /**
     * WebServer başlar. Port numarası ve InetAddress kullanılarak serverSocket
     * bağlantıları yapılır. InputStream ve OutputStream oluşturulur. Request
     * nesnesi oluşturulur. Response nesnesi oluşturulur.
     *
     * @throws IOException
     */
    public void startServer() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            System.out.println(e);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                connectionNumber++;
                ClientThread connection = new ClientThread(clientSocket, connectionNumber, this);
                new Thread(connection).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void stopServer() {
        System.out.println("Server cleaning up.");
        System.exit(0);
    }
}

class ClientThread implements Runnable {

    private static final String SHUTDOWN = "/SHUTDOWN";
    private boolean shutdown = false;

    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket clientSocket;

    private int connectionID;
    private MultiThreadServer server;

    public ClientThread(Socket clientSocket, int id, MultiThreadServer server) {
        this.clientSocket = clientSocket;
        this.connectionID = id;
        this.server = server;
        System.out.println("Connection " + id + " established with: " + clientSocket);
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                //dizini pars ediyor
                Request request = new Request(inputStream);
                request.parse();

                Response response = new Response(outputStream,request);
                response.responseBack();

//                line = bufferedReader.readLine();
//                System.out.println("Received " + line + " from Connection " + connectionID + ".");
//                int n = Integer.parseInt(line);
                if (request.getUrl().equals(SHUTDOWN)) {//connection bitme şartı koymak gerekiyor
                    shutdown = true;
                }
//                if (n == 0) {
//                    break;
//                }
//                printStream.println("" + n * n);

            }

            System.out.println("Connection " + connectionID + " closed.");
            inputStream.close();
            outputStream.close();
            clientSocket.close();

            if (shutdown) {
                server.stopServer();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
