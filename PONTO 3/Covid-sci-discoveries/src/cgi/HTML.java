package cgi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.metadata.model.DocumentDate;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.StringUtils;

public class HTML {

	private static ArrayList<String> aux = new ArrayList<>();
	private static ArrayList<String> authorsList;
	private static File directory;

	public static void main(String[] args) throws  IOException, AnalysisException, InvalidRemoteException, TransportException, GitAPIException {
		authorsList = new ArrayList<String>();
		System.out.println(Header());
		cloneGit();
		System.out.println(body());
		extractMetadata();
		System.out.println(table());
		System.out.println(HtmlBot());
	}

	/**
	 * Metodo para clonar o repositório GIT que 
	 * contém os documentos PDF a extrair Metadata.
	*/
	public static void cloneGit() {
		try {
			String URI = "https://github.com/mpsci-iscteiul/Covid-Scientific-Discoveries-Repository.git";
			directory = new File("./clonegit");

			deleteDir(directory);

			Git git;
			git = Git.cloneRepository()
					.setURI(URI)
					.setDirectory(directory)
					.call();
			Repository repository = git.getRepository();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Função que apaga o diretório clonado no eclipse.
	*/
	public static boolean deleteDir(File dir) {
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
	 * Método para extrair Metadata
	 * dos documentos PDF, através da ferramenta CERMINE.
	*/
	public static void extractMetadata() {
		int iteradorAutor=0;
		if (directory.isDirectory()) {
			for (File f : directory.listFiles()) {
				if (f.isFile() && f.getName().endsWith(".pdf")) {
					try {
						ContentExtractor extractor = new ContentExtractor();
						InputStream inputStream = new FileInputStream(directory+ "/"+f.getName());
						extractor.setPDF(inputStream);
						DocumentMetadata dm = extractor.getMetadata();
						String articleTitle = dm.getTitle();
						String journalName = dm.getJournal();
						DateType datetype=DateType.PUBLISHED;
						String publicationYear =dm.getDate(datetype).getYear();
						List<DocumentAuthor> authorDMList = dm.getAuthors();

						System.out.println("<tr>");
						System.out.println("<td> <a href=\"https://github.com/mpsci-iscteiul/Covid-Scientific-Discoveries-Repository/blob/master/" + f.getName() + "\"" + ">" + articleTitle + "</a>" + "</td>" );
					//	System.out.println("<td> <a href=\"" + "C:\\Users\Miguel\Documents\Informática e Gestão de Empresas\3ºano\2º semestre\ES II\14\ESII-2020-2021-CGI\clonegit\biology-09-00094.pdf" 
							//	+ "\"" + ">" + articleTitle + "</a>" + "</td>" );
						System.out.println("<td>" + journalName + "</td>" );
						System.out.println("<td>" + publicationYear + "</td>" );

						for ( int i=0; i!=authorDMList.size(); i++) {
							authorsList.add(authorDMList.get(i).getName());	
						}

						String authors = StringUtils.join(authorsList, ", ");
						authorsList.clear();
						aux.add(authors);

						System.out.println("<td>" + aux.get(iteradorAutor) +  "</td>" );
						iteradorAutor++;
						System.out.println("</tr>");
					} catch (IOException | AnalysisException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Função que devolve uma String que contém
	 * parte do código HTML a gerar.
	*/
	public static String body() {
		return "<html>\r\n" +"<head>\r\n" + 
				"<style>\r\n" + 
				"#customers {\r\n" + 
				"  font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\r\n" + 
				"  border-collapse: collapse;\r\n" + 
				"  width: 100%;\r\n" + 
				"}\r\n" + 
				"h1 {text-align: center;\r\n" + 
				"color: black;\r\n" + 
				"font-family:arial;\r\n" + 
				"}\r\n" + 
				"#customers td, #customers th {\r\n" + 
				"  border: 1px solid #ddd;\r\n" + 
				"  padding: 8px;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"#customers tr:nth-child(even){background-color: #0000;}\r\n" + 
				"\r\n" + 
				"#customers tr:hover {background-color: #D5CBBC;}\r\n" + 
				"\r\n" + 
				"#customers th {\r\n" + 
				"  padding-top: 12px;\r\n" + 
				"  padding-bottom: 12px;\r\n" + 
				"  text-align: left;\r\n" + 
				"  background-color: #55883E;\r\n" + 
				"  color: black;\r\n" + 
				"}</style>"+
				"<title> Covid 19 Artigos </title> \r\n" + "<body>\r\n" + 
				"<h1>Artigos Covid19</h1>\r\n" +"<table id=\"customers\">\r\n" + 
				"  <tr> \r\n" + 
				"    <th>Article title</th>\r\n" + 
				"    <th> Journal name</th>\r\n" + 
				"    <th>Publication year</th>\r\n" + 
				"    <th> Authors</th>\r\n" + 
				"</tr>"  ;
	}
	
	/**
	 * Função que devolve uma String que contém
	 * parte do código HTML a gerar.
	*/
	public static String table() {
		return "</table>";
	}

	/**
	 * Função que devolve uma String que contém
	 * parte do código HTML a gerar.
	*/
	public static String Header(){
		return "Content-type: text/html\n\n";
	}

	/**
	 * Função que devolve uma String que contém
	 * parte do código HTML a gerar.
	*/
	public static String HtmlBot(){
		return "</body>\n</html>\n";
	}

	/**
	 * Função que devolve a lista auxiliar.
	*/
	public static ArrayList<String> getAux() {
		return aux;
	}

	/**
	 * Função que devolve a lista de autores.
	*/
	public static ArrayList<String> getAuthorsList() {
		return authorsList;
	}

	/**
	 * Função que devolve a diretoria.
	*/
	public static File getDiretoria() {
		return directory;
	}


}
