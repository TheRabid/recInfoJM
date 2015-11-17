package trabajo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class Pruebas {

	public static void main(String[] args) {

		String line = "Me gustaría encontrar construcciones arquitectónicas situadas en España con elementos decorativos que pertenezcan tanto a la Edad Media como a la época gótica y cuyo estado de conservación sea óptimo.";
		
		Analyzer analyzer = new SpanishAnalyzer(Version.LUCENE_44);
		
		List<String> result = new ArrayList<String>();
	    try {
	      TokenStream stream  = analyzer.tokenStream(null, line);
	      stream.reset();
	      while (stream.incrementToken()) {
	        result.add(stream.getAttribute(CharTermAttribute.class).toString());
	      }
	    } catch (IOException e) {
	      // not thrown b/c we're using a string reader...
	      throw new RuntimeException(e);
	    }

	    System.out.println(result);
	}

}
