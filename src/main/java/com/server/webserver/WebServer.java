package com.server.webserver;

import java.io.FileInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.StrictMath.log;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author b730
 */
public final class WebServer {

    private static int PORT;
    private static final Logger logServer = Logger.getLogger("Server_Class_Logging");
    static final Logger logRequest = Logger.getLogger("Request_Class_Logging");

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // Webserver class'ının loglarını serverLogs.txt dosyasına yazdırıyor.
        FileHandler serverLogFH = new FileHandler("serverLogs.txt");
        logServer.addHandler(serverLogFH);
        // Loglama seviyesini belirliyor. Herşeyin loglanması isteniyor.
        logServer.setLevel(Level.ALL);

        // Request class'ının loglarını requestLogs.txt dosyasınıa yazdırıyor.
        FileHandler requestLogFH = new FileHandler("requestLogs.txt");
        logRequest.addHandler(requestLogFH);
        // Loglama seviyesini belirliyor. Herşeyin loglanması isteniyor.
        logRequest.setLevel(Level.ALL);

        // Gerekli ayarlamalar yapılır.
        loadProperties();

        // WebServer nesnesi oluşturulur.
        WebServer server;
        server = new WebServer();

        // Properties dosyasında verilen port numarasına serverSocket bağlanır.
        ServerSocket serverSocket = new ServerSocket(PORT);

        // Verilen port sonsuz döngü içerisinde dinlenir.
        while (true) {
            try {
                // Talep geldiği durumda clientSocket nesnesine bağlanır.
                Socket clientSocket = serverSocket.accept();

                // Request(Thread) nesnesi oluşturulur ve ilgili istemciyle bağlantıya geçilir. 
                Request connection = new Request(clientSocket);

                // Thread başlatılır.
                new Thread(connection).start();
            } catch (IOException e) {
                logServer.log(Level.ALL, "clientSocket bağlantı problemi!!", e);
            }
        }
    }

    /**
     *
     * properties ayarları yüklenir. Port numarası properties dosyasından
     * çekilir ve "PORT" değişkenine atanır.
     */
    public static void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            // Properties dosyası açılır.
            input = new FileInputStream("config.properties");

            // properties dosyası yüklenir.
            prop.load(input);

            //config.properties dosyasından port numarası okunur ve integer'a çevrilir.
            PORT = Integer.parseInt(prop.getProperty("PORT"));

        } catch (IOException ex) {
            logServer.log(Level.ALL, "config.properties(FileInputStream) dosyası açma hatası!!", ex);
        } finally {
            // Try kısmında belirtilen dosya mevcut değilse işlem sonlandırılır.
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logServer.log(Level.ALL, "config.properties(FileInputStream) kapatma hatası!!", e);
                }
            }
        }
    }
//    public void stopServer() {
//        System.out.println("Server cleaning up.");
//        System.exit(0);
//    }
}
