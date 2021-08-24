package tests;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Facebooklogin {
 
	public static WebDriver driver;
	static String baseurl="http://www.facebook.com";
	//change username and password here for login
	String username="";
	String password="";
	String post_msg="Hello World";
	
	
	@BeforeMethod
	public void setup(){
		
		//disabling chrome notifications
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		//set chrome driver path here
		System.setProperty("webdriver.chrome.driver", "Drivers\\chromedriver.exe");
		driver=new ChromeDriver(options);
		driver.get(baseurl);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	}
	
	@Test
	public void CanUserPostStatusOnFacebookHome() throws InterruptedException{
		
		//finding email-id and passwd fields
		WebElement email_id=driver.findElement(By.xpath("//*[@id='email']"));
		WebElement passwd=driver.findElement(By.xpath("//*[@id='pass']"));
		
		//Login btn
		WebElement login_btn=driver.findElement(By.xpath("//input[@value='Log In']"));
		
		//passing data to fields and click on login
		email_id.sendKeys(username); Thread.sleep(2000);
		passwd.sendKeys(password); Thread.sleep(2000);
		login_btn.click();
		
		//wait, untill logged in and status box does not appears
		WebElement status_box=driver.findElement(By.xpath("//textarea[contains(@title,'Write something here')]"));
		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOf(status_box));
		
		status_box.sendKeys(post_msg); Thread.sleep(3000);
		
		//post btn
		WebElement post_btn=driver.findElement(By.xpath("//button/span[contains(text(),'Post')]"));
		
		Actions builder=new Actions(driver);
		builder.moveToElement(post_btn).build().perform();
		
		post_btn.click(); Thread.sleep(5000);
	
		Assert.assertTrue(driver.getPageSource().contains(post_msg), post_msg+" status post has been failed, Failing Test!\n\n");
		
		System.out.println(post_msg+" status post has been completed, Passing Test!\n\n");
	
}
	
	
	@AfterMethod
	public void teardown(ITestResult result){
		
		if(ITestResult.FAILURE==result.getStatus()){
			
			try{
				
				// To create reference of TakesScreenshot
				TakesScreenshot screenshot=(TakesScreenshot)driver;
				// Call method to capture screenshot
				File src=screenshot.getScreenshotAs(OutputType.FILE);
				
				// Copy files to specific location 
				// result.getName() will return name of test case so that screenshot name will be same as test case name
				FileUtils.copyFile(src, new File("screenshots\\"+result.getName()+".png"));
				System.out.println("Successfully captured a screenshot");
				
			}catch (Exception e){
				
				System.out.println("Exception while taking screenshot "+e.getMessage());
			} 
	}
		driver.quit();
		
	}
	
}
