package trabajoSemantico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase SemanticGenerator contiene metodos para gestionar modelos
 *         rdf asi como un metodo principal para poder generar el modelo
 *         completo rdf segun las especificaciones del enunciado
 */

public class SemanticGenerator {

	/* Hashmaps para organizar los recursos */
	public static HashMap<String, Resource> persons;
	public static HashMap<String, Resource> concepts;

	/* Dominios */
	public static final String DOMAIN_PATH = "http://www.recInfo.com/";
	public static final String SKOS_PATH = "http://www.w3.org/TR/skos-primer/";

	/* Variables del modelo */
	public static Model model;
	public static Resource person;
	public static Resource document;
	public static Resource concept;
	public static Property name;
	public static Property lastName;
	public static Property type;
	public static Property identifier;
	public static Property title;
	public static Property autor;
	public static Property description;
	public static Property publisher;
	public static Property date;
	public static Property language;
	public static Property narrower;
	public static Property hasConcept;
	public static Property conceptName;
	public static Property broader;

	private static ArrayList<String> temas;

	/**
	 * Metodo main de la clase SemanticGenerator Puede ser usado mediante los
	 * parametros especificados por el enunciado.
	 * 
	 * NOTA: Para que funcione es necesario especificar los terminos segun el fichero
	 * tesauro.txt. De esta forma quedará almacenado en el fichero skos.rdf
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		String rdf = "Modelo.rdf";
		String skos = "skos.rdf";
		String docs = "./recordsdc";

		for (int i = 0; i < args.length; i += 2) {
			if (args[i].toLowerCase().equals("-rdf")) {
				rdf = args[i + 1];
			}
			if (args[i].toLowerCase().equals("-skos")) {
				skos = args[i + 1];
			}
			if (args[i].toLowerCase().equals("-docs")) {
				docs = args[i + 1];
			}
		}

		temas = new ArrayList<String>();
		File[] listFiles = new File(docs).listFiles();

		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("recinfo", DOMAIN_PATH);
		model.setNsPrefix("skos", SKOS_PATH);
		person = model.createResource(DOMAIN_PATH + "Persona");
		document = model.createResource(DOMAIN_PATH + "Document");
		concept = model.createResource(SKOS_PATH + "Concept");
		type = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

		name = model.createProperty(DOMAIN_PATH + "Nombre");
		lastName = model.createProperty(DOMAIN_PATH + "Apellidos");

		identifier = model.createProperty(DOMAIN_PATH + "Identificador");
		title = model.createProperty(DOMAIN_PATH + "Titulo");
		autor = model.createProperty(DOMAIN_PATH + "Autor");
		description = model.createProperty(DOMAIN_PATH + "Descripcion");
		publisher = model.createProperty(DOMAIN_PATH + "Publicacion");
		date = model.createProperty(DOMAIN_PATH + "Fecha");
		language = model.createProperty(DOMAIN_PATH + "Idioma");
		narrower = model.createProperty(SKOS_PATH + "narrower");
		broader = model.createProperty(SKOS_PATH + "broader");
		hasConcept = model.createProperty(DOMAIN_PATH + "hasConcept");
		conceptName = model.createProperty(DOMAIN_PATH + "conceptName");

		persons = new HashMap<String, Resource>();
		concepts = new HashMap<String, Resource>();

		generateTesauro("tesauro.txt");

		// Escribe el modelo skos
		model.write(new PrintWriter(skos, "UTF-8"));

		for (File f : listFiles) {
			addDocument(f);
		}

		model.write(new PrintWriter(rdf, "UTF-8"));

	}

	/**
	 * El metodo AddDocument es el que se encarga de parsear los documentos de
	 * la coleccion y de anadirlos al modelo rdf
	 */
	public static void addDocument(File f) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

			InputSource is = new InputSource(new FileInputStream(f));
			org.w3c.dom.Document doc = builder.parse(is);

			Resource newDocument = null;

			// Identifier
			if (doc.getElementsByTagName("dc:identifier").item(0) != null) {
				String URI = doc.getElementsByTagName("dc:identifier").item(0).getTextContent();
				String identificador = "oai_zaguan.unizar.es_" + URI.split("/")[URI.split("/").length - 1] + ".xml";
				newDocument = model.createResource(URI).addProperty(type, document).addProperty(identifier,
						identificador);
			}

			// Title
			if (doc.getElementsByTagName("dc:title").item(0) != null) {
				String temp = doc.getElementsByTagName("dc:title").item(0).getTextContent();
				newDocument.addProperty(title, temp);
				for (String a : temas) {
					// if (normalize(temp).contains(a)) {
					if (normalize(temp).matches(".*([^A-Za-z]|^)" + a + "([^A-Za-z]|$).*")) {
						newDocument.addProperty(hasConcept, concepts.get(DOMAIN_PATH + a));
					}
				}
			}

			// Creator
			if (doc.getElementsByTagName("dc:creator").item(0) != null) {

				NodeList nodes = doc.getElementsByTagName("dc:creator");

				for (int i = 0; i < nodes.getLength(); i++) {
					if (doc.getElementsByTagName("dc:creator").item(0).getTextContent().split(", ").length == 1) {

						String URI = DOMAIN_PATH
								+ doc.getElementsByTagName("dc:creator").item(i).getTextContent().replace(" ", "");
						Resource personita = model.createResource(URI)
								.addProperty(name, doc.getElementsByTagName("dc:creator").item(i).getTextContent())
								.addProperty(type, person);
						persons.put(URI, personita);
						newDocument.addProperty(autor, persons.get(URI));

					} else if (doc.getElementsByTagName("dc:creator").item(i).getTextContent()
							.split(", ").length == 2) {
						String URI = DOMAIN_PATH
								+ doc.getElementsByTagName("dc:creator").item(i).getTextContent().replace(" ", "");
						Resource personita = model.createResource(URI)
								.addProperty(name,
										doc.getElementsByTagName("dc:creator").item(i).getTextContent().split(", ")[1])
								.addProperty(lastName,
										doc.getElementsByTagName("dc:creator").item(i).getTextContent().split(", ")[0])
								.addProperty(type, person);
						persons.put(URI, personita);
						newDocument.addProperty(autor, persons.get(URI));
					}
				}
			}

			// Subject ignored

			// Description
			if (doc.getElementsByTagName("dc:description").item(0) != null) {
				String desc = doc.getElementsByTagName("dc:description").item(0).getTextContent();
				newDocument.addProperty(description, desc);
				for (String a : temas) {
					if (normalize(desc).matches(".*([^A-Za-z]|^)" + a + "([^A-Za-z]|$).*")) {
						newDocument.addProperty(hasConcept, concepts.get(DOMAIN_PATH + a));
					}
				}
			}

			// Publisher
			if (doc.getElementsByTagName("dc:publisher").item(0) != null) {
				newDocument.addProperty(publisher, doc.getElementsByTagName("dc:publisher").item(0).getTextContent());
			}

			// Contributor ignored

			// Date
			if (doc.getElementsByTagName("dc:date").item(0) != null) {
				newDocument.addProperty(date, doc.getElementsByTagName("dc:date").item(0).getTextContent());
			}

			// Type ignored

			// Format ignored

			// Source ignored

			// Language
			if (doc.getElementsByTagName("dc:language").item(0) != null) {
				newDocument.addProperty(language, doc.getElementsByTagName("dc:language").item(0).getTextContent());
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

	/**
	 * Metodo que genera el modelo terminologico de skos en base a un fichero
	 * especificado por parametro
	 */
	private static void generateTesauro(String tesauroPath) {
		try {
			Scanner s = new Scanner(new File(tesauroPath));
			Resource used = null;
			while (s.hasNextLine()) {

				String[] line = s.nextLine().split("-");

				if (line.length == 1) { // Tema
					temas.add(normalize(line[0]));
					used = generateConcept(normalize(line[0]));
				} else if (line.length == 2) { // SubTema
					temas.add(normalize(line[1]));
					generateSubconcept(used, normalize(line[1]));
					used.addProperty(narrower, normalize(line[1]));
				}

			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo auxiliar
	 */
	private static Resource generateConcept(String tema) {
		Resource temp = model.createResource(DOMAIN_PATH + tema).addProperty(type, concept).addProperty(conceptName,
				tema);
		concepts.put(DOMAIN_PATH + tema, temp);
		return temp;
	}

	/**
	 * Metodo auxiliar
	 */
	private static void generateSubconcept(Resource original, String tema) {
		Resource temp = model.createResource(DOMAIN_PATH + tema).addProperty(type, concept)
				.addProperty(broader, original).addProperty(conceptName, tema);
		concepts.put(DOMAIN_PATH + tema, temp);
	}

	/**
	 * Metodo auxiliar
	 */
	private static String normalize(String s) {
		s = s.toLowerCase();
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s;
	}

}
