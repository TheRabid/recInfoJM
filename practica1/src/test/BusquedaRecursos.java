package test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class BusquedaRecursos {

	
	public static void main(String[] args) {
		
		// cargamos el fichero deseado
		Model model = FileManager.get().loadModel("card.rdf");
		
		Resource r = model.getResource("http://wiki.ontoworld.org/index.php/_IRW2006");
		
		// obtenemos todos los statements del modelo
		StmtIterator it = model.listStatements(
				  new
			      SimpleSelector(null, null, (RDFNode) null) {
			          public boolean selects(Statement s) {
			              return r.hasProperty(s.getPredicate());
			          }
			     });
		
		
		// mostramos todas las tripletas cuyo objeto es un literal
		while (it.hasNext()) {
			Statement st = it.next();

			if (st.getObject().isLiteral()) {
				System.out.println(st.getSubject().getURI() + " - "
						+ st.getPredicate().getURI() + " - "
						+ st.getLiteral().toString());
			}
		}

	}
}
