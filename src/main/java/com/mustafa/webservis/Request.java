package com.mustafa.webservis;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;

/**
 *
 * @author b730 Browserdan gelen talepler parse edilir. Talep edilen dosya adı
 * belirlenir. Dosya mevcutsa gerekli düzenleme yapılıp cevap döndürülür. Dosya
 * mevcut değilse gerekli düzenleme yapılıp cevap döndürülür.
 *
 */
public final class Request implements Runnable {

    private Socket socket;
    private static final String CRLF = "\r\n";
    private static final String FILE_NOT_FOUND_MESSAGE = "<HTML>"
            + "<HEAD><TITLE>404 Not Found</TITLE></HEAD>"
            + "<BODY>Not Found</BODY></HTML>";
    private static final int BUFFER_SIZE = 1024;

    /**
     *
     * @param socket
     * @throws IOException
     */
    public Request(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void run() {
        try {
            startRequest();
        } catch (Exception e) {
            WebServer.logRequest.log(Level.ALL, "Thread start problem !!", e);
        }
    }

    private void startRequest() throws IOException, Exception {

        InputStream inputStream;
        inputStream = socket.getInputStream();

        DataOutputStream dataOutputStream;

        dataOutputStream = new DataOutputStream(
                socket.getOutputStream());

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(inputStream));

        /*
         // Gelen isteğin IP adresini yazdırma kısmı kodları
         InetAddress incomingAddress = socket.getInetAddress();
         String ipString = incomingAddress.getHostAddress();
         System.out.println("The incoming address is:   " + ipString);
         */
        // Talebin ilk satırı okunur.(Muhtemel talep: "GET /index.html HTTP/1.1")
        String lineRead = br.readLine();

        // Okunan ilk satır ekrana yazdırılır.
        System.out.println("\n" + lineRead);

        // Talebin ilk satırı istenen dosya adını ayrıştırmak için 
        // fonksiyona gönderilir. (Muhtemel talep: "GET /index.html HTTP/1.1")
        // Fonksiyon return değeri olarak talep edilen dosya adını döndürür("./index.html").
        WebServer.logRequest.entering("Request", "splitRequest(String )", lineRead);
        String fileName = splitRequest(lineRead);

        String takenText = null;
        // Gelen metinin tamamı ekrana bastırılır.
        while ((takenText = br.readLine()).length() != 0) {
            System.out.println(takenText);
        }

        WebServer.logRequest.entering("Request", "response(string fileName, DataOutputStream)", fileName);
        response(fileName, dataOutputStream);

        // Streamler ve soketler kapatılır.
        dataOutputStream.close();
        br.close();
        socket.close();

    }

    /**
     * Talebe göre cevap hazırlanır. Hazırlanan cevap parametre ile gelen stream
     * üzerinden gönderilir.
     *
     * @param fileName
     * @param dataOutputStream
     * @throws IOException
     * @throws Exception
     */
    private void response(String fileName, DataOutputStream dataOutputStream)
            throws IOException, Exception {

        // Talep edilen dosya mevcut ise açılır. 
        // Talep edilen dosya mevcut değil ise FileNotFoundException verilir.
        FileInputStream fileInputStream = null;
        boolean fileExists = true;
        try {
            fileInputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            WebServer.logRequest.log(Level.ALL, "Talep edilen dosya \"" + fileName + "\" bulunamadı!!", e);
            fileExists = false;
        }

        // Cevap mesajı oluşturulur.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;

        if (fileExists) { // Talep edilen dosyanın mevcut olma durumu için hazırlanan cevap
            statusLine = "HTTP/1.1 200 OK: ";
            contentTypeLine = "Content-Type: "
                    + createContentType(fileName) + CRLF;
            WebServer.logRequest.info("Talep edilen " + fileName + " mevcut, cevap hazırlanıyor...");
        } else { // Talep edilen dosyanın mevcut OLMAMA durumu için hazırlanan cevap
            statusLine = "HTTP/1.1 404 Not Found: ";
            contentTypeLine = "Content-Type: text/html" + CRLF;
            entityBody = FILE_NOT_FOUND_MESSAGE;
            WebServer.logRequest.info("Talep edilen " + fileName + " mevcut değil, cevap hazırlanıyor...");
        }

        // Status-Line istemciye gönderilir.
        dataOutputStream.writeBytes(statusLine);

        // Content-Type istemciye gönderilir.
        dataOutputStream.writeBytes(contentTypeLine);

        // Header-Line kısmının bittiğini ifade eden CRLF gönderilir.
        dataOutputStream.writeBytes(CRLF);

        if (fileExists) {   // Talep edilen dosya mevcutsa gönderilir ve dosya stream'i kapatılır.
            sendBytes(fileInputStream, dataOutputStream);
            fileInputStream.close();
            WebServer.logRequest.info("Talep edilen " + fileName + " mevcut, cevap gönderiliyor...");
        } else {    // Talep edilen dosya mevcut değilse OLMAMA durumu mesajı gönderilir.
            dataOutputStream.writeBytes(entityBody);
            WebServer.logRequest.info("Talep edilen " + fileName + " mevcut değil, cevap gönderiliyor...");
        }
    }

    /**
     * Eğer talep edilen dosya mevcutsa buffer boyutu kadar parçalar halinde
     * istemciye gönderilir.
     *
     * @param fileInputStream
     * @param outputStream
     * @throws Exception
     */
    private void sendBytes(FileInputStream fileInputStream, OutputStream outputStream)
            throws Exception {

        int numberOfBytes;
        byte[] buffer = new byte[BUFFER_SIZE];

        // Dosya içindeki metin, dosya sonuna kadar okunur
        //(her döngüde bir buffer boyutu kadar[1024] okunur ve stream'e gönderilir).
        while ((numberOfBytes = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, numberOfBytes);
        }
    }

    /**
     * Test kısmı yazıldı. Gerekli MIME'ler ayarlanır.
     *
     * @param fileName
     * @return Talep edilen dosya tipine karşılık gönderilecek olan content-type
     * string'i oluşturulur ve döndürülür.
     */
    public String createContentType(String fileName) {

        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".jpg")) {
            return "text/jpg";
        }
        if (fileName.endsWith(".gif")) {
            return "text/gif";
        }
        return "application/octet-stream";
    }

    /**
     * Test kısmı yazıldı.
     *
     * @param lineRead -> Talebin ilk satırını ifade eder.
     * @return Talep edilen dosya adı döndürülür.
     */
    public String splitRequest(String lineRead) {

        
        // Talep parçalara bölünür.(Talep Örn.: "GET /index.html HTTP/1.1" )
        // (alınması gereken dosya ismi: "/index.html")
        StringTokenizer tokens = new StringTokenizer(lineRead);
        tokens.nextToken();  // "GET /index.html HTTP/1.1" talebinin "GET" kısmı atlanır.
        String fileName = tokens.nextToken(); // Talep edilen dosya adı("/index.html") alınır.

        //varsayılan olarak index.html sayfasına yönlendirme kısmı
        if (fileName.compareTo("/") == 0) {
            fileName = fileName + "index.html";
        }

        // Aranan dosyanın(file), 
        // şuan içinde bulunulan klasör(directory) içinde olduğu belirtilir.
        // işlem sonunda "./index.html" stringi oluşur. 
        return "." + fileName;
    }
}
