package trabajo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase Buscador implementa metodos para buscar datos de
 *         consultas/necesidades de informacion en un indice creado por Lucene
 *         previamente. Posee asimismo un metodo main que se encarga de
 *         gestionar las necesidades de informacion contenidas en un fichero XML
 *         formateado de una manera determinada
 */

public class Buscador {

	/* Atributos privados */

	// Contiene la lematizacion de una necesidad de informacion
	private static List<String> result;

	// Booleano para debug. Si es true muestra informacion adicional por
	// pantalla
	private static final boolean DEBUG = true;

	/**
	 * Metodo main de la clase Buscador. Uso de este programa: java
	 * trabajo.Buscador -index <indexPath> -infoNeeds <infoNeedsFile> -output
	 * <resultsFile>
	 */
	public static void main(String[] args) throws Exception {
		/* Comprobar que se ha invocado bien al programa */
		String usage = "Uso de este programa:\tjava trabajo.Buscador -index <indexPath>"
				+ " -infoNeeds <infoNeedsFile> -output <resultsFile>";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

		/* Variables por defecto */
		String index = "index";
		String infoNeeds = "infoNeedsFile.xml";
		String output = "resultsFile.txt";
		int totalDocs = 10;

		/* Busqueda de los parametros del programa */
		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				index = args[i + 1];
				i++;
			} else if ("-infoNeeds".equals(args[i])) {
				infoNeeds = args[i + 1];
				i++;
			} else if ("-output".equals(args[i])) {
				output = args[i + 1];
				i++;
			}
		}

		// Creacion de IndexReader e IndexSearcher
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);

		// Creacion del PrintWriter que imprimira los resultados
		PrintWriter writer = new PrintWriter(output, "UTF-8");

		// Parseo y analisis del fichero de necesidades de informacion
		File f = new File(infoNeeds);
		ArrayList<String[]> results = informationNeedsParser(f);
		String[] identifiers = results.get(0);
		String[] needs = results.get(1);

		// Bucle para cada necesidad de informacion
		for (int i = 0; i < needs.length; i++) {

			// Arreglar la necesidad de informacion
			String input = needs[i].replace("\n", " ").replace(".", "").replace("\t", "").trim();
			if (DEBUG)
				System.out.println(input);

			// Lematizacion de la consulta
			result = spanisher(input);
			if (DEBUG)
				System.out.println(result);

			// Procesamiento de la consulta
			Query query = generateQuery(input);

			// Busqueda de la consulta
			String strong = buscar(searcher, query, totalDocs, identifiers[i], DEBUG);

			// Devolucion de la consulta
			writeToFile(writer, strong);
		}
		writer.close();
	}

	/**
	 * Metodo spanisher de la clase Buscador. Lematiza la necesidad de
	 * informacion pasada por parametro con un analizador personalizado espanol
	 * y devuelve una lista de Strings con las lematizaciones de dicha necesidad
	 * de informacion
	 * 
	 * @param input:
	 *            Necesidad de informacion
	 * 
	 * @return: Necesidad de informacion lematizada
	 */
	private static ArrayList<String[]> informationNeedsParser(File f)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		FileInputStream fis = new FileInputStream(f);
		InputSource is = new InputSource(fis);
		org.w3c.dom.Document doc = builder.parse(is);

		NodeList mList = doc.getElementsByTagName("informationNeed");
		String[] identifiers = new String[mList.getLength()];
		String[] needs = new String[mList.getLength()];

		for (int temp2 = 0; temp2 < mList.getLength(); temp2++) {
			Node mNode = mList.item(temp2);

			if (mNode.getNodeType() == Node.ELEMENT_NODE) {
				Element mElement = (Element) mNode;

				// Identifier for information need
				if (mElement.getElementsByTagName("identifier").item(0) != null) {
					String identifier = mElement.getElementsByTagName("identifier").item(0).getTextContent();
					identifiers[temp2] = identifier;
				}

				// Information need itself
				if (mElement.getElementsByTagName("text").item(0) != null) {
					String need = mElement.getElementsByTagName("text").item(0).getTextContent();
					needs[temp2] = need;
				}
			}
		}

		ArrayList<String[]> returned = new ArrayList<String[]>();
		returned.add(identifiers);
		returned.add(needs);
		return returned;
	}

	/**
	 * Metodo spanisher de la clase Buscador. Lematiza la necesidad de
	 * informacion pasada por parametro con un analizador personalizado espanol
	 * y devuelve una lista de Strings con las lematizaciones de dicha necesidad
	 * de informacion
	 * 
	 * @param input:
	 *            Necesidad de informacion
	 * 
	 * @return: Necesidad de informacion lematizada
	 */
	private static List<String> spanisher(String input) {
		Analyzer analyzer = new CustomSpanishAnalyzer();
		List<String> result = new ArrayList<String>();
		try {
			TokenStream stream = analyzer.tokenStream(null, new StringReader(input));
			stream.reset();
			while (stream.incrementToken()) {
				result.add(stream.getAttribute(CharTermAttribute.class).toString());
			}
		} catch (IOException e) {
			// not thrown b/c we're using a string reader...
			analyzer.close();
			throw new RuntimeException(e);
		}
		analyzer.close();
		return result;
	}

	/**
	 * Metodo generateQuery de la clase Buscador. Genera una BooleanQuery a
	 * partir de las necesidades de informacion pasadas por parametro
	 * 
	 * @param input:
	 *            La necesidad de informacion
	 * 
	 * @return: La query generada
	 */
	private static Query generateQuery(String input) {
		// Query resultante
		BooleanQuery query = new BooleanQuery();

		// Procesamiento explicito de autores y fechas
		ArrayList<String> autores = getCreators(input);
		ArrayList<Integer> period = getPeriods();
		ArrayList<Integer> years = getLastNYearsDocs();

		/*
		 * Si en la busqueda se detecta autor o director, anadir TermQuerys
		 * buscando en el campo "creator"
		 */
		if (autores.size() != 0) {
			BooleanQuery author = new BooleanQuery();
			for (String autor : autores) {
				TermQuery queryStr = new TermQuery(new Term("creator", autor));
				author.add(queryStr, BooleanClause.Occur.SHOULD);
			}
			query.add(author, BooleanClause.Occur.MUST);
		}

		/*
		 * Si en la busqueda se detecta un periodo de tiempo (dos numeros de 4
		 * cifras seguidos), se anaden RangeQuerys buscando en el campo "date"
		 */
		if (period.size() != 0) {
			for (int j = 0; j < period.size(); j = j + 2) {
				if (period.get(j) >= 2008) {
					NumericRangeQuery<Integer> periodQuery = NumericRangeQuery.newIntRange("date", period.get(j),
							period.get(j + 1), true, true);
					query.add(periodQuery, BooleanClause.Occur.MUST);
				}
			}
		}

		/*
		 * Si en la busqueda se detecta que se especifica la busqueda de
		 * documentos en los ultimos x anyos, se anaden RangeQuerys buscando en
		 * el campo "date"
		 */
		if (years.size() != 0) {
			for (int j = 0; j < years.size(); j = j + 2) {
				int currentYear = Calendar.getInstance().get(Calendar.YEAR);
				NumericRangeQuery<Integer> periodQuery = NumericRangeQuery.newIntRange("date",
						currentYear - years.get(j), currentYear, true, true);
				query.add(periodQuery, BooleanClause.Occur.MUST);
			}
		}

		/*
		 * Para el resto de la consulta, anadirlo directamente a la query para
		 * el campo titulo y descripcion
		 */
		BooleanQuery descriptionTitle = new BooleanQuery();
		for (String q : result) {
			TermQuery queryStr = new TermQuery(new Term("description", q));
			descriptionTitle.add(queryStr, BooleanClause.Occur.SHOULD);
		}

		for (String q : result) {
			TermQuery queryStr = new TermQuery(new Term("title", q));
			descriptionTitle.add(queryStr, BooleanClause.Occur.SHOULD);
		}
		query.add(descriptionTitle, BooleanClause.Occur.MUST);

		return query;
	}

	/**
	 * Metodo getCreators de la clase Buscador. Dada una necesidad de
	 * informacion pasada por parametro, getCreators devuelve una lista con los
	 * nombres de los creadores que se desean buscar en dicha necesidad de
	 * informacion
	 * 
	 * @param input:
	 *            La necesidad de informacion
	 * 
	 * @return: Un ArrayList con los autores/directores que se buscan en la
	 *          necesidad de informacion
	 */
	private static ArrayList<String> getCreators(String input) {
		ArrayList<String> autores = new ArrayList<String>();
		if (input.contains("autor")) {
			result.remove("autor");
			String autorInput = input.split("autor")[1];
			boolean encontrado = false;
			boolean fin = false;

			Scanner s = new Scanner(autorInput);
			while (s.hasNext() && !fin) {
				String word = s.next();

				List<String> sp = spanisher(word);
				if (sp.size() != 0) {
					result.remove(sp.get(0));
				}

				if (word.charAt(0) == Character.toUpperCase(word.charAt(0))) {
					autores.add(spanisher(word).get(0));
					encontrado = true;
				} else {
					if (encontrado) {
						if (sp.size() != 0) {
							result.add(sp.get(0));
						}
						fin = true;
					}
				}
			}
			s.close();
		}
		return autores;
	}

	/**
	 * Metodo getPeriods de la clase Buscador. Utiliza la ArrayList result para
	 * buscar dos fechas de anyo consecutivas y establecer un periodo de anyos
	 * entre ellas. Devuelve un ArrayList de Integer en el que dos Integer
	 * consecutivos representan un periodo de anyos que puede resultar de
	 * interes para la consulta
	 * 
	 * @return: ArrayList de Integer con los periodos
	 */
	private static ArrayList<Integer> getPeriods() {
		ArrayList<Integer> period = new ArrayList<Integer>();
		for (int i = 0; i < result.size(); i++) {
			try {
				int date = Integer.parseInt(result.get(i));
				if (result.get(i).length() == 4) {

					try {
						int date2 = Integer.parseInt(result.get(i + 1));
						if (result.get(i + 1).length() == 4) {
							period.add(date);
							period.add(date2);
							i++;
						}
					} catch (NumberFormatException e) {
					}
				}
			} catch (NumberFormatException e) {
			}
		}

		return period;
	}

	/**
	 * Metodo getLastNYearsDocs de la clase Buscador. Utiliza la ArrayList
	 * result para buscar los terminos lematizados "ultim", un entero y "anyo"
	 * para anadir a la consulta la busqueda de documentos cuya fecha de
	 * creacion se halle entre el anyo actual y los n anteriores (siendo n el
	 * numero determinado por el entero mencionado anteriormente).
	 * 
	 * @return: ArrayList de Integer con el numero de ultimos anyos que la
	 *          necesidad de informacion pide
	 */
	private static ArrayList<Integer> getLastNYearsDocs() {
		ArrayList<Integer> years = new ArrayList<Integer>();

		for (int i = 0; i < result.size(); i++) {
			try {
				int year = Integer.parseInt(result.get(i));
				if ((i + 1) < result.size() && result.get(i + 1).equalsIgnoreCase("año") && (i - 1) >= 0
						&& result.get(i - 1).equalsIgnoreCase("ultim")) {
					years.add(year);
				}
			} catch (NumberFormatException e) {
			}
		}

		return years;
	}

	/**
	 * Metodo buscar de la clase Buscador. Dada una consulta en forma de objeto
	 * Query, devuelve un String con los documentos relevantes devueltos para
	 * esa consulta
	 * 
	 * @param searcher:
	 *            Objeto IndexSearcher que realizara la busqueda
	 * @param query:
	 *            Objeto de tipo Query con la consulta en si
	 * @param docs:
	 *            Entero que almacena el numero de documentos que se desea
	 *            devolver de la consulta
	 * @param need:
	 *            Identificador de la necesidad de informacion
	 * @param debug:
	 *            Booleano para debug
	 * 
	 * @throws IOException
	 */
	private static String buscar(IndexSearcher searcher, Query query, int docs, String need, boolean debug)
			throws IOException {
		String returned = "";

		// Buscar los n primeros documentos para la consulta
		TopDocs results = searcher.search(query, docs);
		ScoreDoc[] hits = results.scoreDocs;
		int numTotalHits = results.totalHits;
		if (debug)
			System.out.println(numTotalHits + " total matching documents");

		// Crear el string
		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String path = doc.get("path");
			if (path != null) {
				returned = returned + need + "\t";
				returned = returned + path
						.split(Pattern.quote(File.separator))[path.split(Pattern.quote(File.separator)).length - 1];
				returned = returned + "\n";
			}
		}

		return returned;
	}

	/**
	 * Metodo writeToFile de la clase Buscador. Imprime el String strong
	 * mediante el PrintWriter output
	 * 
	 * @param output:
	 *            Objeto PrintWriter que imprimira el String
	 * @param strong:
	 *            String a imprimir
	 */
	private static void writeToFile(PrintWriter output, String strong) {
		output.print(strong);
	}

	/**
	 * Constructor (privado puesto que no se puede instanciar)
	 */
	private Buscador() {
	}
}