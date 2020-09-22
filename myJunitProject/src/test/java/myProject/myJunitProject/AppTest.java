
package myProject.myJunitProject; 

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
	

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;



 
/** 
 * Unit test for simple App.
 */ 

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

 public class AppTest 
{ 
	 static ExtentReports extent;
	 static ExtentTest logger;
	 static WebDriver driver; 
	 
	 
  //   static String username = "kirpal_s_bhogal@hotmail.com";
  //   static String password = "Imgreat@14";
     static Properties obj = null;
     static FileInputStream objfile = null;
	 
	 WebDriverWait wait = new WebDriverWait(driver,30);
	 
	 //Email properties
	
	 // init Selenium webelements
	 
	 
	@BeforeClass 
	
	public static void setUp() throws Exception {
        
         
	     driver = new ChromeDriver();	
	    
		 driver.get("https://pe-nation.com/new-arrivals");
	     driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		 extent = new ExtentReports();
	     extent.setSystemInfo("OS", "Mac High Sierra");
		 ExtentHtmlReporter reporter=new ExtentHtmlReporter("/Users/kirpal/eclipse-workspace1/myJunitProject/Users/kirpal/screen/HtmlExtentreport.html");
		 extent.attachReporter(reporter);
		 
		 obj = new Properties();
	     objfile = new FileInputStream("/Users/kirpal/eclipse-workspace1/myJunitProject/application.properties");
	     obj.load(objfile);
	             
          
} 
	
	@SuppressWarnings({ "resource" })
	

	@Test
	public void test1_NewArrivalLoadAll() throws InterruptedException, IOException  {
	
		
		//get data from web element object repository, application.properties file
		
		
		String objPaginationViewed = obj.getProperty("objPaginationViewed");
		String objpaginationTotal = obj.getProperty("objPaginationTotal");
 
		logger=extent.createTest("Load All the products until pagination finishes");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#pge_1PmWI-d0I > div.om-global-close-button.om-popup-close > span"))).click();;
	    Thread.sleep(1000);

	    //get the current number of product visible 
	    
	    String pagingationViewed = driver.findElement(By.id(objPaginationViewed)).getText();
	  
	    // below is the total products required to be loaded at the starting it shows "16 of <max> currently 76". 
	    String paginationTotal = driver.findElement(By.id(objpaginationTotal)).getText();
	    
	    int pagingationViewedI = Integer.parseInt(pagingationViewed);
	    int paginationTotalI = Integer.parseInt(paginationTotal);
	    
	    System.out.println("pagingationViewed until now " + pagingationViewed);
	    
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    
	    // 91 products on 19th Sep, 76 products on 21st Sep, it Automatically adjusts the scrolling until max product is loaded. 
	    // Scroll until all the products are loaded. 
	    while(pagingationViewedI != paginationTotalI) {
	    	
	    	
	    //scroll by 1000 points
	    js.executeScript("window.scrollBy(0,1000)");
	    
	 	pagingationViewed = driver.findElement(By.id("pagination-viewed")).getText();
	    pagingationViewedI = Integer.parseInt(pagingationViewed);
	    System.out.println("pagingationViewed " + pagingationViewed);
	     
	    Thread.sleep(2000);
	    }
	     //get back top of the page after all the products are loaded. 
	     js.executeScript("window.scrollTo(0,0)"); 
	     
         Assert.assertEquals(paginationTotalI, pagingationViewedI);
         
         
         String temp=Utility.getScreenshot(driver,0);
         if (pagingationViewedI == paginationTotalI) {
	    	  logger.pass("All products are loaded", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
         }else {
        	 logger.fail("Complete products are not loaded", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
         }
          
         Thread.sleep(1000);
         js.executeScript("window.scrollTo(0,0)"); 
	}
	
	
	@Test
	public void test2_checkonEachProdoctAndAddtoBasket() throws Exception {
	   

		String objPopUpButton = obj.getProperty("objPopUpButton");
		String objSizeDropDown = obj.getProperty("objSizeDropDown");
		String objCartCurrentQuantity = obj.getProperty("objCartCurrentQuantity");
		String objAddToCart = obj.getProperty("objAddToCart");

 
		logger=extent.createTest("Add each of the products to the cart");
		
		String s = null, s1=null; 
		
		// get the count of all the products displayed. 
	    
	    List<WebElement> elementsxpath = driver.findElements(By.xpath("//*[@id='product-listing-container']/div/form/div/ul/li/article/figure/a/div/img"));
	    
	    System.out.println(elementsxpath.size());
	    JavascriptExecutor js = (JavascriptExecutor) driver ; 
	       
	    Thread.sleep(2000);
	       
	    try {
	    
	    	
	    	
	    for(int i=1; i<=elementsxpath.size(); i++) {
	    
	      logger.info("Adding product No: " + i + "to the cart"); 
	      String temp = null;
	    
	      s = "#product-listing-container > div > form > div > ul > li:nth-child(" + i +  ") > article > figure > a > div > img" ;
	      
	      System.out.println(s);
	   
	      //small scroll after one line of product is checked
	      
	      if ( i%4 == 0) {
	    	  js.executeScript("window.scrollBy(0,300)");
	      }
	            Thread.sleep(2000);
	      wait.until((ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s))));
	      
	      WebElement web = driver.findElement(By.cssSelector(s)); 
	      ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
          
	      s1 = "//*[@id='product-listing-container']/div/form/div/ul/li[" + i + "]/article/figure/a"; 
	      String hrefString = driver.findElement(By.xpath(s1)).getAttribute("href");
	      
	      URL url1 = new URL(hrefString);
	      HttpURLConnection http = (HttpURLConnection)url1.openConnection();
	      int statusCode = http.getResponseCode();
	      System.out.println(statusCode);
	            
	      if (statusCode ==200) { 

		      Actions action = new Actions(driver);
	    	  
	    	  action.keyDown(Keys.COMMAND).click(web).keyUp(Keys.COMMAND).perform();
	    	  wait.until((ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s))));
	    	  tabs = new ArrayList<String> (driver.getWindowHandles());
	    	  Thread.sleep(1000);
	    	  driver.switchTo().window(tabs.get(1));
	    
	    	// close the popup window.
	    	  driver.findElement(By.cssSelector(objPopUpButton)).click();

	           //select size and add to cart 
	    	  int sizeIndex=1; 
	    	  String size1=  driver.findElement(By.xpath(objSizeDropDown)).getAttribute("Class").toString();
	    	  
		      logger.info("Adding the first available size to the cart, product no: " + i ); 
		      
		      //get the first available size and add rto cart
	    	  while (size1.contains("out-of-stock")) {
	    		  
	    		 sizeIndex =sizeIndex +1;
	    		 
	    		 size1=  driver.findElement(By.xpath("//*[@id='sizes-dropdown-list']/li["+ sizeIndex + "]")).getAttribute("Class").toString();
	    		  System.out.println(sizeIndex);
	    		 if (sizeIndex>6) {
	    			 temp=Utility.getScreenshot(driver,i);
	    			 logger.fail("No size available to be added to the cart, product number: " + i ); 
	    		
	    		 }
	    		 
	    	  }
	    	  
	    	  System.out.println(sizeIndex);
	    	 
	    	  driver.findElement(By.xpath("//*[@id='sizes-dropdown-list']/li[" + sizeIndex + "]")).click();
	    	  
	
	    	  String cartCount = driver.findElement(By.xpath(objCartCurrentQuantity)).getText();  
	    	  int cartCountI = Integer.parseInt(cartCount);
	    	  
	    	  Thread.sleep(2000); 
	    	  
	    	  //click on add to cart 
	  
	    	  wait.until((ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objAddToCart)))).click();
	    	  
	    	    
	    	  Thread.sleep(2000); 
	    	  String cartCountAfterClick = driver.findElement(By.xpath(objCartCurrentQuantity)).getText();  
	    	  int cartCountIAfterClick = Integer.parseInt(cartCountAfterClick);
	    	  System.out.println("count " + cartCountAfterClick);
	    	  
	    	  temp=Utility.getScreenshot(driver,i);
	    	  
	    	  if (cartCountIAfterClick == (cartCountI +1) ) {    
	    		  
	    		 logger.info( "Navigated to the specified URL");
	    		 
	    		 logger.pass("Product No: " + i + " " + hrefString + " is Passed", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
	  		
	    		  System.out.println("Product No: " + i + " " + hrefString + " is Passed"); 
	    		  
	    		  Utility.sendEmail( "Product No: " + i + "is not added to cart with this Link: " + hrefString); 
	    		  
	    	  }
	   
	   // Failed Product in the else, Email is send with the product URL in failure, 404 or 400+ or 500+ Error
	    	  
	      else
	    	  
	    	  
	    	  {
	    	      //Fail in case of code 404 or 400+ or 500+ Error, the script will not end and will continue with other products. 
	    	  
	    	  
	    		  logger.fail("Product No: " + i + " " + hrefString + " is Failed", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
	    		  System.out.println("Product No: " + i + " " + hrefString + "is Failed"); 
	    		  
	    		  //send email with the problematic product which is not getting loaded. 
	    		  
	    		  
	    		  
	    		  
	    		  Utility.sendEmail( "Product No: " + i + "is not added to cart with this Link: " + hrefString); 
	    		  

	    	    }
	    	  	
	         }
	      
	      driver.switchTo().window(tabs.get(1)).close();
	      // get back to the main product screen
	      driver.switchTo().window(tabs.get(0));
	    	
	    } 
	    
	    } catch (Exception e) {
	    	System.out.println(e);
	    	// generate extent report before failure
	    	extent.flush();}


	}
	

	@AfterClass
	public static void tearDown() throws Exception {
     	AppTest.driver.quit();
     	// Extent Html report is generated after the flush.
		extent.flush();
	}	
	
	
	public static class Utility 

	{
		
		// screenshot function
		public static String getScreenshot(WebDriver driver, int productNumber)
		{
			TakesScreenshot ts=(TakesScreenshot) driver;
			
			File src=ts.getScreenshotAs(OutputType.FILE);
			String path="/Users/kirpal/screen/PEnation-" + productNumber+ "-time- " + System.currentTimeMillis()+".png";
			File destination=new File(path);
			try 
			{
				FileUtils.copyFile(src, destination);
				
			} catch (IOException e) 
			{
				System.out.println("Capture Failed :" + e.getMessage());
			}
			return path;
		}
		
		
		public static void sendEmail(String message) throws AddressException, MessagingException
		{
			
		     String username = "kirpal_s_bhogal@hotmail.com";
		     String password = "Imgreat@14";
          //this is outlook/hotmail only setup for now. 
	         Properties props = null;
	         if (props == null) {
	             props = new Properties();
	             props.put("mail.smtp.auth", true);
	             props.put("mail.smtp.starttls.enable", true);
	             props.put("mail.smtp.host", "smtp.live.com");
	             props.put("mail.smtp.port", "587");
	             props.put("mail.smtp.user", username);
	             props.put("mail.smtp.pwd", password);
	         }
	         Session session = Session.getInstance(props, null);
	         session.setDebug(true);
	         Message msg = new MimeMessage(session);
	         msg.setFrom(new InternetAddress(username));
	        
	             msg.setSubject("PE Nation - Products not getting added to the cart " );
           try {
	       
	         msg.setText(message);
	         msg.setRecipient(Message.RecipientType.TO, new InternetAddress("kirpal_s_bhogal@hotmail.com"));
	         Transport transport = session.getTransport("smtp");
	         transport.connect("smtp.live.com", 587, username, password);
	         transport.sendMessage(msg, msg.getAllRecipients());
	         System.out.println("Mail sent successfully at ");
	         transport.close();
	     
	         
              } catch (Exception e) {

               System.out.println(e);}
	        
			
		}
		
	}
	
}
