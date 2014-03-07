package server;

import java.io.FileInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author b730
 */
public final class WebServer {

    /**
     * WebServer nesnesi oluşturulur ve başlatılır.
     *
     * @param args
     * @throws IOException
     */
    private static int PORT;

    public static void main(String[] args) throws IOException {

        // Gerekli ayarlamalar yapılır.
        loadProperties();
        
        // WebServer nesnesi oluşturulur.
        WebServer server = new WebServer();

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
                System.out.println(e);
            }
        }
    }

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
            System.out.println(ex);
        } finally {
            // Try kısmında belirtilen dosya mevcut değilse işlem sonlandırılır.
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
//    public void stopServer() {
//        System.out.println("Server cleaning up.");
//        System.exit(0);
//    }
}
