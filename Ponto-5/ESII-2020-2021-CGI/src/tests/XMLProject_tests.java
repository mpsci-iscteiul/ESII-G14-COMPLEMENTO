package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;

import javax.xml.xpath.XPathExpressionException;

import org.junit.jupiter.api.Test;

import cgi.XmlProject;

class XMLProject_tests {

	@Test
	void test() throws XPathExpressionException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		XmlProject xmlProject = new XmlProject("gitdata/covid19spreading.rdf");
		String output = xmlProject.getQuery("sum(Testes(all))");
		assertEquals("100", output);
		
		String output2 = xmlProject.getQuery("Regiao(Testes > 40 and Internamentos < 60 or Infecoes =50)");
		assertEquals("[Alentejo, Algarve]",output2);
		
		String output3 = xmlProject.getQuery("50");
		assertEquals("50",output3);
		
		String output4 = xmlProject.getQuery("");
		assertEquals("Erro-> Verifique que as funcoes e condicoes comecam com letra maiuscula",output4);
		
		String output5 = xmlProject.getQuery("Regiao(all)");
		assertEquals("[Alentejo, Algarve, Centro, Lisboa, Norte]", output5);
		
		String output6 = xmlProject.getQuery("Testes(Algarve)");
		assertEquals("50",output6);


	}

	
	
}
