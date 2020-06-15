import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;



class Testes {
	
	Ponto4 p = new Ponto4();
	File f = null;
	ArrayList<String> tag1 = new ArrayList<String>();

	@Test
	public void test_contentDocument() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		p.contentDocument();
	}
		
	
	@Test
	public void test_getTag1() {
		p.getTag1();
		Assert.assertEquals(tag1, p.getTag1());
	}
	
	
	@Test
	public void test_HTMLBuilder() {
		p.HTMLBuilder();
	}
	
	
//	@Test
//	public void test_WriteToFile() {
//		p.WriteToFile(f., f.getName());;
//	}
//	
	
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}

}
