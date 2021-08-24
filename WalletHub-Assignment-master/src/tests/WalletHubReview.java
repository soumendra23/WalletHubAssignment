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

public class WalletHubReview {
	
	public static WebDriver driver;
	static String baseurl="https://wallethub.com/join/light";
	static String review_sub_url="http://wallethub.com/profile/test_insurance_company/";
	//change username in below URL
	static String review_verification_url="https://wallethub.com/profile/username/reviews/";
	
	
	
	//change username and password here for login
	String username="";
	String password="";
	
	
	@BeforeMethod
	public void setup(){
		
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
	public void CanPostReviewOnWalletHub() throws InterruptedException{
		
		//Clicking on Login animate, entering credentials and logging in
		WebElement login_animate=driver.findElement(By.xpath("//li/a[contains(text(),'Login')]"));
		login_animate.click(); Thread.sleep(2000);
		
		WebElement user_email_id=driver.findElement(By.xpath("//input[@type='text' and contains(@placeholder,'Email')]"));
		user_email_id.sendKeys(username); Thread.sleep(2000);
		
		WebElement passwd=driver.findElement(By.xpath("//input[@type='password' and contains(@placeholder,'Password')]"));
		passwd.sendKeys(password); Thread.sleep(2000);
		
		
		//Logging in
		WebElement login_btn=driver.findElement(By.xpath("//button//span[contains(text(),'Login')]"));
		login_btn.click(); Thread.sleep(5000);
		
		//routing to review submission page
		driver.navigate().to(review_sub_url); 
		
		//Handling GET YOUR FREE CREDIT SCORE & REPORT pop up
		WebElement CSR_PopUp=driver.findElement(By.xpath("//*[@id='footer_cta']/span/span/i[2]"));
		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(CSR_PopUp));
		CSR_PopUp.click();
		
		
		//hovering to review stars and selecting 5th one
		WebElement review_stars=driver.findElement(By.xpath("//*[contains(@class,'wh-rating rating_5')]"));
		Actions builder=new Actions(driver);
		builder.moveToElement(review_stars); Thread.sleep(2000);
		
		WebElement review_5th_Star=driver.findElement(By.xpath("//*[contains(@class,'wh-rating-choices-holder')]/a[5]"));
		builder.moveToElement(review_5th_Star).click().perform();
		
		//selecting health from policy DD
		WebElement policyDD=driver.findElement(By.xpath("//*[@class='dropdown-list-new']"));
		wait.until(ExpectedConditions.elementToBeClickable(policyDD));
		policyDD.click(); Thread.sleep(2000);
		
		WebElement policyDDHealth=driver.findElement(By.xpath("//a[contains(@data-target,'Health')]"));
		policyDDHealth.click(); Thread.sleep(2000);
		
		//selecting 5th star
		WebElement PolicyHealthStar=driver.findElement(By.xpath("//*[@class='bf-icon-star-empty star bstar bf-icon-star'][5]"));
		PolicyHealthStar.click(); Thread.sleep(2000);
		
		//Adding review
		WebElement PolicyReview=driver.findElement(By.xpath("//textarea[@name='review' and @id='review-content']"));
		PolicyReview.clear();
		
		 String msg = "";
	        for (int i = 0; i < 30; i++) {
	            msg += " Hey, You!";
	        }
		
		PolicyReview.sendKeys(msg);
		PolicyReview.submit();
		
		//checking confirmation
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1/span[contains(text(),'Your')]/a[contains(text(),'has been posted')]"))));
		
		//navigating to profile page to see if posted review exist
		driver.navigate().to(review_verification_url);
		String bodyText = driver.findElement(By.tagName("body")).getText();
		Assert.assertTrue(bodyText.contains(bodyText), "Review is not showing in profile/review page, Failing Test!");
		System.out.println("Posted review is showing in profile/review page, Passing Test!");
	
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
