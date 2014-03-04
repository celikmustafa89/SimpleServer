/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.File;

/**
 *
 * @author b730
 */
public class Constans {
     public static final String ROOT_MUSTAFA =    System.getProperty("user.dir") +"/mustafa";
     public static final int PORT = 8080;
     static String FILE_NOT_FOUND_MESSAGE = "HTTP/1.1 404 File Not Found\r\n" +
          "Content-Type: text/html\r\n" +
          "Content-Length: 23\r\n" +
          "\r\n" +
          "<h1>File Not Found</h1>";
     public static String URL=null;
}
