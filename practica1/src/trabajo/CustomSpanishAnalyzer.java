package trabajo;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.tartarus.snowball.ext.SpanishStemmer;

/**
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * @author Alberto Sabater Bailon (546297)
 * 
 *         La clase CustomSpanishAnalyzer implementa una version propia del
 *         SpanishAnalyzer estandar de Lucene
 */
public class CustomSpanishAnalyzer extends Analyzer {

	private final static String[] CUSTOM_STOP_SET = { "interes", "trabaj", "llam", "relacion", "cuy", "llam", "period",
			"gust", "sab", "articul", "relacionad", "hech", "englob", "tesis", "trat", "quier", "document", "encontr",
			"situ", "element", "pertenezc","mund"};

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final Tokenizer source = new StandardTokenizer(reader);
		TokenStream result = new StandardFilter(source);
		result = new LowerCaseFilter(result);
		result = new StopFilter(result, SpanishAnalyzer.getDefaultStopSet());
		result = new SnowballFilter(result, new SpanishStemmer());
		CharArraySet stopSet = new CharArraySet(0, false);
		for (String a : CUSTOM_STOP_SET)
			stopSet.add(a);
		result = new StopFilter(result, stopSet);
		return new TokenStreamComponents(source, result);
	}

}
