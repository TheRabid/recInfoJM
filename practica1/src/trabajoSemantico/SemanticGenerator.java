package trabajoSemantico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class SemanticGenerator {

	public static Model model;
	public static final String DOMAIN_PATH = "http://www.recInfo.com/";
	
	public static Resource person;
	public static Resource document;
	public static Property name;
	public static Property lastName;
	public static Property type;

	public static Property title;
	public static Property autor;
	public static Property description;
	public static Property publisher;
	public static Property date;
	public static Property language;

	
	public static void main(String[] args) {
		
		String pathZaguan = "./recordsdc";
		File[] listFiles = new File(pathZaguan).listFiles();
		
		
        model = ModelFactory.createDefaultModel();
		model.setNsPrefix("recinfo", DOMAIN_PATH);
		person = model.createResource(DOMAIN_PATH + "Persona");
        document = model.createResource(DOMAIN_PATH + "Document");
        type = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

        name = model.createProperty(DOMAIN_PATH + "Nombre");
        lastName = model.createProperty(DOMAIN_PATH + "Apellidos");
        
        title = model.createProperty(DOMAIN_PATH + "Titulo");
        autor = model.createProperty(DOMAIN_PATH + "Autor");
        description = model.createProperty(DOMAIN_PATH + "Descripcion");
        publisher = model.createProperty(DOMAIN_PATH + "Publicacion");
        date = model.createProperty(DOMAIN_PATH + "Fecha");
        language = model.createProperty(DOMAIN_PATH + "Idioma");

        
        System.out.println("Leyendo files");
		for (File f:listFiles) {
			addDocument(f);
		}
	
		
        model.write(System.out); 
		
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
				if (doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ").length == 1) {
					Resource albmos = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""))
							.addProperty(name,  doc.getElementsByTagName("dc:creator").item(0).getTextContent())
							.addProperty(type, person);
					
					newDocument.addProperty(autor, DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""));
				}
				else if (doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ").length == 2) {
					Resource albmos = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""))
							.addProperty(name,  doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ")[1])
							.addProperty(lastName,  doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ")[0])
							.addProperty(type, person);
					
					newDocument.addProperty(autor, DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""));
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
	
}
