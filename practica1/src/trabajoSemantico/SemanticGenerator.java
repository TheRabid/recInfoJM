package trabajoSemantico;

import java.io.File;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SemanticGenerator {

	public static Model model;
	
	public static void main(String[] args) {
		
		String pathZaguan = "recordscd";
		File[] listFiles = new File(pathZaguan).listFiles();
		
		for (File f:listFiles) {
			addDocument(f);
		}
	
        Model model = ModelFactory.createDefaultModel();
        model.write(System.out); 
		
	}
	
	
	public static void addDocument(File f){

		
		
	}
	
	
}
