
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.WebServer;

/**
 *
 * @author mustafa
 */
public class ServerUnitTest {

    public ServerUnitTest() {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("beforeclass");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("afterclass");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("setup");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("after");
    }

    @Test
    public void parseDogru() {
//        int sayi1 = 10;
//        int sayi2 = 20;
//        int gercekSonuc = 200;
//        //
//        Server islem = new Server();
//        islem.start();
//        //int sonuc = islem.carp();
//        Assert.assertTrue("Sonucta Hata Var", "index.html" == islem.);
    }

}
