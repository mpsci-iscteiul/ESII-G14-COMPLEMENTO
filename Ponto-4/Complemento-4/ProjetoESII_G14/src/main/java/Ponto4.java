
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.text.html.HTML;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;


public class Ponto4 {
	
	public int i = 0;
	public ArrayList<String> tag1 = new ArrayList<String>();
	public ArrayList<String> tag2 = new ArrayList<String>();
	public ArrayList<String> tag3 = new ArrayList<String>();
	
	public static void main( String[] args ) throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		Ponto4 p = new Ponto4();
		p.contentDocument();
		p.HTMLBuilder();		
	}

	
	public void contentDocument() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		//Criar clone caso não exista ou aceder ao clone
				Git git;
				if(new File("/cloneES").exists()) {
					git =Git.open(new File("/cloneES"));
					git.pull();
					System.out.println("Clone já existe");
				}

				else {
					git = Git.cloneRepository()
							.setURI("https://github.com/vbasto-iscte/ESII1920.git")
							.setDirectory(new File("/cloneES"))
							.setCloneAllBranches(true)
							.call();
					System.out.println("Clone criado");
				}

				Repository repository = git.getRepository();

				Map<String, Ref> tags = repository.getTags();

				//Percorre a lista de referências para obter todas as tags
				List<Ref> listRef = new ArrayList<Ref>();
				Iterator it = tags.entrySet().iterator();
				System.out.println("Lista de Tags:");
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					listRef.add((Ref) pair.getValue());
					System.out.println(pair.getKey());
				}
				System.out.println("");
				
				for (Ref ref : listRef) {
					i = i + 1;
					System.out.println("Tag " + i + " : " + ref.getName().replace("refs/tags/", ""));

					try (RevWalk revWalk = new RevWalk(repository)) {

						RevCommit commit = revWalk.parseCommit(ref.getObjectId());
						System.out.println("Descrição da Tag: " + commit.getFullMessage());
						
						RevTree tree = commit.getTree();
						PersonIdent author = commit.getAuthorIdent();
						Date data = author.getWhen();
						System.out.println("Data: " + data);

						try(TreeWalk treeWalk = new TreeWalk(repository)){

							treeWalk.addTree(tree);
							treeWalk.setRecursive(true);
							treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));

							if (!treeWalk.next()) {
								throw new IllegalStateException("Não existe");
							}
							System.out.println("Nome do documento: " + treeWalk.getPathString());
							ObjectId objectId = treeWalk.getObjectId(0);
							ObjectLoader loader = repository.open(objectId);
							System.out.println("");
							
							if(i == 1) {
								tag1.add(ref.getName().replace("refs/tags/", "")); //Nome da tag
								tag1.add(commit.getFullMessage()); //Descrição da tag
								tag1.add(data.toString()); //Data da tag
								tag1.add(treeWalk.getPathString()); //Nome do documento
								tag1.add("http://visualdataweb.de/webvowl/#iri=https://github.com/vbasto-iscte/ESII1920/raw/" + ref.getName().replace("refs/tags/", "") + "/covid19spreading.rdf");
							}
							System.out.println(tag1);
							
							if(i == 2) {
								tag2.add(ref.getName().replace("refs/tags/", "")); //Nome da tag
								tag2.add(commit.getFullMessage()); //Descrição da tag
								tag2.add(data.toString()); //Data da tag
								tag2.add(treeWalk.getPathString()); //Nome do documento
								tag2.add("http://visualdataweb.de/webvowl/#iri=https://github.com/vbasto-iscte/ESII1920/raw/" + ref.getName().replace("refs/tags/", "") + "/covid19spreading.rdf");
							}
							System.out.println(tag2);
							
							if(i == 3) {
								tag3.add(ref.getName().replace("refs/tags/", "")); //Nome da tag
								tag3.add(commit.getFullMessage()); //Descrição da tag
								tag3.add(data.toString()); //Data da tag
								tag3.add(treeWalk.getPathString()); //Nome do documento
								tag3.add("http://visualdataweb.de/webvowl/#iri=https://github.com/vbasto-iscte/ESII1920/raw/" + ref.getName().replace("refs/tags/", "") + "/covid19spreading.rdf");
							}
							System.out.println(tag3);
							
						}
						revWalk.dispose();
						
					}
				}
	}


	
	public void HTMLBuilder() {
		
		try {

			StringBuilder htmlStringBuilder=new StringBuilder();

			//Titulo da página
			htmlStringBuilder.append("<html><head><title>Covid-19 - Ponto 4 </title></head>");

			htmlStringBuilder.append("<body>");
			htmlStringBuilder.append("<table border=\"1\" bordercolor=\"#000000\">");

			//Colunas
			htmlStringBuilder.append("<tr><td><b>Nome da Tag</b></td><td><b>Descrição</b></td><td><b>Timestamp</b></td></td><td><b>Nome do Ficheiro</b></td></td><td><b>Link de Vizualização</b></td></tr>");
			
			//Primeira Tag
			htmlStringBuilder.append("<tr><td>"+ tag1.get(0) + "</td><td>" + tag1.get(1) + "</td><td>" + tag1.get(2) + "</td><td>" + tag1.get(3) + "</td><td>" + tag1.get(4) + "</td></tr>");
			
			//Segunda Tag
			htmlStringBuilder.append("<tr><td>"+ tag2.get(0) + "</td><td>" + tag2.get(1) + "</td><td>" + tag2.get(2) + "</td><td>" + tag2.get(3) + "</td><td>" + tag2.get(4) + "</td></tr>");
			
			//Terceira Tag
			htmlStringBuilder.append("<tr><td>"+ tag3.get(0) + "</td><td>" + tag3.get(1) + "</td><td>" + tag3.get(2) + "</td><td>" + tag3.get(3) + "</td><td>" + tag3.get(4) + "</td></tr>");
			
			htmlStringBuilder.append("</table></body></html>");
			
			//Nome do ficheiro HTML
			WriteToFile(htmlStringBuilder.toString(),"Grupo14_Ponto4.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void WriteToFile(String fileContent, String fileName) throws IOException {
		String projectPath = System.getProperty("user.dir");
		String tempFile = projectPath + File.separator+fileName;
		File file = new File(tempFile);
		
		if (file.exists()) {
			try {
				File newFileName = new File(projectPath + File.separator+ "copia_"+fileName);
				file.renameTo(newFileName);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
		Writer writer=new OutputStreamWriter(outputStream);
		writer.write(fileContent);
		writer.close();

	}

	public ArrayList<String> getTag1() {
		return tag1;
	}


	public ArrayList<String> getTag2() {
		return tag2;
	}


	public ArrayList<String> getTag3() {
		return tag3;
	}
	
	

}

