
package myProject.myJunitProject; 



import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestCase2 {

	@Test
	public void test() {
		
	System.out.println("Hello Nice project"); 
	 
 WebDriver driver = new FirefoxDriver(); 
 driver.get("https://google.com.au");
 
 
	}
 
}
	