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
import com.hp.hpl.jena.vocabulary.VCARD;

public class SemanticGenerator {

	public static Model model;
	public static final String DOMAIN_PATH = "http://www.recInfo.com/";
	
	public static Resource person;
	public static Resource document;
	public static Property name;
	public static Property lastName;
	public static Property type;

	public static Property title;
	public static Property subject;
	public static Property description;

	
	public static void main(String[] args) {
		
		String pathZaguan = "./recordsdc";
		File[] listFiles = new File(pathZaguan).listFiles();
		
		
        model = ModelFactory.createDefaultModel();
		model.setNsPrefix("recinfo", DOMAIN_PATH);
		person = model.createResource(DOMAIN_PATH + "Persona");
        document = model.createResource(DOMAIN_PATH + "Document");
        name = model.createProperty(DOMAIN_PATH + "Nombre");
        lastName = model.createProperty(DOMAIN_PATH + "Apellidos");
        type = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        
        title = model.createProperty(DOMAIN_PATH + "Title");
        subject = model.createProperty(DOMAIN_PATH + "Asunto");
        description = model.createProperty(DOMAIN_PATH + "Descripcion");

        
        System.out.println("Leyendo files");
		for (File f:listFiles) {
//			System.out.println("New Faile");
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
//				doc1.add(new StringField("identifier", doc.getElementsByTagName("dc:identifier").item(0).getTextContent(),
//						Field.Store.YES));
				
				newDocument = model.createResource(doc.getElementsByTagName("dc:identifier").item(0).getTextContent());
			}
			
			
			
			
			// Title
			if (doc.getElementsByTagName("dc:title").item(0) != null) {
	//			doc1.add(new TextField("title", doc.getElementsByTagName("dc:title").item(0).getTextContent(),
	//					Field.Store.YES));
				newDocument.addProperty(title,  doc.getElementsByTagName("dc:title").item(0).getTextContent());
				
			}
	
			
			// Creator
			if (doc.getElementsByTagName("dc:creator").item(0) != null) {
//				doc1.add(new TextField("creator", doc.getElementsByTagName("dc:creator").item(0).getTextContent(),
//						Field.Store.YES));
				
//				System.out.println(doc.getElementsByTagName("dc:creator").item(0).getTextContent());
					
				if (doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ").length == 1) {
					Resource albmos = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""))
							.addProperty(name,  doc.getElementsByTagName("dc:creator").item(0).getTextContent())
							.addProperty(type, person);
				}
				else if (doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ").length == 2) {
					Resource albmos = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:creator").item(0).getTextContent().replace(" ", ""))
							.addProperty(name,  doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ")[1])
							.addProperty(lastName,  doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ")[0])
							.addProperty(type, person);
				}
			

			}
	
			// Subject
			if (doc.getElementsByTagName("dc:subject").item(0) != null) {
//				doc1.add(new TextField("subject", doc.getElementsByTagName("dc:subject").item(0).getTextContent(),
//						Field.Store.YES));
				newDocument.addProperty(subject,  doc.getElementsByTagName("dc:subject").item(0).getTextContent());
			}
	
			// Description
			if (doc.getElementsByTagName("dc:description").item(0) != null) {
//				doc1.add(new TextField("description", doc.getElementsByTagName("dc:description").item(0).getTextContent(),
//						Field.Store.YES));
				newDocument.addProperty(description,  doc.getElementsByTagName("dc:description").item(0).getTextContent());
			}
	
			// Publisher
			if (doc.getElementsByTagName("dc:publisher").item(0) != null) {
//				doc1.add(new TextField("publisher", doc.getElementsByTagName("dc:publisher").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Contributor
			if (doc.getElementsByTagName("dc:contributor").item(0) != null) {
//				doc1.add(new TextField("contributor", doc.getElementsByTagName("dc:contributor").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Date
			if (doc.getElementsByTagName("dc:date").item(0) != null) {
//				doc1.add(new IntField("date",
//						Integer.parseInt(doc.getElementsByTagName("dc:date").item(0).getTextContent().trim()),
//						Field.Store.YES));
			}
	
			// Type
			if (doc.getElementsByTagName("dc:type").item(0) != null) {
//				doc1.add(new StringField("type", doc.getElementsByTagName("dc:type").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Format
			if (doc.getElementsByTagName("dc:format").item(0) != null) {
//				doc1.add(new StringField("format", doc.getElementsByTagName("dc:format").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
	
			// Source
			if (doc.getElementsByTagName("dc:source").item(0) != null) {
//				doc1.add(new StringField("source", doc.getElementsByTagName("dc:source").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Language
			if (doc.getElementsByTagName("dc:language").item(0) != null) {
//				doc1.add(new StringField("language", doc.getElementsByTagName("dc:language").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Relation
			if (doc.getElementsByTagName("dc:relation").item(0) != null) {
//				doc1.add(new StringField("relation", doc.getElementsByTagName("dc:relation").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Coverage
			if (doc.getElementsByTagName("dc:coverage").item(0) != null) {
//				doc1.add(new StringField("coverage", doc.getElementsByTagName("dc:coverage").item(0).getTextContent(),
//						Field.Store.YES));
			}
	
			// Rights
			if (doc.getElementsByTagName("dc:rights").item(0) != null) {
//				doc1.add(new StringField("rights", doc.getElementsByTagName("dc:rights").item(0).getTextContent(),
//						Field.Store.YES));
			}
		
	
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
