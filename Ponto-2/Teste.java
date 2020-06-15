import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.mail.EmailException;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Teste {

	// WebDriver instance
//	static WebDriver driver;

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		System.setProperty("webdriver.chrome.driver","C:\\Users\\Joao\\eclipse-workspace\\ESII\\drivers\\chromedriver.exe");
//		driver = new ChromeDriver();
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//		driver.close();   // close the tab it has opened
//		driver.quit();    // close the browser
//	}

	@Test
	public void test() throws EmailException, InterruptedException {
		String[] args= new String [0];
		g14_final g = new g14_final();
		g.main(args);
		g.verificarSite();
		g.login();
		g.verWebpages();
		g.contactUs();
		g.joinUs();
		g.htmlGenerator();
		g.enviarEmail("Yau");
		

	}

}