package trabajoSemantico;

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
	 */
	public static void main(String args[]) {

		// cargamos el fichero deseado
		Model model = FileManager.get().loadModel("Modelo.rdf");

		for (int i = 0; i < Consultas.consultas.length; i++) {
			String queryString = Consultas.consultas[i];
			
			// ejecutamos la consulta y obtenemos los resultados
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, model);

			try {
				ResultSet results = qexec.execSelect();
				for (; results.hasNext();) {
					QuerySolution soln = results.nextSolution();
					Iterator<String> it = soln.varNames();
					while (it.hasNext()) {
						System.out.printf(soln.get(it.next()) + "    ");
					}
					System.out.println();

				}
			} finally {
				qexec.close();
			}
		}
	}

}