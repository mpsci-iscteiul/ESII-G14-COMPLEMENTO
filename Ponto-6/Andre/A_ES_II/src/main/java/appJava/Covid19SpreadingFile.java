package appJava;

import java.security.Timestamp;
import java.util.Date;

import org.eclipse.jgit.lib.ObjectLoader;

public class Covid19SpreadingFile {

	private Date tm;
	private ObjectLoader ol;

	public Covid19SpreadingFile(Date tm, ObjectLoader ol) {
		this.ol = ol;
		this.tm = tm;
	}
	
	

	public Date getTm() {
		return tm;
	}



	public void setTm(Date tm) {
		this.tm = tm;
	}



	public ObjectLoader getOl() {
		return ol;
	}



	public void setOl(ObjectLoader ol) {
		this.ol = ol;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Covid File: Timestamp: " + this.tm + "; Texto: " + this.ol);

		return sb.toString();
	}
}
