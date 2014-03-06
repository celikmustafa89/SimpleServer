package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import static server.Constans.*;

/**
 *
 * @author b730 Browserdan gelen istekler tutulur. Gelen istek parse edilir.
 * Parse edilen sonuç Response class'ında kullanılmak üzere URL değişkenine
 * atanır.
 *
 */
public class Request {

    private InputStream inputStream;
    private String url;
    protected String URL;

    /**
     * InputStream atanır.
     *
     * @param inputStream
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * String "url" atanır.
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Kullanılan InputStream değişkenini getirir.
     *
     * @return InputStream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Kullanılan String "url" değişkenini getirir.
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Request Constructor Class'ın tek constructer metodudur. Parametre olarak
     * InputStream değişkenini alır. Çağrıldığı zaman kendi içinde "parse()"
     * metodunu çağırır.
     *
     * @param inputStream
     * @throws IOException
     */
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
    }

    /**
     * InputStream'den request'i okur. Gelen karakter dizinini konsola bastırır.
     * Request "url" ini parse eder ve alınan url'i "URL" değişkenine atar.
     *
     * @throws IOException
     */
    public void parse() throws IOException {
        String receivedMessage = null;
        byte[] buffer = new byte[1024];

        inputStream.read(buffer);
        receivedMessage = new String(buffer);

        System.out.println("(--\n" + receivedMessage + "\n--)");

        String[] inputList = receivedMessage.split("\\s+");
        if (inputList.length > 1) {
            url = inputList[1];
        } else {
            url = null;
        }
        URL = url;
    }
}
