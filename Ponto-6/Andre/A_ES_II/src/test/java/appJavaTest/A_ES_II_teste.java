package appJavaTest;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Test;

import appJava.Projeto;

public class A_ES_II_teste {

	@Test

	public void test1 () throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		Projeto t1 = new Projeto();
		t1.CreateFileList();
		String string1 = t1.getS2();
		assertNotEquals("teste1", string1);
		

	}

	@Test

	public void test2() {
		Projeto t2 = new Projeto();
		t2.setS2("ola");
		assertEquals("ola", t2.getS2());

	}



}
