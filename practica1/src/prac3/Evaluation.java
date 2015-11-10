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
	private static int tp = 0;
	private static int fp = 0;
	private static int fn = 0;
	private static int tn = 0;
	private static int infNeeds = 2;

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
		
		for(int i = 1; i<infNeeds+1;i++){
			updateNumbers(i);
			System.out.println(getPrecision(i));
		}

	}

	private static void updateNumbers(int infNeed) {
		tp=0;
		fp=0;
		tn=0;
		fn=0;
		for (QRel qrel : q) {
			if (infNeed == qrel.getInformation_need()) {
				boolean found = false;
				for (Result resul : r) {
					if (infNeed == resul.getInformation_need() && qrel.getDocument_id() == resul.getDocument_id()) {
						found = true;
						if(qrel.getRelevancy()==1)
							tp++;
						else
							fp++;
					}
					if(found){
						break;
					}
				}
				if(!found){
					if(qrel.getRelevancy()==1)
						fn++;
					else
						tn++;
				}
			}
		}
	}

	private static double getPrecision(int infNeed) {
		// Calcular total recuperados
		int total = 0;
		for (Result res : r) {
			if (res.getInformation_need() == infNeed) {
				total++;
			}
		}

		// Calcular los recuperados y relevantes
		return (double) (tp/total);
	}
}
