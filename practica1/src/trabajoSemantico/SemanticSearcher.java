package trabajoSemantico;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class SemanticSearcher {
	
	public static final String DOMAIN_PATH = "http://www.recInfo.com/";


	/**
	 * ejecuci�n de consultas sparql
	 */
	public static void main(String args[]) {

		// cargamos el fichero deseado
		Model model = FileManager.get().loadModel("Modelo.rdf");

		// definimos la consulta (tipo query)
//		String queryString = ""
//				+ " PREFIX recinfo: <http://www.recInfo.com/> \n"
//				+ " PREFIX skos: <http://www.w3.org/TR/skos-primer/> \n"
//				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ " SELECT ?x WHERE  { \n"
//				+ " ?x rdf:type recinfo:Document. \n"
//				+ " ?x skos:narrower \"http://www.recInfo.com/arquitectura\". \n"
//				+ " } ";
		
		String queryString = ""
				+ " PREFIX recinfo: <http://www.recInfo.com/> \n"
				+ " PREFIX skos: <http://www.w3.org/TR/skos-primer/> \n"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ " select distinct ?id ?concept where {"
				+ " ?doc recinfo:Identificador ?id."
				+ " ?doc recinfo:hasConcept ?concept."
				+ " ?concept recinfo:conceptName \"musica\". "
				+ " ?doc recinfo:Autor ?autor."
				+ " ?autor recinfo:Nombre \"Javier\""
				+ " }";

		// ejecutamos la consulta y obtenemos los resultados
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				RDFNode x = soln.get("id");
				RDFNode y = soln.get("concept");
//				RDFNode z = soln.get("y");
//				Resource y = soln.getResource("y");
				/*RDFNode z = soln.get("z");
				if (z.isLiteral()) {
					System.out.println(x.getURI() + " - " + y.getURI() + " - " + z.toString());
				} else {
					System.out.println(x.getURI() + " - " + y.getURI() + " - " + z.asResource().getURI());
				}*/
				
				System.out.println(x.toString() + " - " + y.toString());
			}
		} finally

		{
			qexec.close();
		}

		System.out.println("----------------------------------------");

	}

}