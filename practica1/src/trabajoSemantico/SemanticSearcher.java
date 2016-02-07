package trabajoSemantico;

import java.util.Iterator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
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
		
		
		/*
		//Consulta 13_02
		String queryString = ""
				+ " PREFIX recinfo: <http://www.recInfo.com/> \n"
				+ " PREFIX skos: <http://www.w3.org/TR/skos-primer/> \n"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ " SELECT DISTINCT ?id WHERE {"
				+ "   ?doc recinfo:Identificador ?id."
				+ "   ?doc recinfo:hasConcept ?concept."
				+ "   { ?concept recinfo:conceptName \"musica\". } "
				+ "   UNION"
				+ "   { ?concept skos:broader ?sub . "
				+ "     ?sub recinfo:conceptName \"musica\" }"
				+ "   ?doc recinfo:Autor ?autor."
				+ "   ?autor recinfo:Nombre ?name"
				+ "   FILTER regex (?name, \".*Javier.*\", \"i\") "
				+ " }"
				+ " ORDER BY ?id ";
		*/
		
		/*
		//Consulta 02_04
		String queryString = ""
				+ " PREFIX recinfo: <http://www.recInfo.com/> \n"
				+ " PREFIX skos: <http://www.w3.org/TR/skos-primer/> \n"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ " SELECT DISTINCT ?id ?concept where {"
				+ "    ?doc recinfo:hasConcept ?concept1 . "
				+ "    ?doc recinfo:hasConcept ?concept2 . "
				+ "    ?doc recinfo:hasConcept ?concept3 . "
				+ "    ?doc recinfo:Identificador ?id "
				+ "    { "
				+ "      { ?concept1 recinfo:conceptName \"guerra\" }"
				+ "      UNION"
				+ "      { ?concept1 skos:broader ?sub . "
				+ "        ?sub recinfo:conceptName \"guerra\" } "
				+ "      { ?concept2 recinfo:conceptName \"historia\" }"
				+ "      UNION"
				+ "      { ?concept2 skos:broader ?sub . "
				+ "        ?sub recinfo:conceptName \"historia\" } "
				+ "    } "
				+ "    UNION"
				+ "    { ?concept3 recinfo:conceptName \"espana\" }"
				+ "    UNION"
				+ "    { ?concept3 skos:broader ?sub . "
				+ "      ?sub recinfo:conceptName \"espana\" } "
				+ " }"
				+ " ORDER BY ?id ";
		*/
		
		//Consulta 09_3
		String queryString = ""
				+ " PREFIX recinfo: <http://www.recInfo.com/> \n"
				+ " PREFIX skos: <http://www.w3.org/TR/skos-primer/> \n"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
				+ " SELECT DISTINCT ?id WHERE {"
				+ "   ?doc recinfo:Identificador ?id."
				+ "   ?doc recinfo:hasConcept ?concept."
				+ "   { ?concept recinfo:conceptName \"energias\". } "
				+ "   UNION"
				+ "   { ?concept skos:broader ?sub . "
				+ "     ?sub recinfo:conceptName \"energias\" } "
				+ "   ?doc recinfo:Fecha ?year"
				+ "   FILTER (xsd:integer(?year) > 2010 && xsd:integer(?year) < 2015) "
				+ " }"
				+ " ORDER BY ?id ";
		

		// ejecutamos la consulta y obtenemos los resultados
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				
//				RDFNode z = soln.get("y");
//				Resource y = soln.getResource("y");
				/*RDFNode z = soln.get("z");
				if (z.isLiteral()) {
					System.out.println(x.getURI() + " - " + y.getURI() + " - " + z.toString());
				} else {
					System.out.println(x.getURI() + " - " + y.getURI() + " - " + z.asResource().getURI());
				}*/
				
				/*RDFNode x = soln.get("id");
				RDFNode y = soln.get("concept");
				System.out.println(x.toString() + "\t" + y.toString());*/
				
				Iterator<String> it = soln.varNames();
				while (it.hasNext()) {
					System.out.printf(soln.get(it.next()) + "    ");
				} System.out.println();
				
				
			}
		} finally

		{
			qexec.close();
		}

		System.out.println("----------------------------------------");

	}

}