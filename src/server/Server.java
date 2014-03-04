/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import static server.Constans.*;


/**
 *
 * @author b730
 */


public class Server {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

     public void start() throws IOException {
          ServerSocket serverSocket=null;
        try {
         
            serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName("localhost"));
        }
        catch (IOException e) {
          System.exit(1);
        }

        while (true) {
          Socket socket = null;
          InputStream input = null;
          OutputStream output = null;

          socket = serverSocket.accept();
          input = socket.getInputStream();
          output = socket.getOutputStream();

          Request request = new Request(input);//dizini pars ediyor
 
          Response response = new Response(output);
          response.responseBack();
          
          socket.close();
        }
    }
}
