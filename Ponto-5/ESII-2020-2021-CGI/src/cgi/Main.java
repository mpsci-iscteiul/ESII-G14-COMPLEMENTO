package cgi;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

// TODO: Auto-generated Javadoc
/**
 * The Class hello.
 */
class Main
{
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws XPathExpressionException the x path expression exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 * @throws InvalidRemoteException the invalid remote exception
	 * @throws TransportException the transport exception
	 * @throws GitAPIException the git API exception
	 */
	public static void main( String args[] ) 
			throws XPathExpressionException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, InvalidRemoteException, TransportException, GitAPIException
	{
		
		String URI = "https://github.com/vbasto-iscte/ESII1920";
		File directory = new File("./gitdata");
		generateRDF(URI, directory);


		//  Here is a minimalistic CGI program that uses cgi_lib
		//  Print the required CGI header.
		System.out.println(cgi_lib.Header());

		//  Parse the form data into a Hashtable.
		@SuppressWarnings("rawtypes")
		Hashtable form_data = cgi_lib.ReadParse(System.in);
//
//		// Create the Top of the returned HTML page
//		//	System.out.println("Here are the name/value pairs from the form:");
//
//		//
		XmlProject xmlproject = new XmlProject("gitdata/covid19spreading.rdf");
		String query = (String) form_data.get("Query");
		String result = xmlproject.getQuery(query);
//
//		printFile("header.html");
//
//
		printHTMLHeader();
		//	Print the name/value pairs sent from the browser.
		System.out.println(cgi_lib.Variables(form_data));

		System.out.println("<h2>");
		System.out.println("Result: " + result);
		System.out.println("</h2>");
		
		
		printHTMLBottom();
//		System.out.println(cgi_lib.HtmlBot());
		
//		printFile("footer.html");
	}
	
	/**
	 * Generate RDF.
	 *
	 * @param URI the uri
	 * @param directory the directory
	 * @throws InvalidRemoteException the invalid remote exception
	 * @throws TransportException the transport exception
	 * @throws GitAPIException the git API exception
	 */
	private static void generateRDF(String URI, File directory) throws InvalidRemoteException, TransportException, GitAPIException {
		Git.cloneRepository()
				.setURI(URI)
				.setDirectory(directory).call();
	}
	


	/**
	 * Prints the file.
	 *
	 * @param fileName the file name
	 */
	private static void printFile(String fileName) {
		String line;
		File html = new File(fileName);
		try {
			Scanner scanner = new Scanner(html);
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				System.out.println(line);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void printHTMLHeader() {
		System.out.println("<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<style>\r\n" + 
				"body {\r\n" + 
				"  font-family: Helvetica;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"h1 {\r\n" + 
				"  font-family: Arial Black;\r\n" + 
				"  color: #4682B4;\r\n" + 
				"}\r\n" + 
				"</style>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"\r\n" + 
				"<h1>Covid-19 Query: </h1>");
	}
	
	private static void printHTMLBottom() {
		System.out.println("</body>\r\n" + 
				"</html>");
	}
}
