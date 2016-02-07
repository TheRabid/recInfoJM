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

public class SemanticSearcher {

	/**
	 * Ejecucion de las consultas
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
		
		String rdf = "Modelo.rdf";
		String rdfs = "";	// Unused
		String infoNeeds = "infoNeedsS.txt";
		String output = "equipo10semantic.txt";
		
		

		// cargamos el fichero deseado
		Model model = FileManager.get().loadModel(rdf);
		PrintWriter writer = new PrintWriter(output, "UTF-8");

		
		/*Scanner s = new Scanner(new File(infoNeeds));
		while (s.hasNextLine()) {
			String numCons = s.next();
//			if (!numCons.equals("05-05")) continue;
			String queryString = s.nextLine();
			System.out.println("Nueva consulta " + numCons);

			if (!queryString.equals("")) {

				// ejecutamos la consulta y obtenemos los resultados
				Query query = QueryFactory.create(queryString);
				QueryExecution qexec = QueryExecutionFactory.create(query, model);

				try {
					ResultSet results = qexec.execSelect();
					for (; results.hasNext();) {
						System.out.println("new " + numCons); 
						QuerySolution soln = results.nextSolution();
						Iterator<String> it = soln.varNames();
						writer.printf(numCons + "\t");
						while (it.hasNext()) {
							writer.printf(soln.get(it.next()) + "\t");
						}
						writer.println();

					}
				} finally {
					qexec.close();
				}
			}
		}*/
		
		for (int i = 0; i < Consultas.consultas.length; i++) {
//			if (i != 4) continue;

			System.out.println("Nueva consulta");
			String queryString = Consultas.consultas[i];

			if (!queryString.equals("")) {

				// ejecutamos la consulta y obtenemos los resultados
				Query query = QueryFactory.create(queryString);
				QueryExecution qexec = QueryExecutionFactory.create(query, model);

				try {
					ResultSet results = qexec.execSelect();
					for (; results.hasNext();) {
						System.out.println("new "); 
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