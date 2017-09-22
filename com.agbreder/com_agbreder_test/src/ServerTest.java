import org.junit.Test;

/**
 * Testador lexical
 * 
 * @author Bernardo Breder
 */
public class ServerTest extends AbstractTest {
	
	@Test
	public void test() throws InterruptedException {
		Thread.sleep(1000);
		js("logining()");
		Thread.sleep(1000);
		c.findElementById("left-index-inbox");
		Thread.sleep(10 * 60 * 1000);
	}
	
}
