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
	private static int infNeeds = 2;

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		/* Comprobar que se ha invocado bien al programa */
		String usage = "Uso de este programa:\tjava prac3.Evaluation -qrels <qRelsFileName>"
				+ " -results <resultsFileName> -output <outputFileName>";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

		/* Variables por defecto */
		String qrels = "Prac3Files/qrels.txt";
		String results = "Prac3Files/results.txt";
		String output = "Prac3Files/output.txt";

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

		for (int i = 1; i < infNeeds + 1; i++) {
			// Actualizar true y false positives y negatives
			Values v = updateNumbers(i);

			// Inf Need
			System.out.println("Information need: " + i);

			// Precision
			System.out.printf("Precision: %.2f%n", getPrecision(v));
			System.out.printf("Recall: %.2f%n", getRecall(v));
			System.out.printf("F1 Score: %.2f%n", getF1Score(v));
			
			Values newV = updateNumbersK(10, i);
			System.out.printf("Precision@10: %.2f%n", getPrecK(newV));
			System.out.println(v.getTP());
			System.out.println(v.getFP());
			System.out.println(v.getFN());
			System.out.println(v.getTN());
		}

	}

	private static Values updateNumbers(int infNeed) {
		int tp = 0;
		int fp = 0;
		int tn = 0;
		int fn = 0;
		for (QRel qrel : q) {
			if (infNeed == qrel.getInformation_need()) {
				boolean found = false;
				for (Result resul : r) {
					if (infNeed == resul.getInformation_need() && qrel.getDocument_id() == resul.getDocument_id()) {
						found = true;
						if (qrel.getRelevancy() == 1)
							tp++;
						else
							fp++;
					}
					if (found) {
						break;
					}
				}
				if (!found) {
					if (qrel.getRelevancy() == 1)
						fn++;
					else
						tn++;
				}
			}
		}

		return new Values(infNeed, tp, fp, tn, fn);
	}
	
	private static Values updateNumbersK(int k, int infNeed) {
		int tp = 0;
		int fp = 0;
		int tn = 0;
		int fn = 0;
		int cuenta = 0;
		for (QRel qrel : q) {
			if(cuenta > k){
				break;
			}
			if (infNeed == qrel.getInformation_need()) {
				boolean found = false;
				for (Result resul : r) {
					if (infNeed == resul.getInformation_need() && qrel.getDocument_id() == resul.getDocument_id()) {
						found = true;
						if (qrel.getRelevancy() == 1)
							tp++;
						else
							fp++;
					}
					if (found) {
						break;
					}
				}
				if (!found) {
					if (qrel.getRelevancy() == 1)
						fn++;
					else
						tn++;
				}
			}
			cuenta++;
		}

		return new Values(infNeed, tp, fp, tn, fn);
	}

	private static double getPrecision(Values v) {
		return v.getTP() / ((double) (v.getTP() + v.getFP()));
	}
	
	private static double getPrecK(Values v) {
		return v.getTP() / ((double) (v.getTP() + v.getFP()));
	}

	private static double getRecall(Values v) {
		return v.getTP() / ((double) (v.getTP() + v.getFN()));
	}

	private static double getF1Score(Values v) {
		return 2 * ((getPrecision(v) * getRecall(v)) / (getPrecision(v) + getRecall(v)));
	}
}
