package server;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import static server.Constans.*;

/**
 * Browserdan gelen isteğe cevap verir. Request objesinin parse ettiği url
 * değişkenini kullanarak ilgili içerikleri browsera gönderir.
 *
 * @author b730
 */
public class Response {

    private static final String FILE_NOT_FOUND_MESSAGE = "HTTP/1.1 404 File Not Found\r\n"
            + "Content-Type: text/html\r\n"
            + "Content-Length: 23\r\n"
            + "\r\n"
            + "<h1>File Not Found</h1>";
    private static final int BUFFER_SIZE = 1024;
    private OutputStream outputStream = null;
    private Request request = null;

    /**
     * Varolan tek yapıcıdır. OutputStream parametresini alır.
     *
     * @param output
     * @param request1
     */
    public Response(OutputStream output, Request request1) {
        this.outputStream = output;
        request = request1;
    }

    /**
     * Request nesnesinin almış olduğu isteğe göre browser'a cevap verir.
     *
     * @throws IOException
     */
    public void responseBack() throws IOException {

        //varsayılan olarak index.html sayfasına yönlendirme kısmı
        if (request.URL.compareTo("/") == 0) {
            request.URL = request.URL + "index.html";
        }

        File file = new File(ROOT_MUSTAFA, request.URL);
        FileInputStream fileInputStream = null;

        //istenen dosya mevcutsa gönderiyor
        if (file.exists()) {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            while (fileInputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
        } else {//dosya mevcut değilse hata mesajı gönderiyor
            outputStream.write(FILE_NOT_FOUND_MESSAGE.getBytes());
        }
    }
}
