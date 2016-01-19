package trabajoSemantico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

public class SemanticGenerator {

	public static Model model;
	public static final String DOMAIN_PATH = "http://www.recInfo.com/";
	
	public static Resource person;
	public static Resource document;
	
	
	public static void main(String[] args) {
		
		String pathZaguan = "./recordscd";
		
		File documents =  new File(pathZaguan);
		File[] listFiles = documents.listFiles();
		
		if (documents.isDirectory()) System.out.println("Directorio");
		else  System.out.println("No directorio");
		if (documents.isFile()) System.out.println("File");
		else System.out.println("No File");
		if (documents.isHidden()) System.out.println("Oculto");
		else  System.out.println("no oculto");
		if (documents.canRead()) System.out.println("canRead");
		else  System.out.println("no canRead");
		
		if (listFiles == null) {
			System.out.println("NULLLL");
		}
		
        Model model = ModelFactory.createDefaultModel();
        person = model.createResource("http://www.recInfo.com/Persona");
        document = model.createResource("http://www.recInfo.com/Document");

        System.out.println("Leyendo files");
		for (File f:listFiles) {
			System.out.println("New File");
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
			
			
			// Title
			if (doc.getElementsByTagName("dc:title").item(0) != null) {
	//			doc1.add(new TextField("title", doc.getElementsByTagName("dc:title").item(0).getTextContent(),
	//					Field.Store.YES));
				
				Resource person  = model.createResource(DOMAIN_PATH + doc.getElementsByTagName("dc:title").item(0).getTextContent().replace(" ", ""))
			             .addProperty(VCARD.FN, doc.getElementsByTagName("dc:title").item(0).getTextContent());
				
			}
	
			/*
			// Creator
			if (doc.getElementsByTagName("dc:creator").item(0) != null) {
				doc1.add(new TextField("creator", doc.getElementsByTagName("dc:creator").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Subject
			if (doc.getElementsByTagName("dc:subject").item(0) != null) {
				doc1.add(new TextField("subject", doc.getElementsByTagName("dc:subject").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Description
			if (doc.getElementsByTagName("dc:description").item(0) != null) {
				doc1.add(new TextField("description", doc.getElementsByTagName("dc:description").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Publisher
			if (doc.getElementsByTagName("dc:publisher").item(0) != null) {
				doc1.add(new TextField("publisher", doc.getElementsByTagName("dc:publisher").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Contributor
			if (doc.getElementsByTagName("dc:contributor").item(0) != null) {
				doc1.add(new TextField("contributor", doc.getElementsByTagName("dc:contributor").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Date
			if (doc.getElementsByTagName("dc:date").item(0) != null) {
				doc1.add(new IntField("date",
						Integer.parseInt(doc.getElementsByTagName("dc:date").item(0).getTextContent().trim()),
						Field.Store.YES));
			}
	
			// Type
			if (doc.getElementsByTagName("dc:type").item(0) != null) {
				doc1.add(new StringField("type", doc.getElementsByTagName("dc:type").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Format
			if (doc.getElementsByTagName("dc:format").item(0) != null) {
				doc1.add(new StringField("format", doc.getElementsByTagName("dc:format").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Identifier
			if (doc.getElementsByTagName("dc:identifier").item(0) != null) {
				doc1.add(new StringField("identifier", doc.getElementsByTagName("dc:identifier").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Source
			if (doc.getElementsByTagName("dc:source").item(0) != null) {
				doc1.add(new StringField("source", doc.getElementsByTagName("dc:source").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Language
			if (doc.getElementsByTagName("dc:language").item(0) != null) {
				doc1.add(new StringField("language", doc.getElementsByTagName("dc:language").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Relation
			if (doc.getElementsByTagName("dc:relation").item(0) != null) {
				doc1.add(new StringField("relation", doc.getElementsByTagName("dc:relation").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Coverage
			if (doc.getElementsByTagName("dc:coverage").item(0) != null) {
				doc1.add(new StringField("coverage", doc.getElementsByTagName("dc:coverage").item(0).getTextContent(),
						Field.Store.YES));
			}
	
			// Rights
			if (doc.getElementsByTagName("dc:rights").item(0) != null) {
				doc1.add(new StringField("rights", doc.getElementsByTagName("dc:rights").item(0).getTextContent(),
						Field.Store.YES));
			}
		*/
	
		
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
