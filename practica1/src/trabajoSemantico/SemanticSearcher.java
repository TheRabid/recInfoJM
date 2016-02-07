package trabajoSemantico;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

public class SemanticSearcher {

	/**
	 * Ejecucion de las consultas
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {

		// cargamos el fichero deseado
		Model model = FileManager.get().loadModel("Modelo.rdf");
		PrintWriter writer = new PrintWriter("equipo10semantic.txt", "UTF-8");

		for (int i = 0; i < Consultas.consultas.length; i++) {
			String queryString = Consultas.consultas[i];

			if (!queryString.equals("")) {

				// ejecutamos la consulta y obtenemos los resultados
				Query query = QueryFactory.create(queryString);
				QueryExecution qexec = QueryExecutionFactory.create(query, model);

				try {
					ResultSet results = qexec.execSelect();
					for (; results.hasNext();) {
						QuerySolution soln = results.nextSolution();
						Iterator<String> it = soln.varNames();
						writer.printf(Consultas.identificadores[i] + "\t");
						while (it.hasNext()) {
							writer.printf(soln.get(it.next()) + "\t");
						}
						writer.println();

					}
				} finally {
					qexec.close();
				}
			}
		}
		writer.close();
	}

}