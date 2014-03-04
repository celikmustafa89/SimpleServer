/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import static server.Constans.*;

/**
 *
 * @author b730
 */

public class Request {

    private InputStream input;
    private String url;

    public void setInput(InputStream input) {
        this.input = input;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public InputStream getInput() {
        return input;
    }
    public String getUrl() {
        return url;
    }  
    
    public Request(InputStream input) throws IOException {
        this.input = input;
        parse();
    }
    public void parse() throws IOException {
        String str=null;
        byte[] buffer = new byte[1024];
        
        input.read(buffer);
        str = new String(buffer);
        
        System.out.println("(--\n"+str+"\n--)");
        
        String [] inputList=str.split("\\s+");
        if (inputList.length>1) {
            url= inputList[1];
        }else
            url= null;
        URL=url;
    }
}
