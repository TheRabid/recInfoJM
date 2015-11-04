package trabajo;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** Simple command-line based search demo. */
public class SearchFiles {

	private SearchFiles() {
	}

	/** Simple command-line based search demo. */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		String usage = "Uso de este programa:\tjava trabajo.SearchFiles -index <indexPath>"
				+ " -infoNeeds <infoNeedsFile> -output <resultsFile>";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

		String index = "index";
		String infoNeeds = "infoNeedsFile.xml";
		String output = "resultsFile";
		String field = "contents";
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		String queryString = null;
		int hitsPerPage = 10;

		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				index = args[i + 1];
				i++;
			} else if ("-infoNeeds".equals(args[i])) {
				infoNeeds = args[i + 1];
				i++;
			} else if ("-output".equals(args[i])) {
				output = args[i + 1];
				i++;
			}
		}

		// Index
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new SpanishAnalyzer(Version.LATEST);

		/*
		 * Aquí va el codigo para obtener un String con las queries
		 */
		File f = new File(infoNeeds);
		ArrayList<String[]> results = informationNeedsParser(f);
		String[] identifiers = results.get(0);
		String[] needs = results.get(1);

		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
		QueryParser parser = new QueryParser(Version.LUCENE_44, field, analyzer);

		String line = queryString != null ? queryString : in.readLine();

		if (line == null || line.length() == -1) {
			// Exit
		} else {
			line = line.trim();
			if (line.length() != 0) {

				BooleanQuery query = new BooleanQuery();

				if (line.startsWith("spatial")) {
					Scanner s = new Scanner(line);
					String input = s.next();
					input = input.split(":")[1];
					System.out.println("input: " + input);

					Double west = new Double(Double.parseDouble(input.split(",")[0]));
					Double east = new Double(Double.parseDouble(input.split(",")[1]));
					Double south = new Double(Double.parseDouble(input.split(",")[2]));
					Double north = new Double(Double.parseDouble(input.split(",")[3]));

					// valor este de la caja de consulta
					// Xmin <= east
					NumericRangeQuery<Double> westRangeQuery = NumericRangeQuery.newDoubleRange("west", null, east,
							true, true);
					NumericRangeQuery<Double> eastRangeQuery = NumericRangeQuery.newDoubleRange("east", west, null,
							true, true);
					NumericRangeQuery<Double> southRangeQuery = NumericRangeQuery.newDoubleRange("south", null, north,
							true, true);
					NumericRangeQuery<Double> northRangeQuery = NumericRangeQuery.newDoubleRange("north", south, null,
							true, true);

					query.add(westRangeQuery, BooleanClause.Occur.SHOULD);
					query.add(eastRangeQuery, BooleanClause.Occur.SHOULD);
					query.add(southRangeQuery, BooleanClause.Occur.SHOULD);
					query.add(northRangeQuery, BooleanClause.Occur.SHOULD);

					if (s.hasNextLine()) {
						line = s.nextLine();
						System.out.println("line" + line);
		System.out.println("length: " + needs.length);
		for (int i=0; i<needs.length; i++) {
			
			List<String> result = new ArrayList<String>();
		    try {
		      TokenStream stream  = analyzer.tokenStream(null, needs[i]);
		      stream.reset();
		      while (stream.incrementToken()) {
		        result.add(stream.getAttribute(CharTermAttribute.class).toString());
		      }
		    } catch (IOException e) {
		      // not thrown b/c we're using a string reader...
		      throw new RuntimeException(e);
		    }
				
			
			System.out.println(result);
	
			/*if (line == null || line.length() == -1) {
				//Exit
			} else {
				line = line.trim();
				if (line.length() != 0) {
	
					BooleanQuery query = new BooleanQuery();
	
					if (line.startsWith("spatial")) {
						Scanner s = new Scanner(line);
						String input = s.next();
						input = input.split(":")[1];
						System.out.println("input: " + input);
	
						Double west = new Double(Double.parseDouble(input.split(",")[0]));
						Double east = new Double(Double.parseDouble(input.split(",")[1]));
						Double south = new Double(Double.parseDouble(input.split(",")[2]));
						Double north = new Double(Double.parseDouble(input.split(",")[3]));
	
						// valor este de la caja de consulta
						// Xmin <= east
						NumericRangeQuery<Double> westRangeQuery = NumericRangeQuery.newDoubleRange("west", null, east,
								true, true);
						NumericRangeQuery<Double> eastRangeQuery = NumericRangeQuery.newDoubleRange("east", west, null,
								true, true);
						NumericRangeQuery<Double> southRangeQuery = NumericRangeQuery.newDoubleRange("south", null, north,
								true, true);
						NumericRangeQuery<Double> northRangeQuery = NumericRangeQuery.newDoubleRange("north", south, null,
								true, true);
	
						query.add(westRangeQuery, BooleanClause.Occur.SHOULD);
						query.add(eastRangeQuery, BooleanClause.Occur.SHOULD);
						query.add(southRangeQuery, BooleanClause.Occur.SHOULD);
						query.add(northRangeQuery, BooleanClause.Occur.SHOULD);
	
						if (s.hasNextLine()) {
							line = s.nextLine();
							System.out.println("line" + line);
							Query queryStr = parser.parse(line);
							query.add(queryStr, BooleanClause.Occur.SHOULD);
						}
						s.close();
					} else {
						Query queryStr = parser.parse(line);
						query.add(queryStr, BooleanClause.Occur.SHOULD);
					}
					s.close();
				} else {
					Query queryStr = parser.parse(line);
					query.add(queryStr, BooleanClause.Occur.MUST);
				}

				System.out.println("Searching for: " + query.toString(field));

				if (repeat > 0) { // repeat & time as benchmark
					Date start = new Date();
					for (int i = 0; i < repeat; i++) {
						searcher.search(query, 100);
					}
					Date end = new Date();
					System.out.println("Time: " + (end.getTime() - start.getTime()) + "ms");
				}

				doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

				reader.close();
			}
		}
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
	/*
	 * public static void doPagingSearch(BufferedReader in, IndexSearcher
	 * searcher, Query query, int hitsPerPage, boolean raw, boolean interactive)
	 * throws IOException {
	 * 
	 * // Collect enough docs to show 5 pages TopDocs results =
	 * searcher.search(query, 5 * hitsPerPage); ScoreDoc[] hits =
	 * results.scoreDocs;
	 * 
	 * int numTotalHits = results.totalHits; System.out.println(numTotalHits +
	 * " total matching documents");
	 * 
	 * int start = 0; int end = Math.min(numTotalHits, hitsPerPage);
	 * 
	 * while (true) { if (end > hits.length) { System.out.println(
	 * "Only results 1 - " + hits.length + " of " + numTotalHits +
	 * " total matching documents collected."); System.out.println(
	 * "Collect more (y/n) ?"); String line = in.readLine(); if (line.length()
	 * == 0 || line.charAt(0) == 'n') { break; }
	 * 
	 * hits = searcher.search(query, numTotalHits).scoreDocs; }
	 * 
	 * end = Math.min(hits.length, start + hitsPerPage);
	 * 
	 * for (int i = start; i < end; i++) { if (raw) { // output raw format
	 * System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
	 * continue; }
	 * 
	 * Document doc = searcher.doc(hits[i].doc); String path = doc.get("path");
	 * if (path != null) { System.out.println((i + 1) + ". " + path); Date d =
	 * new Date(Long.parseLong(doc.get("modified"))); System.out.println(
	 * "   modified: " + d); System.out.println(searcher.explain(query,
	 * hits[i].doc)); } else { System.out.println((i + 1) + ". " +
	 * "No path for this document"); }
	 * 
	 * }
	 * 
	 * if (!interactive || end == 0) { break; }
	 * 
	 * if (numTotalHits >= end) { boolean quit = false; while (true) {
	 * System.out.print("Press "); if (start - hitsPerPage >= 0) {
	 * System.out.print("(p)revious page, "); } if (start + hitsPerPage <
	 * numTotalHits) { System.out.print("(n)ext page, "); } System.out.println(
	 * "(q)uit or enter number to jump to a page.");
	 * 
	 * String line = in.readLine(); if (line.length() == 0 || line.charAt(0) ==
	 * 'q') { quit = true; break; } if (line.charAt(0) == 'p') { start =
	 * Math.max(0, start - hitsPerPage); break; } else if (line.charAt(0) ==
	 * 'n') { if (start + hitsPerPage < numTotalHits) { start += hitsPerPage; }
	 * break; } else { int page = Integer.parseInt(line); if ((page - 1) *
	 * hitsPerPage < numTotalHits) { start = (page - 1) * hitsPerPage; break; }
	 * else { System.out.println("No such page"); } } } if (quit) break; end =
	 * Math.min(numTotalHits, start + hitsPerPage); } } }
	 */

	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage,
			boolean raw, boolean interactive) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		end = hits.length;

		for (int i = start; i < end; i++) {
			if (raw) { // output raw format
				System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
				continue;
			}

			Document doc = searcher.doc(hits[i].doc);
			String path = doc.get("path");
			if (path != null) {
				// System.out.println((i + 1) + ". " + path);
				System.out.print(
						path.split(Pattern.quote(File.separator))[path.split(Pattern.quote(File.separator)).length - 1]
								.substring(0, 2));
				if (i != end - 1)
					System.out.print(", ");
				else {
					System.out.println();
					System.out.println();
				}
			} else {
				// System.out.println((i + 1) + ". " + "No path for this
				// document");
			}

		}
	}

	public static void enterSpatialQuery(String input, BooleanQuery query) {
		Double west = new Double(Double.parseDouble(input.split(",")[0]));
		Double east = new Double(Double.parseDouble(input.split(",")[1]));
		Double south = new Double(Double.parseDouble(input.split(",")[2]));
		Double north = new Double(Double.parseDouble(input.split(",")[3]));

		// valor este de la caja de consulta
		// Xmin <= east
		NumericRangeQuery<Double> westRangeQuery = NumericRangeQuery.newDoubleRange("west", null, east, true, true);
		NumericRangeQuery<Double> eastRangeQuery = NumericRangeQuery.newDoubleRange("east", west, null, true, true);
		NumericRangeQuery<Double> southRangeQuery = NumericRangeQuery.newDoubleRange("south", null, north, true, true);
		NumericRangeQuery<Double> northRangeQuery = NumericRangeQuery.newDoubleRange("north", south, null, true, true);

		query.add(westRangeQuery, BooleanClause.Occur.MUST);
		query.add(eastRangeQuery, BooleanClause.Occur.MUST);
		query.add(southRangeQuery, BooleanClause.Occur.MUST);
		query.add(northRangeQuery, BooleanClause.Occur.MUST);
	}

	public static ArrayList<String[]> informationNeedsParser(File f)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		FileInputStream fis = new FileInputStream(f);
		InputSource is = new InputSource(fis);
		org.w3c.dom.Document doc = builder.parse(is);
		

		// Information need
		NodeList mList = doc.getElementsByTagName("informationNeed");
		String[] identifiers = new String[mList.getLength()];
		String[] needs = new String[mList.getLength()];
		
		for (int temp2 = 0; temp2 < mList.getLength(); temp2++) {
			Node mNode = mList.item(temp2);

			if (mNode.getNodeType() == Node.ELEMENT_NODE) {
				Element mElement = (Element) mNode;

				// Identifier for information need
				if (mElement.getElementsByTagName("identifier").item(0) != null) {
					String identifier = mElement.getElementsByTagName("identifier").item(0).getTextContent();
					identifiers[temp2] = identifier;
				}

				// Information need itself
				if (mElement.getElementsByTagName("text").item(0) != null) {
					String need = mElement.getElementsByTagName("text").item(0).getTextContent();
					needs[temp2] = need;
				}
			}
		}

		ArrayList<String[]> returned = new ArrayList<String[]>();
		returned.add(identifiers);
		returned.add(needs);
		return returned;
	}
}