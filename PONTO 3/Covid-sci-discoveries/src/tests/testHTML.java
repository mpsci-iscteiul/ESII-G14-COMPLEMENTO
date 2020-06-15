package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cgi.HTML;

class testHTML {


	ArrayList list = new ArrayList<>();
	ArrayList list2 = new ArrayList<>();
	File dir = null;
	String s1 ="ola";
	String s2 ="ola";
	String s3 ="ola";
	String s4 ="ola";
	
	@Test
	public void testMain() {
		HTML html = new HTML();
	}

	@Test
	public void testCloneGit() {
		HTML html2 = new HTML();
		html2.cloneGit();
	}

	@Test
	public void testDeleteDir() {
		HTML html3 = new HTML();
		HTML.deleteDir(dir);
	}

	@Test
	public void testExtractMetadata() {
		HTML html4 = new HTML();
		HTML.extractMetadata();
	}

	@Test
	public void testGetAux() {
		HTML html5 = new HTML();
		assertEquals(list,html5.getAux());
	}
	
	@Test
	public void testGetAuthorsList() {
		HTML html6 = new HTML();
		assertEquals(list2,html6.getAuthorsList());
	}

	@Test
	public void testGetDiretoria() {
		HTML html7 = new HTML();
		assertEquals(dir,html7.getDiretoria());
	}
	
	@Test
	public void testTable() {
		HTML html8 = new HTML();
		assertEquals(s1,html8.table());
	}
	
	@Test
	public void testHeader() {
		HTML html9 = new HTML();
		assertEquals(s2,html9.Header());
	}
	
	@Test
	public void testBot() {
		HTML html9 = new HTML();
		assertEquals(s3,html9.HtmlBot());
	}
	
	@Test
	public void testBody() {
		HTML html10 = new HTML();
		assertEquals(s4,html10.body());
	}

}
