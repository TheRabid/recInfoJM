package test;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;



public class Inference {

	/**
	 * ejemplo de uso de la inferencia
	 */
	public static void main (String args[]) {
		String NS = "urn:x-hp-jena:eg/";
	
		// contruimos un modelo secillo 
		Model rdfsExample = ModelFactory.createDefaultModel();
		Property hijoDe = rdfsExample.createProperty(NS, "hijoDe");
		Property hijoAdoptDe = rdfsExample.createProperty(NS, "hijoAdoptivoDe");
		rdfsExample.add(hijoAdoptDe, RDFS.subPropertyOf, hijoDe);
		rdfsExample.createResource(NS+"Luis").addProperty(hijoAdoptDe, rdfsExample.createResource(NS+"Juan"));
		
		Resource r = rdfsExample.createResource(NS+"Animal");
		Resource t = rdfsExample.createResource(NS+"Mamifero");
		r.addProperty(RDFS.subClassOf, t);
		Resource b = rdfsExample.createResource(NS+"Humano");
		b.addProperty(RDFS.subClassOf, r);
		Resource l = rdfsExample.createResource(NS+"Juan");
		l.addProperty(RDF.type, b);
		System.out.println();
		
		//resultados sin inferencia
		System.out.println("Sin Inferencia");
		Resource luis = rdfsExample.getResource(NS+"Luis");
		System.out.println("Statement: " + luis.getProperty(hijoAdoptDe));
		System.out.println("Statement: " + luis.getProperty(hijoDe));
		
		//miramos las superclases de b en el modelo inferido
		Resource humano = rdfsExample.getResource(NS+"Humano");
		for(Statement st: humano.listProperties(RDFS.subClassOf).toList()){
			System.out.println(humano.getURI()+" "+RDFS.subClassOf.getURI()+" "+st.getResource().getURI());
		}
		
		//miramos los tipos del recurso l en el modelo inferido
		Resource juan = rdfsExample.getResource(NS+"Juan");
		for(Statement st: juan.listProperties(RDF.type).toList()){
			System.out.println(juan.getURI()+" "+RDF.type.getURI()+" "+st.getResource().getURI());
		}
		
		//creamos un modelo de inferencia
		InfModel inf = ModelFactory.createRDFSModel(rdfsExample);
		System.out.println();
		System.out.println("Inferencia");
		//observamos que en el modelo tambien existe que a 
		//está relacionado con foo con las propiedades p y q
		luis = inf.getResource(NS+"Luis");
		System.out.println("Statement: " + luis.getProperty(hijoAdoptDe));
		System.out.println("Statement: " + luis.getProperty(hijoDe));
		
		//miramos las superclases de b en el modelo inferido
		humano = inf.getResource(NS+"Humano");
		for(Statement st: humano.listProperties(RDFS.subClassOf).toList()){
			System.out.println(humano.getURI()+" "+RDFS.subClassOf.getURI()+" "+st.getResource().getURI());
		}
		
		//miramos los tipos del recurso l en el modelo inferido
		juan = inf.getResource(NS+"Juan");
		for(Statement st: juan.listProperties(RDF.type).toList()){
			System.out.println(juan.getURI()+" "+RDF.type.getURI()+" "+st.getResource().getURI());
		}
		
	}
	
	
	
	
}
