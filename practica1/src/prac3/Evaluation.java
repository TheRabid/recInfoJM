package prac3;

import java.util.ArrayList;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase Evaluation hace [RELLENAR ESTO CUANDO TAL]
 */

public class Evaluation {

	// Atributos privados
	private static ArrayList<QRel> q;
	private static ArrayList<Result> r;
	
	public static void main(String[] args) throws Exception {
		/* Comprobar que se ha invocado bien al programa */
		String usage = "Uso de este programa:\tjava prac3.Evaluation -qrels <qRelsFileName>"
				+ " -results <resultsFileName> -output <outputFileName>";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

		/* Variables por defecto */
		String qrels = "qrels.txt";
		String results = "results.txt";
		String output = "output.txt";

		/* Busqueda de los parametros del programa */
		for (int i = 0; i < args.length; i++) {
			if ("-qrels".equals(args[i])) {
				qrels = args[i + 1];
				i++;
			} else if ("-results".equals(args[i])) {
				results = args[i + 1];
				i++;
			} else if ("-output".equals(args[i])) {
				output = args[i + 1];
				i++;
			}
		}
		
		/* Funcionamiento del programa */
		q = DataExtractor.getQRels(qrels);
		r = DataExtractor.getResultados(results);
		
		
	}
	
	private static double getPrecision(int infNeed){
		
		return 0;
	}
}
