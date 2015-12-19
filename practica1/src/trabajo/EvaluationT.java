package trabajo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase EvaluationT contiene metodos para evaluar el sistema de
 *         recuperacion de informacion desarrollado para el trabajo de la
 *         asignatura. El distintivo 'T' en su nombre lo diferencia de la clase
 *         Evaluation de la practica 3.
 */

public class EvaluationT {

	/* Atributos privados */

	// Hashmap con los juicios de relevancia para cada documento y consulta
	private static HashMap<String, ArrayList<QRelT>> q;

	// Hashmap con los resultados devueltos por el buscador para cada documento
	// y consulta
	private static HashMap<String, ArrayList<ResultT>> r;

	// ArrayList de String con los nombres de las consultas
	private static ArrayList<String> n;

	// Entero para la precision@k
	private static final int K = 10;

	// Strings por defecto para los ficheros
	private static final String DEFAULTQRELS = "defaultQrels";
	private static final String DEFAULTRESULTS = "defaultResults";
	private static final String DEFAULTOUTPUT = "defaultOutput";

	/**
	 * Metodo main de la clase EvaluationT. Uso de este programa: java
	 * trabajo.EvaluationT -qrels <qrelsFileName> -results
	 * <resultsFileName> -output <outputFileName>
	 */
	public static void main(String[] args) throws FileNotFoundException {
		/* Extraccion de parametros */
		String qrels = DEFAULTQRELS;
		String results = DEFAULTRESULTS;
		String output = DEFAULTOUTPUT;
		
		for (int i = 0; i < args.length; i++) {
			if (i != args.length - 1) {
				if (args[i].equals("-qrels")) {
					qrels = args[i + 1];
				}

				if (args[i].equals("-results")) {
					results = args[i + 1];
				}

				if (args[i].equals("-output")) {
					output = args[i + 1];
				}
			}
		}

		/* Funcionamiento principal del programa */

		/* Extraccion de informacion de los ficheros */
		q = DataExtractorT.getQRels(qrels);
		r = DataExtractorT.getResultados(results);
		n = DataExtractorT.getNeeds(results);
		ArrayList<ConsultDataT> data = getData();

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(output, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		/* Bucle principal de iteracion */
		for (ConsultDataT c : data) {

			/* Muestra la informacion de cada consulta */
			writer.println("Information need: " + c.getConsult());
			writer.printf("Precision: %.3f%n", getPrecision(c));
			writer.printf("Recall: %.3f%n", getRecall(c));
			writer.printf("F1 Score: %.3f%n", getF1Score(c));
			writer.printf("Precision@%d: %.3f%n", K, getPrecision(c, K));
			writer.printf("Mean Average Precision: %.3f%n", getMeanAveragePrecision(c));

			ArrayList<RecPrecPoint> rpp = c.getRec_prec_points();
			writer.println("recall_precision");

			for (int i = 0; i < K && i < rpp.size(); i++) {
				RecPrecPoint p = rpp.get(i);
				writer.printf("%.3f\t%.3f%n", p.getRecall(), p.getPrecision());
			}

			writer.println("interpolated_recall_precision");
			double[] interpolated = getInterpolatedRecallPrecision(c);
			double r = 0.0;
			for (double d : interpolated) {
				writer.printf("%.3f\t%.3f%n", r, d);
				r += 0.1;
			}

			writer.println();
			writer.println("**************************************");
			writer.println();
		}

		printConclusion(data, writer);
		writer.close();
	}

	private static ArrayList<ConsultDataT> getData() {
		/* Variable a devolver */
		ArrayList<ConsultDataT> data = new ArrayList<ConsultDataT>();

		/*
		 * Bucle de iteracion principal a lo largo de las necesidades de
		 * informacion
		 */
		for (String strong : n) {
			/* Variables */
			ArrayList<QRelT> qrel = q.get(strong);
			ArrayList<ResultT> resu = r.get(strong);
			ConsultDataT c = new ConsultDataT(strong);
			ArrayList<RecPrecPoint> rpp = new ArrayList<RecPrecPoint>();

			/* Bucle de iteracion principal a lo largo de los resultados */
			for (ResultT re : resu) {
				/* Variables para saber si hemos encontrado la QRel */
				boolean found = false;
				int toRemove = -1;
				for (int i = 0; i < qrel.size() && !found; i++) {
					QRelT qr = qrel.get(i);
					if (re.getDocument_id().equals(qr.getDocument_id())) {
						/* True positive */
						found = true;
						c.addTp();
						toRemove = i;
						rpp.add(new RecPrecPoint(c.getTp(), getPrecision(c)));
					}
				}
				if (found) {
					qrel.remove(toRemove);
				} else {
					/* False positive */
					c.addFp();
				}
			}

			/* Bucle para sumar falsos negativos */
			for (int i = 0; i < qrel.size(); i++) {
				c.addFn();
			}

			/* Introducir ConsultData en data */
			c.setRec_prec_points(rpp);
			data.add(c);
		}

		/* Bucle para arreglar recalls */
		for (ConsultDataT cd : data) {
			ArrayList<RecPrecPoint> points = cd.getRec_prec_points();

			for (RecPrecPoint p : points) {
				p.setRecall(p.getRecall() / (cd.getTp() + cd.getFn()));
			}
		}

		return data;
	}

	private static double getPrecision(ConsultDataT v) {
		return v.getTp() / ((double) (v.getTp() + v.getFp()));
	}

	private static double getPrecision(ConsultDataT v, int k) {
		return v.getTp(k) / (double) k;
	}

	private static double getRecall(ConsultDataT v) {
		return v.getTp() / ((double) (v.getTp() + v.getFn()));
	}

	private static double getF1Score(ConsultDataT v) {
		return 2 * ((getPrecision(v) * getRecall(v)) / (getPrecision(v) + getRecall(v)));
	}

	private static double getF1Score(double prec, double reca) {
		return 2 * (prec * reca) / (prec + reca);
	}

	private static double getMeanAveragePrecision(ConsultDataT c) {
		if (c.getSizeOfConsultData() == 0) {
			return 0;
		} else {
			ArrayList<RecPrecPoint> rpp = c.getRec_prec_points();

			double avg = 0.0;

			for (RecPrecPoint p : rpp) {
				avg += p.getPrecision();
			}

			return avg / rpp.size();
		}

	}

	private static double[] getInterpolatedRecallPrecision(ConsultDataT c) {

		ArrayList<RecPrecPoint> rpp = c.getRec_prec_points();

		double[] interpolatedRP = new double[11];
		int index = 0;
		for (double i = 0.0; i <= 1; i += 0.1) {
			double max = 0.0;
			for (int j = 0; j < rpp.size(); j++) {
				RecPrecPoint p = rpp.get(j);
				double prec = p.getPrecision();
				double rec = p.getRecall();
				if (rec >= i) {
					if (prec > max) {
						max = prec;
					}
				}
			}

			interpolatedRP[index] = max;
			index++;
		}

		double aux = 0.0;
		for (int i = interpolatedRP.length - 1; i >= 0; i--) {
			if (interpolatedRP[i] == 0) {
				interpolatedRP[i] = aux;
			} else {
				aux = interpolatedRP[i];
			}
		}

		return interpolatedRP;
	}

	private static void printConclusion(ArrayList<ConsultDataT> data, PrintWriter writer) {
		writer.println("TOTAL");

		double precision = 0, recall = 0, prec10 = 0, MAP = 0;

		double[] interpolated = new double[11];

		for (ConsultDataT cd : data) {
			precision += getPrecision(cd);
			recall += getRecall(cd);
			prec10 += getPrecision(cd, K);
			MAP += getMeanAveragePrecision(cd);

			double[] inter = getInterpolatedRecallPrecision(cd);
			for (int i = 0; i < inter.length; i++) {
				interpolated[i] += inter[i];
			}
		}

		int numConsults = data.size();

		writer.printf("Precision: %.3f%n", precision / numConsults);
		writer.printf("Recall: %.3f%n", recall / numConsults);
		writer.printf("F1 Score: %.3f%n", getF1Score(precision, recall) / numConsults);
		writer.printf("Precision@%d: %.3f%n", K, prec10 / numConsults);
		writer.printf("Mean Average Precision: %.3f%n", MAP / numConsults);
		writer.println("interpolated_recall_precision");
		double r = 0.0;
		for (double d : interpolated) {
			writer.printf("%.3f\t%.3f%n", r, d / numConsults);
			r += 0.1;
		}
	}
}
