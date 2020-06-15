package appJava;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.text.DefaultHighlighter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.StringsComparator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class Projeto.
 */
public class Projeto {

	/** The rdf files. */
	List<String> rdfFiles = new ArrayList<>();
	
	/** The ficheiros. */
	private List<Covid19SpreadingFile> ficheiros = new ArrayList<>(); 
	
	/** The s 0. */
	private String s0;
	
	/** The s 1. */
	private String s1; 
	
	/** The s 2. */
	private static String s2;	
	
	/** The s 3. */
	private static String s3;


	/**
	 * O metodo CreateFileList tem como objetivo ir buscar os ficheiros ao github
	 * e adiciona -los a uma lista de covidSpreadings para depois poder trabalhar com ela na seguinte funcao
	 *
	 * @throws InvalidRemoteException the invalid remote exception
	 * @throws TransportException the transport exception
	 * @throws GitAPIException the git API exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void CreateFileList() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		String URI = "https://github.com/vbasto-iscte/ESII1920";
		File directory = new File("directory");

		deleteDir(directory);
		

		Git git = Git.cloneRepository()
				.setURI(URI)
				.setDirectory(directory)
				.call();

		Repository repository = git.getRepository();

		List<Ref> listTags = git.tagList().call();

		for (Ref ref : listTags) { 
			ObjectId tagFile = repository.resolve(ref.getName());

			try (RevWalk revWalk = new RevWalk(repository)) {

				RevCommit commit = revWalk.parseCommit(tagFile);
				RevTree tree = commit.getTree();

				try (TreeWalk treeWalk = new TreeWalk(repository)) {
					treeWalk.addTree(tree);
					treeWalk.setRecursive(true);
					treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));

					if (!treeWalk.next()) {
						throw new IllegalStateException("Did not find expected file 'covid19spreading.rdf'");

					}

					ObjectId objectId = treeWalk.getObjectId(0);
					ObjectLoader loader = repository.open(objectId);

					ficheiros.add(new Covid19SpreadingFile(commit.getAuthorIdent().getWhen(), loader));

				}

			} catch (IOException e) {
				e.printStackTrace();

			}
		}

		compareDates(ficheiros);
		
	}




	/**
	 * Compara as datas para obter os utlimos dois ficheiros que serao colocados 
	 * na minha lista auxiliar lista final de onde depois irao ser extraidos os conteudos de ambos
	 * e futuramente comprados na funcao MycommandVisitor
	 *
	 * @param ficheiros the ficheiros
	 * @return the list
	 * @throws MissingObjectException the missing object exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Covid19SpreadingFile> compareDates(List<Covid19SpreadingFile> ficheiros) throws MissingObjectException, IOException {
		List<Covid19SpreadingFile> listaFinal = new ArrayList<>();
		Covid19SpreadingFile covid2 = ficheiros.get(0);
		Integer index = 0;

		for (Covid19SpreadingFile c : ficheiros) {
			if (c.getTm().after(covid2.getTm())) {
				covid2 = c;
				index = ficheiros.indexOf(c);
			}
		}

		ficheiros.remove(ficheiros.get(index));
		Covid19SpreadingFile covid3 = ficheiros.get(1);

		for (Covid19SpreadingFile c : ficheiros) {
			if (c.getTm().after(covid3.getTm())) {
				covid3 = c;
			}
		}
		listaFinal.add(covid2);
		listaFinal.add(covid3);

		/*saco os ObjectLoaders para strings e fica td organizado 
		e dps chamo a funcao create file com essas strings*/
		s0 = new String(listaFinal.get(0).getOl().getBytes()).trim();
		s1 = new String(listaFinal.get(1).getOl().getBytes()).trim();

		StringsComparator comparator = new StringsComparator(s0, s1);
		MyCommandsVisitor myCommandsVisitor = new MyCommandsVisitor();
		comparator.getScript().visit(myCommandsVisitor);

		s2=	new String ( myCommandsVisitor.left );
		s3=	new String ( myCommandsVisitor.right);


		/*Nao é fundamental no projeto mas deu me jeito no inicio para ver se conseguia ir buscar
		se conseguia ir buscar todos os conteudos dos ficheiros e por em novos ficheiros .txt*/
		//createFiles (s0, s1,s2,s3);

		return listaFinal;

	}



	/**
	 * Delete dir.
	 *
	 * @param dir the dir
	 * @return true, if successful
	 */
	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		
		return dir.delete();
	}


	/**
	 * metodo que usei para verificar que tinha conseguido o conteudo dos ficheiros (nao e fundamental 
	para o objetivo do projeto visto nao os usar para nada
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @param d the d
	 */
	public void createFiles (String a, String b, String c, String d) {

		FileWriter fw0 = null;
		BufferedWriter bw0 = null;	
		PrintWriter out0= null;

		FileWriter fw1 = null;
		BufferedWriter bw1 = null;	
		PrintWriter out1= null;

		FileWriter fw2 = null;
		BufferedWriter bw2 = null;	
		PrintWriter out2= null;

		FileWriter fw3 = null;
		BufferedWriter bw3 = null;	
		PrintWriter out3= null;

		try {
			fw0 = new FileWriter("commit0.txt", false);
			bw0 = new BufferedWriter(fw0);
			out0 = new PrintWriter(bw0);  
			out0.println(a);
			out0.close();

			fw1 = new FileWriter("commit1.txt", false);
			bw1 = new BufferedWriter(fw1);
			out1 = new PrintWriter(bw1);  
			out1.println(b);
			out1.close();

			fw2 = new FileWriter("commit2.txt", false);
			bw2 = new BufferedWriter(fw2);
			out2 = new PrintWriter(bw2);  
			out2.println(c);
			out2.close();

			fw3 = new FileWriter("commit3.txt", false);
			bw3 = new BufferedWriter(fw3);
			out3 = new PrintWriter(bw3);  
			out3.println(d);
			out3.close();



		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}

	}



	

	/**
	 * Class que criei para ir Verificar as diferencas e assinala-las colocando os valores diferentes
	entre chavetas ou parenteses
	 */
	class MyCommandsVisitor implements CommandVisitor<Character> {

		/** The left. */
		String left = "";
		
		/** The right. */
		String right = "";

		/**
		 * Visit keep command.
		 *
		 * @param c the c
		 */
		@Override
		public void visitKeepCommand(Character c) {
			left = left + c;
			right = right + c;
		}



		/**
		 * Diferencas presentes no penultimo ficheiro estarao delimitadas/sinalizadas
			 atraves de chavetas ---> ex: texto_normal (valor diferente) texto_normal 
		 *
		 * @param c the c
		 */
		@Override
		public void visitInsertCommand(Character c) {

			
//	--		 right = right.replace((c+""),"</xmp><font color='#c5c5c5'>"+ (c+"") +"</font><xmp>");
			right = right + "(" + c + ")";


		}

		/**
		 * Diferencas presentes no penultimo ficheiro estarao delimitadas/sinalizadas
			 atraves de chavetas ---> ex: texto_normal {valor diferente} texto_normal
		 *
		 * @param c the c
		 */
		@Override
		public void visitDeleteCommand(Character c) {

			

			left = left + "{" + c + "}";
		}


	}




	/**
	 * Gets the ficheiros.
	 *
	 * @return the ficheiros
	 */
	public List<Covid19SpreadingFile> getFicheiros() {
		return ficheiros;
	}




	/**
	 * Sets the ficheiros.
	 *
	 * @param ficheiros the new ficheiros
	 */
	public void setFicheiros(List<Covid19SpreadingFile> ficheiros) {
		this.ficheiros = ficheiros;
	}


	/**
	 * Gets the s2.
	 *
	 * @return the s2
	 */
	public static String getS2() {
		return s2;
	}




	/**
	 * Sets the s2.
	 *
	 * @param s2 the new s2
	 */
	public static void setS2(String s2) {
		Projeto.s2 = s2;
	}




	/**
	 * Gets the s3.
	 *
	 * @return the s3
	 */
	public static String getS3() {
		return s3;
	}




	/**
	 * Sets the s3.
	 *
	 * @param s3 the new s3
	 */
	public static void setS3(String s3) {
		Projeto.s3 = s3;
	}




	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		System.out.println("Content-type: text/html\n\n");
		Projeto p = new Projeto();

		try {

			p.CreateFileList();


		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
				"<style>\r\n" + 
				"body {\r\n" + 
				"	font-family: Arial;\r\n" + 
				"	color: black;\r\n" + 
				"}\r\n" + 
				" \r\n" + 
				".split {\r\n" + 
				"	height: 100%;\r\n" + 
				"	width: 50%;\r\n" + 
				"	position: fixed;\r\n" + 
				"	z-index: 1;\r\n" + 
				"	top: 0;\r\n" + 
				"	overflow-x: hidden;\r\n" + 
				"	padding-top: 20px;\r\n" + 
				"}\r\n" + 
				" \r\n" + 
				".left {\r\n" + 
				"	left: 0;\r\n" + 
				"	background-color: #D7FBF6;\r\n" + 
				"}\r\n" + 
				" \r\n" + 
				".right {\r\n" + 
				"	right: 0;\r\n" + 
				"	background-color: #FCFCC3;\r\n" + 
				"}\r\n" + 
				"</style>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"	<div class=\"split left\">\r\n" + 
				"		<div class=\"centered\">\r\n" + 
				"			<p>${ULTIMA VERSAO - diferencas disposto dentro d () }</p>\r\n\n" + 
				"<xmp>" + s3+ "</xmp>" +
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				" \r\n" + 
				"	<div class=\"split right\">\r\n" + 
				"		<div class=\"centered\">\r\n" + 
				"			<p>${PENULTIMA VERSAO - diferencas disposto dentro d {} }</p>\r\n\n" +
				"<xmp>" + s2 + "</xmp>" +
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"</body>\r\n" + 
				"</html>");

	}


}
