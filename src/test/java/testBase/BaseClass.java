package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;//log4j
import org.apache.logging.log4j.Logger;   //log4j
public class BaseClass {

	public static WebDriver driver;  //-----here make driver variable common across all objects,use STATIC keyword on base class when it is declared as example: we need to create baseclass new object, so conflict in driver variable 
	public static Logger logger;
	public Properties p;
	
	@BeforeClass(groups={"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	public void setup(String os, String br) throws IOException
	{
		logger=LogManager.getLogger(this.getClass());  //Log4j
		//System.setProperty("webdriver.chrome.driver","C:\\Users\\abisw\\OneDrive\\Arpita\\SeleniumEclipseProjects\\OpencartV121\\src\\test\\resources\\chromedriver.exe");
		
		//loading properties file
		 FileReader file=new FileReader(".//src//test//resources//config.properties");
		 p=new Properties();
		 p.load(file);

		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
				DesiredCapabilities capabilities=new DesiredCapabilities();
				
				//os
				if(os.equalsIgnoreCase("windows"))
				{
					capabilities.setPlatform(Platform.WIN11);
				}
				else if (os.equalsIgnoreCase("linux"))
				{
					capabilities.setPlatform(Platform.LINUX);
				}
				else if (os.equalsIgnoreCase("mac"))
				{
					capabilities.setPlatform(Platform.MAC);
				}
				else
				{
					System.out.println("No matching os");
					return;
				}
				
				//browser
				switch(br.toLowerCase())
				{
				case "chrome": capabilities.setBrowserName("chrome"); break;
				case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
				case "firefox": capabilities.setBrowserName("Firefox"); break;
				default: System.out.println("No matching browser"); return;
				}
				
				driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}
			
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{
	
			switch(br.toLowerCase()) 
			{
			case "chrome": driver=new ChromeDriver(); break;
			case "edge": driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default: System.out.println("No matching browser.."); return;
			}
		}
		//WebDriverManager.chromedriver().setup();
		//driver=new ChromeDriver();
	    driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		//driver.get("http://localhost/opencart/upload/index.php"); //when use hardcoded values and do not use property file
		driver.get(p.getProperty("appURL2"));
		driver.manage().window().maximize();
	}
	
	
	@AfterClass(groups={"Sanity","Regression","Master"})
	public void tearDown()
	{
		driver.close();
	}
	

	public String randomeString()
	{
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	
	public String randomeNumber()
	{
		String generatedString=RandomStringUtils.randomNumeric(10);
		return generatedString;
	}
	
	public String randomAlphaNumeric()
	{
		String str=RandomStringUtils.randomAlphabetic(3);
		String num=RandomStringUtils.randomNumeric(3);
		
		return (str+"@"+num);
	}
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		System.out.println(targetFilePath);
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}

}
