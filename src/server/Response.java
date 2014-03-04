/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import static server.Constans.*;

/**
 *
 * @author b730
 */

public class Response {

    private static final int BUFFER_SIZE = 1024;
    private OutputStream outputStream;

    public Response(OutputStream output) {
        this.outputStream = output;
    }

    public void responseBack() throws IOException{        
        
          //varsayılan olarak index.html sayfasına yönlendirme kısmı
          if(URL.compareTo("/")==0)
             URL=URL+"index.html";

          File file = new File(ROOT_MUSTAFA, URL);
          FileInputStream fileInputStream=null;
  
          //istenen dosya mevcutsa gönderiyor
          if (file.exists()) {
              fileInputStream=new FileInputStream(file);
              byte[] bytes=new byte[1024];
              while (fileInputStream.read(bytes)!=-1) {                
                  outputStream.write(bytes);
              }
          } else {//dosya mevcut değilse hata mesajı gönderiyor
              outputStream.write(FILE_NOT_FOUND_MESSAGE.getBytes());
          }
      }
}


