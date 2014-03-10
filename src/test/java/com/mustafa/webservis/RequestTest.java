package com.mustafa.webservis;

import java.io.IOException;
import java.net.Socket;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author mustafa
 */
public class RequestTest extends TestCase {

    public RequestTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of splitRequest() method, of class Request.
     */
    public void testSplitRequest() throws IOException {

        System.out.println("splitRequest");
        Socket socket = new Socket();
        Request instance = new Request(socket);
        String result = instance.splitRequest("GET /denemetest.html HTTP/1.1");
        Assert.assertEquals("Request doğru şekilde split edilmiyor.", result, "./denemetest.html");

    }
    
    public void testCreateContentType() throws IOException{
        System.out.println("ContentType");
        Socket socket = new Socket();
        Request instance = new Request(socket);
        
        String result = instance.createContentType("./index.html");
        Assert.assertEquals("html uzantısı doğru split edilmiyor.", result,"text/html" );
        
        result = instance.createContentType("./jpg.jpg");
        Assert.assertEquals("jpg uzantısı doğru split edilmiyor.", result,"text/jpg" );
        
        result = instance.createContentType("./gif.gif");
        Assert.assertEquals("gif uzantısı doğru split edilmiyor.", result,"text/gif" );
        
        result = instance.createContentType("./zzzz.zzzz");
        Assert.assertEquals("default uzantısı doğru split edilmiyor.", result,"application/octet-stream" );
    }

}
