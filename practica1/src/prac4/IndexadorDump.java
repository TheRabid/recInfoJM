package prac4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase Indexador implementa metodos para indexar los documentos de
 *         una coleccion determinada para mas adelante realizar busquedas sobre
 *         esos indices creados. Esta version en concreto lo hace sobre el volcado
 *         del segmento que da Apache Nutch
 */
public class IndexadorDump {

	/* Atributos privados */

	// Booleano para debug. Si es true muestra informacion adicional por
	// pantalla
	private static final boolean DEBUG = false;

	/**
	 * Metodo main de la clase Indexador. Indexa todos los documentos pasados
	 * por parametro (seg_path) en index_path.
	 * 
	 * Uso: java trabajo.Indexador [-index INDEX_PATH] [-seg SEG_PATH]
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws SAXException, ParserConfigurationException {
		String usage = "Uso: java trabajo.IndexFiles" + " [-index INDEX_PATH] [-seg segPath]\n\n";

		String indexPath = "indexDump";
		String dumpPath = "dumps/dump";
		boolean create = true;
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[i + 1];
				i++;
			} else if ("-docs".equals(args[i])) {
				dumpPath = args[i + 1];
				i++;
			}
		}

		final File docDir = new File(dumpPath);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out.println("El directorio '" + docDir.getAbsolutePath()
					+ "' no existe o no se puede leer. Por favor compruebelo de nuevo");
			System.exit(1);
		}

		Date start = new Date();
		try {
			if (DEBUG)
				System.out.println("Indexando '" + indexPath + "'...");
			Directory dir = FSDirectory.open(new File(indexPath));
			Analyzer analyzer = new CustomSpanishAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);

			if (create) {
				// Crea un nuevo indice borrando los anteriores
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Anade documentos a indice creado
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			IndexWriter writer = new IndexWriter(dir, iwc);
			ArrayList<Par> dumpFiles = parseDump(dumpPath);
			indexDump(dumpFiles, writer);

			writer.close();

			Date end = new Date();
			if (DEBUG)
				System.out.println(end.getTime() - start.getTime() + " milisegundos");

		} catch (

		IOException e)

		{
			System.out.println("Excepcion " + e.getClass() + "\n con el mensaje: " + e.getMessage());
		}

	}

	/**
	 * 
	 * Metodo indexDocs de la clase Indexador. Indexa el archivo pasado por
	 * parametro usando el IndexWriter pasado tambien por parametro o, si se ha
	 * pasado por parametro un directorio, itera a lo largo de todos los
	 * ficheros de dicho directorio
	 * 
	 * @param writer
	 *            Writer to the index where the given file/dir info will be
	 *            stored
	 * @param file
	 *            The file to index, or the directory to recurse into to find
	 *            files to index
	 * @throws IOException
	 *             If there is an I/O error
	 */
	@SuppressWarnings("unused")
	private static void indexDocs(IndexWriter writer, File file) throws IOException {
		// No intentar indexar ficheros que no se pueden leer
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// Posibilidad de que de error IO
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {

				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					// En windows algunos ficheros temporales dan esta excepcion
					return;
				}

				try {

					// Crear un nuevo Document vacio
					Document doc = new Document();

					// Anadir el path del fichero como un campo llamado "path"
					// sin tokenizarlo
					Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
					doc.add(pathField);

					// Anadir tambien el campo "modified" con la fecha de
					// modificacion del fichero
					doc.add(new LongField("modified", file.lastModified(), Field.Store.YES));

					// Parsear el documento
					documentParser(file, doc);

					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						// Si el indice es nuevo anadimos sin mas
						if (DEBUG)
							System.out.println("adding " + file);
						writer.addDocument(doc);
					} else {
						// Indice existente, por lo que actualizamos el
						// documento
						if (DEBUG)
							System.out.println("updating " + file);
						writer.updateDocument(new Term("path", file.getPath()), doc);
					}

				} catch (ParserConfigurationException | SAXException e) {
					e.printStackTrace();
				} finally {
					fis.close();
				}
			}
		}
	}
	
	
	private static void indexDump(ArrayList<Par> dump, IndexWriter writer) throws IOException, SAXException, ParserConfigurationException {
		
		for (Par par:dump) {
			// Crear un nuevo Document vacio
			System.out.println(par.docName);
			System.out.println(par.content);
			Document doc = new Document();
			
			String path = par.docName;

			// Anadir el path del fichero como un campo llamado "path"
			// sin tokenizarlo
			Field pathField = new StringField("path", path, Field.Store.YES);
			doc.add(pathField);

			// Anadir tambien el campo "modified" con la fecha de
			// modificacion del fichero
//				doc.add(new LongField("modified", file.lastModified(), Field.Store.YES));

			// Parsear el documento
			documentParserDump(par.content, doc);

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				// Si el indice es nuevo anadimos sin mas
				if (DEBUG)
					System.out.println("adding " + path);
				writer.addDocument(doc);
			} else {
				// Indice existente, por lo que actualizamos el
				// documento
				if (DEBUG)
					System.out.println("updating " + path);
				writer.updateDocument(new Term("path", path), doc);
			}
			
		}
		System.out.println(dump.size());
	}


	/**
	 * 
	 * Metodo documentParser de la clase Indexador. Parsea el documento XML a la
	 * busqueda de etiquetas que se puedan analizar y ubicar como campos en el
	 * indice correspondiente
	 * 
	 * @param f
	 *            El fichero a parsear
	 * @param doc1
	 *            El documento al que anadir los campos
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void documentParserDump(String xml, Document doc1)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(xml));
		org.w3c.dom.Document doc = builder.parse(is);
		
		// Title
		if (doc.getElementsByTagName("dc:title").item(0) != null) {
			doc1.add(new TextField("title", doc.getElementsByTagName("dc:title").item(0).getTextContent(),
					Field.Store.YES));
		}

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
			doc1.add(new StringField("language", doc.getElementsByTagName("dc:rights").item(0).getTextContent(),
					Field.Store.YES));
		}
	}

	
	/**
	 * 
	 * Metodo documentParser de la clase Indexador. Parsea el documento XML a la
	 * busqueda de etiquetas que se puedan analizar y ubicar como campos en el
	 * indice correspondiente
	 * 
	 * @param f
	 *            El fichero a parsear
	 * @param doc1
	 *            El documento al que anadir los campos
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	private static void documentParser(File f, Document doc1)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		FileInputStream fis = new FileInputStream(f);
		InputSource is = new InputSource(fis);
		org.w3c.dom.Document doc = builder.parse(is);

		// Title
		if (doc.getElementsByTagName("dc:title").item(0) != null) {
			doc1.add(new TextField("title", doc.getElementsByTagName("dc:title").item(0).getTextContent(),
					Field.Store.YES));
		}

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
	}

	private static ArrayList<Par> parseDump(String path) throws FileNotFoundException {
		/* Initialize objects */
		File f = new File(path);
		Scanner s = new Scanner(f, "UTF-8");
		boolean nameFound = false;
		boolean contentFound = false;
		Par temp = new Par(null, null);
		ArrayList<Par> returned = new ArrayList<Par>();
		int cuenta = 0;

		while (s.hasNextLine()) {
			String line = s.nextLine();
			int index = line.indexOf(':');

			if (index > 0) {
				/* Name found */
				if (line.substring(0, index).equals("url")) {
					// Get the name
					int index2 = line.lastIndexOf('/');
					String name = line.substring(index2);
					temp.docName = name;
					nameFound = true;
				}

				/* Content found */
				if (line.substring(0, index).equals("Content") && line.length() - 1 == index) {
					String newLine = s.nextLine();
					if (!newLine.startsWith("<?xml")) {
						// Esto no es
						temp = new Par(null, null);
						nameFound = false;
					} else {
						temp.content = newLine;
						contentFound = true;
						newLine = s.nextLine();
						while(!newLine.startsWith("Recno::") && s.hasNextLine()){	
							temp.content = temp.content + newLine;
							newLine = s.nextLine();
						}
					}
				}
			}
			/* If both found add to arraylist */
			if (nameFound && contentFound) {
				nameFound = false;
				contentFound = false;
				returned.add(temp);
				temp = new Par(null, null);
				cuenta++;
				System.out.println(cuenta);
			}
		}
		s.close();
		return returned;
	}

	/**
	 * Constructor (privado, puesto que no se puede instanciar)
	 */
	private IndexadorDump() {
	}
}