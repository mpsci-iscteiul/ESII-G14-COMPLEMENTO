package cgi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlProject.
 */
public class XmlProject {


	/** The doc. */
	private Document doc;
	
	/** The xpath. */
	private XPath xpath;

	/**
	 * Instantiates a new xml project.
	 *
	 * @param fileName the file name
	 */
	public XmlProject(String fileName) {
		File inputFile = new File(fileName);    	      	 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(inputFile);
			this.doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			this.xpath = xpathFactory.newXPath();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("impossível ler o ficheiro pedido");
			e.printStackTrace();
		}         

	}

	/**
	 * Gets the output.
	 *
	 * @param input the input
	 * @return the output
	 * @throws XPathExpressionException the x path expression exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public String getOutput(String input) throws XPathExpressionException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String[] splitInput = splitString(input);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < splitInput.length; i++) {
			String result = getTranslated(splitInput[i]);
			sb.append(result);
			sb.append(" ");
		}
		return(sb.toString());
	}

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
	 */
//	public static void main(String[] args) throws XPathExpressionException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		XmlProject xmlproject = new XmlProject("covid19spreading.rdf");
//		String query = "/NamedIndividual/text()/@*";
//		String translation = xmlproject.getOutput(query);
//		System.out.println(translation);
//		String result = xmlproject.QueryResult(translation);
//		System.out.println(result);
//	}
	
	/**
	 * Gets the query.
	 *
	 * @param input the input
	 * @return the query
	 * @throws XPathExpressionException the x path expression exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public String getQuery(String input) throws XPathExpressionException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String translation = getOutput(input);
		String result = QueryResult(translation);
		return result;
	}

	/**
	 * Query result.
	 *
	 * @param query the query
	 * @return the string
	 */
	public String QueryResult(String query)  {

		String result;
		try {
		XPathExpression expr = xpath.compile(query);
		result = (String) expr.evaluate(doc, XPathConstants.STRING);
		if(result=="" || result.contains("#")) {
			ArrayList<String> array = new ArrayList<String>();
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				String nodeValue = node.getNodeValue();
				nodeValue = StringUtils.substringAfter(nodeValue, "#");
				array.add(nodeValue);
			}
			return array.toString();
		}
		} catch(Exception e)  {
			result = "Erro-> Verifique que as funcoes e condicoes comecam com letra maiuscula";
		}

		return result;
	}

	/**
	 * Gets the translated.
	 *
	 * @param word the word
	 * @return the translated
	 */
	public String getTranslated(String word) {
		String result = "";
		if(word.contains("(")) {
			String function = StringUtils.substringBefore(word, "(");
			String argument = StringUtils.chop(StringUtils.remove(word, function)).substring(1);
//			System.out.println("Function: " + function);
//			System.out.println("Argument: " + argument);

			switch (function) {
			case "sum":
			case "Sum":
			case "count":
			case "Count":
				String aritArgument = getTranslated(argument);
				result = aritOperator(function,aritArgument);
				break;
			case "Testes" :
			case "Internamentos" :
			case "Infecoes" :
				result = operation(function,argument);
				break;
			case "Regiao" :
				result = regiao(argument);
				break;
			}
		}
		else result = word;
		return result;
	}


	/**
	 * Operation.
	 *
	 * @param operacao the operacao
	 * @param regiao the regiao
	 * @return the string
	 */
	public String operation(String operacao, String regiao) {
		String query = "";
		if(regiao.contentEquals("all"))
			query = "//*/"+ operacao +"/text()";
		else
			query = "//*[contains(@about,'"+ regiao +"')]/" + operacao + "/text()";
		return query;
	}

	/**
	 * Regiao.
	 *
	 * @param cond the cond
	 * @return the string
	 */
	public String regiao(String cond) {
		String query ="";
		if(cond.contentEquals("all"))
			query = "//NamedIndividual/@*";
		else
			query = "//*["+ cond + "]/@*";
		return query;
	}

	/**
	 * Arit operator.
	 *
	 * @param arit the arit
	 * @param argument the argument
	 * @return the string
	 */
	public String aritOperator(String arit,String argument) {
		String query = arit.toLowerCase() + "(" + argument + ")";
		return query;
	}

	/**
	 * Split string.
	 *
	 * @param subject the subject
	 * @return the string[]
	 */
	public String[] splitString(String subject) {
		Pattern regex = Pattern.compile("\\([^)]*\\)|(\\s* \\s*)");
		Matcher m = regex.matcher(subject);
		StringBuffer b= new StringBuffer();
		while (m.find()) {
		if(m.group(1) != null) m.appendReplacement(b, "SplitHere");
		else m.appendReplacement(b, m.group(0));
		}
		m.appendTail(b);
		String replaced = b.toString();
		String[] splits = replaced.split("SplitHere");
		return splits;
	}


}
