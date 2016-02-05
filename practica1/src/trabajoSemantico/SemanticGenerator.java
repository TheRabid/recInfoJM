package trabajoSemantico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class SemanticGenerator {

	public static Model model;
	public static final String DOMAIN_PATH = "http://www.recInfo.com/";
	public static final String SKOS_PATH = "http://www.w3.org/TR/skos-primer/";
	
	public static Resource person;
	public static Resource document;
	public static Resource concept;
	public static Property name;
	public static Property lastName;
	public static Property type;

	public static Property title;
	public static Property autor;
	public static Property description;
	public static Property publisher;
	public static Property date;
	public static Property language;
	public static Property narrower;

	
	private static ArrayList<String> temas;

	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		temas = new ArrayList<String>();

		String pathZaguan = "./recordsdc";
		File[] listFiles = new File(pathZaguan).listFiles();
		
				
        model = ModelFactory.createDefaultModel();
		model.setNsPrefix("recinfo", DOMAIN_PATH);
		model.setNsPrefix("skos", SKOS_PATH);
		person = model.createResource(DOMAIN_PATH + "Persona");
        document = model.createResource(DOMAIN_PATH + "Document");
        concept = model.createResource(DOMAIN_PATH + "Concept");
        type = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

        name = model.createProperty(DOMAIN_PATH + "Nombre");
        lastName = model.createProperty(DOMAIN_PATH + "Apellidos");
        
        title = model.createProperty(DOMAIN_PATH + "Titulo");
        autor = model.createProperty(DOMAIN_PATH + "Autor");
        description = model.createProperty(DOMAIN_PATH + "Descripcion");
        publisher = model.createProperty(DOMAIN_PATH + "Publicacion");
        date = model.createProperty(DOMAIN_PATH + "Fecha");
        language = model.createProperty(DOMAIN_PATH + "Idioma");
        narrower = model.createProperty(SKOS_PATH + "narrower");

        
        System.out.println("Leyendo files");
		for (File f:listFiles) {
			addDocument(f);
		}

		generateTesauro("tesauro.txt");
		
//		model.write(System.out); 
		model.write(new PrintWriter("Modelo.rdf", "UTF-8"));
		
        
        for (String s:temas) {
        	System.out.println(s);
        }
	}
	
	
	public static void addDocument(File f){

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		
		
			InputSource is = new InputSource(new FileInputStream(f));
			org.w3c.dom.Document doc = builder.parse(is);
			
			
			Resource newDocument = null;

			// Identifier
			if (doc.getElementsByTagName("dc:identifier").item(0) != null) {
				newDocument = model.createResource(doc.getElementsByTagName("dc:identifier").item(0).getTextContent())
						.addProperty(type, document);
			}	
			
			// Title
			if (doc.getElementsByTagName("dc:title").item(0) != null) {
				newDocument.addProperty(title,  doc.getElementsByTagName("dc:title").item(0).getTextContent());
			}
	
			
			// Creator
			if (doc.getElementsByTagName("dc:creator").item(0) != null) {
				
				NodeList nodes = doc.getElementsByTagName("dc:creator");
//				if (nodes.getLength()>1) System.out.println("JAIME PARGUELA");
				
				for (int i=0; i<nodes.getLength(); i++) {
					if (doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ").length == 1) {
						Resource albmos = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(i).getTextContent().replace(" ", ""))
								.addProperty(name,  doc.getElementsByTagName("dc:creator").item(i).getTextContent())
								.addProperty(type, person);
						
						newDocument.addProperty(autor, DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(i).getTextContent().replace(" ", ""));
					}
					else if (doc.getElementsByTagName("dc:creator").item(i).getTextContent().split(", ").length == 2) {
						Resource albmos = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(i).getTextContent().replace(" ", ""))
								.addProperty(name,  doc.getElementsByTagName("dc:creator").item(i).getTextContent().split(", ")[1])
								.addProperty(lastName,  doc.getElementsByTagName("dc:creator").item(i).getTextContent().split(", ")[0])
								.addProperty(type, person);
						
						newDocument.addProperty(autor, DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""));
					}
				}
			}
	
			// Subject ignored
			
			// Description
			if (doc.getElementsByTagName("dc:description").item(0) != null) {
				newDocument.addProperty(description,  doc.getElementsByTagName("dc:description").item(0).getTextContent());
			}
	
			// Publisher
			if (doc.getElementsByTagName("dc:publisher").item(0) != null) {
				newDocument.addProperty(publisher,  doc.getElementsByTagName("dc:publisher").item(0).getTextContent());
			}
	
			// Contributor ignored
	
			// Date
			if (doc.getElementsByTagName("dc:date").item(0) != null) {
				newDocument.addProperty(date,  doc.getElementsByTagName("dc:date").item(0).getTextContent());
			}
	
			// Type ignored
	
			// Format ignored
	
			// Source ignored
	
			// Language
			if (doc.getElementsByTagName("dc:language").item(0) != null) {
				newDocument.addProperty(language,  doc.getElementsByTagName("dc:language").item(0).getTextContent());
			}
	
			// Relation ignored
			
			// Coverage ignored
	
			// Rights ignored
	
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println(f.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private static void generateTesauro(String tesauroPath) {
		try {
			Scanner s = new Scanner(new File(tesauroPath));
			Resource used = null;
			while (s.hasNextLine()) {

				String[] line = s.nextLine().split("-");

				if (line.length == 1) {		// Tema
					temas.add(line[0]);
					used = generateConcept(line[0]);
				}
				else if (line.length == 2) {		//SubTema
					temas.add(line[1]);
					generateSubconcept(used, line[1]);
				}

			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	private static Resource generateConcept(String tema){
		return model.createResource(DOMAIN_PATH + tema).addProperty(type, concept);
	}
	
	private static void generateSubconcept(Resource original, String tema){
		model.createResource(DOMAIN_PATH + tema).addProperty(type, concept).addProperty(narrower, original);
	}
	
}
