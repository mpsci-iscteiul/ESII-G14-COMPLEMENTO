import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

// TODO: Auto-generated Javadoc
/**
 * The Class g14.
 */
public class g14_final {

	/** The driver. */
	public static WebDriver driver;
	
	/** The Website state. */
	private static String WebsiteState = "Available";
	
	/** The Email state. */
	private static String EmailState = "Available";
	
	/** The Forms state. */
	private static String FormsState = "Available";
	
	/** The Webpages state. */
	private static String WebpagesState = "Available";
	
	/** The Login state. */
	private static String LoginState = "Available";
	
	/** The Website. */
	private static String Website = "http://192.168.99.100:8000/";
	
	/** The Titulo website. */
	private static String TituloWebsite = "Grupo 14 – Engenharia de Software II, 2º Semestre 2019/2020, Licenciatura em LEI/LIGE/LETI – Mais um site WordPress";
	
	/** The Username. */
	private static String Username = "g14";
	
	/** The Password. */
	private static String Password = "g14";
	
	/** The Name. */
	private static String Name = "tester";
	
	/** The Surname. */
	private static String Surname = "tester";
	
	/** The Affiliation. */
	private static String Affiliation = "ISCTE";
	
	/** The Password registo. */
	private static String PasswordRegisto = "test123";
	
	/** The Subject. */
	private static String Subject = "Covid19";
	
	/** The Message. */
	private static String Message = "Isto é só um teste, não liguem";
	
	/** The Email administrador. */
	private static String EmailAdministrador = "grupo14esii@gmail.com";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 * @throws EmailException the email exception
	 */
	public static void main(String[] args) throws InterruptedException, EmailException{
		while(true) {
			System.setProperty("webdriver.chrome.driver","C:\\Users\\laura\\OneDrive\\Ambiente de Trabalho\\ES I\\es\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			verificarSite();
			if(WebsiteState.equals("Available")) {
				login();
				verWebpages();
				contactUs();
				joinUs();
				htmlGenerator();
			}
			driver.close();
			Thread.sleep(1200000);
		}
	}

	/**
	 * Verificar site.
	 */
	public static void verificarSite() {
		driver.navigate().to(Website);
		String titulo= driver.getTitle();
		if(TituloWebsite.equals(titulo)) {
			System.out.println("entramos no site " + TituloWebsite);
		}
		else {
			System.out.println("O site "+ titulo + " está em baixo");
			try {
				enviarEmail("O Site esta em baixo");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			WebsiteState="Unavailable";
			LoginState="Inconclusivo";
			WebpagesState="Inconclusivo";
			FormsState="Inconclusivo";
			EmailState="Inconclusivo";
		}

	}

	/**
	 * Login.
	 *
	 * @throws EmailException the email exception
	 */
	public static void login() throws EmailException {
		try {
			Actions scroll = new Actions(driver);
			WebElement elemento = driver.findElement(By.linkText("Iniciar sessão"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elemento);
			Thread.sleep(1500);
			scroll.moveToElement(driver.findElement(By.linkText("Iniciar sessão"))).click().perform();
			Thread.sleep(1500);
			WebElement username = driver.findElement(By.id("user_login"));
			username.sendKeys(Username);
			WebElement password = driver.findElement(By.id("user_pass"));
			password.sendKeys(Password);
			driver.findElement(By.id("wp-submit")).click();
			Thread.sleep(1500);	
			try {
				driver.findElement(By.id("login_error"));
				enviarEmail("Nao consegui fazer Login");
				LoginState="Unavailable";
				WebpagesState="Inconclusivo";
				FormsState="Inconclusivo";
				EmailState="Inconclusivo";
			}catch(Throwable e1) {	
			}
			driver.findElement(By.id("wp-admin-bar-site-name")).click();
			Thread.sleep(1500);	
		}
		catch (Throwable e) {
			e.printStackTrace();
			enviarEmail("Nao consegui fazer Login");
			LoginState="Unavailable";
			WebpagesState="Inconclusivo";
			FormsState="Inconclusivo";
			EmailState="Inconclusivo";
		}
	}

	/**
	 * Ver webpages.
	 *
	 * @throws EmailException the email exception
	 */
	public static void verWebpages() throws EmailException {
		try {
			driver.findElement(By.id("menu-item-90")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-92")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-91")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-93")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-82")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-84")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-85")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-86")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("menu-item-87")).click();
			Thread.sleep(1500);
		} catch (Throwable e) {
			e.printStackTrace();
			WebpagesState="Unavailable";
			FormsState="Inconclusivo";
			EmailState="Inconclusivo";
			enviarEmail("As Webpages não estão todas funcionais");
		}
	}

	/**
	 * Contact us.
	 *
	 * @throws EmailException the email exception
	 */
	public static void contactUs() throws EmailException {
		try {
			driver.findElement(By.id("menu-item-88")).click();
			((JavascriptExecutor)driver).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			driver.get("https://generator.email/");
			Thread.sleep(5000); 
			String email= driver.findElement(By.id("email_ch_text")).getText();
			driver.close();
			driver.switchTo().window(tabs.get(0));
			driver.findElement(By.linkText("Contact Us")).click();
			Thread.sleep(2000); 
			driver.findElement(By.id("wpforms-104-field_4")).sendKeys(Subject);
			driver.findElement(By.id("wpforms-104-field_3")).sendKeys(email);
			driver.findElement(By.id("wpforms-104-field_2")).sendKeys(Message);
			Thread.sleep(1000);
			driver.findElement(By.id("wpforms-104-field_2")).sendKeys(Keys.ENTER);
			Thread.sleep(4000);
			driver.findElement(By.id("menu-item-82")).click();
			
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EmailState="Unavailable";
			FormsState="Inconclusivo";
			enviarEmail("Houve um problema a com a página de Contacto do site");
		}
	}

	/**
	 * Join us.
	 *
	 * @throws EmailException the email exception
	 */
	public static void joinUs() throws EmailException {
		try {
			driver.findElement(By.id("menu-item-89")).click();
			((JavascriptExecutor)driver).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			driver.get("https://generator.email/");
			Thread.sleep(5000); 
			String email= driver.findElement(By.id("email_ch_text")).getText();
			driver.close();
			driver.switchTo().window(tabs.get(0));
			driver.findElement(By.linkText("Join Us")).click();
			Thread.sleep(1500); 
			driver.findElement(By.id("first_name")).sendKeys(Name);
			driver.findElement(By.id("last_name")).sendKeys(Surname);
			driver.findElement(By.id("input_box_1592137025")).sendKeys(Affiliation);
			driver.findElement(By.id("user_email")).sendKeys(email);
			driver.findElement(By.id("user_pass")).sendKeys(PasswordRegisto);
			Thread.sleep(1000);
			driver.findElement(By.id("user_pass")).sendKeys(Keys.ENTER);
			Thread.sleep(2000);
			Actions scroll = new Actions(driver);
			WebElement elemento = driver.findElement(By.id("menu-item-82"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elemento);
			Thread.sleep(1500);
			scroll.moveToElement(driver.findElement(By.id("menu-item-82"))).click().perform();

		}catch (Throwable e) {
			e.printStackTrace();
			FormsState="Unavailable";
			enviarEmail("Houve um problema a com os Formulários do site");
		}
	}

	/**
	 * Enviar email.
	 *
	 * @param CorpoDeTexto the corpo de texto
	 * @throws EmailException the email exception
	 */
	public static void enviarEmail(String CorpoDeTexto) throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("grupo14esii@gmail.com","grupo14esii123"));
		email.setSSLOnConnect(true);
		email.setFrom("grupo14esii@gmail.com");
		email.setSubject("ERRO NO SITE");
		email.setMsg(CorpoDeTexto);
		email.addTo(EmailAdministrador);
		email.send();
	}

	/**
	 * Html generator.
	 */
	public static void htmlGenerator () {
		try {
		SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
		String time = formatter.format(System.currentTimeMillis());
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));
		driver.get("https://www.cs.iupui.edu/~ajharris/webprog/jsTester.html");
		WebElement caixaTexto = driver.findElement(By.id("txtInput"));
		caixaTexto.click();
		String html = "<html><body><h1>Ponto 2-Teste do Site</h1><table border=\"10\" style=\"width:100%\" height=\"auto\"><th>Login</th><th>WebPages</th><th>Email</th><th>Forms</th><th>Data</th><th>Website</th></tr><tr><td>"+WebpagesState+"</td><td>"+LoginState+"</td><td>"+EmailState+"</td><td>"+FormsState+"</td><td>"+time+"</td><td>"+WebsiteState+"</td></tr></table>";
		caixaTexto.sendKeys(html);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input[value='show the output']")).click();
		Thread.sleep(250000);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}