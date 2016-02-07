package trabajoSemantico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Scanner;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase SemanticSearcher contiene metodos para hacer busquedas
 *         SPARQL asi como un metodo principal para poder consultar el modelo
 *         rdf generado en base a las consultas contenidas en un fichero
 */

public class SemanticSearcher {

	/**
	 * El metodo main lleva a cabo las consultas SPARQL de acuerdo a la
	 * especificacion del enunciado
	 */
	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {

		String rdf = "Modelo.rdf";
		@SuppressWarnings("unused")
		String rdfs = ""; // Unused
		String infoNeeds = "infoNeedsS.txt";
		String output = "semanticResults10.txt";

		for (int i = 0; i < args.length; i += 2) {
			if (args[i].toLowerCase().equals("-rdf")) {
				rdf = args[i + 1];
			}
			if (args[i].toLowerCase().equals("-rdfs")) {
				rdfs = args[i + 1];
			}
			if (args[i].toLowerCase().equals("-infoNeeds")) {
				infoNeeds = args[i + 1];
			}
			if (args[i].toLowerCase().equals("-output")) {
				output = args[i + 1];
			}
		}

		// Carga del modelo
		Model model = FileManager.get().loadModel(rdf);
		PrintWriter writer = new PrintWriter(output, "UTF-8");

		Scanner s = new Scanner(new File(infoNeeds));
		while (s.hasNextLine()) { // Para cada consulta
			String numCons = s.next();
			String queryString = s.nextLine();
			if (!queryString.equals("")) {

				// Ejecutamos la consulta y obtenemos los resultados
				Query query = QueryFactory.create(queryString);
				QueryExecution qexec = QueryExecutionFactory.create(query, model);

				try {
					ResultSet results = qexec.execSelect();
					for (; results.hasNext();) { // Se escribe cada resultado en
													// output
						QuerySolution soln = results.nextSolution();
						Iterator<String> it = soln.varNames();
						writer.printf(numCons + "\t");
						while (it.hasNext()) {
							writer.printf(soln.get(it.next())+"");
						}
						writer.println();

					}
				} finally {
					qexec.close();
				}
			}
		}
		s.close();
		writer.close();
	}

}