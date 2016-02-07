package trabajoSemantico;

public class Consultas {
	
	public static String c13_02 = ""
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
			+ " }";
	
	public static String c02_04 = ""
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
			+ " }";
	
	public static String c09_03 = ""
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
			+ " }";
	
	public static String c07_02 = ""
			+ " PREFIX recinfo: <http://www.recInfo.com/> \n"
			+ " PREFIX skos: <http://www.w3.org/TR/skos-primer/> \n"
			+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
			+ " SELECT DISTINCT ?id WHERE {"
			+ "   ?doc recinfo:Identificador ?id."
			+ "   ?doc recinfo:hasConcept ?concept."
			+ "   { ?concept recinfo:conceptName \"videojuegos\".  "
			+ "     ?concept skos:broader ?sub . "
			+ "     ?sub recinfo:conceptName \"videojuegos\". } "
			+ "   UNION"
			+ "   { ?concept recinfo:conceptName \"diseño\" . "
			+ "     ?concept recinfo:conceptName \"personajes\". } "
			+ "   ?doc recinfo:Fecha ?year"
			+ "   FILTER (xsd:integer(?year) > 2010 && xsd:integer(?year) < 2016) "
			+ " }";
	
	public static String c05_05 = "";
	
	public static String[] identificadores = {"13-2" , "02-4" , "09-3" , "07-2" , "05-5"};
	public static String[] consultas = {c13_02 , c02_04, c09_03, c07_02, c05_05};
}
