package prac3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class EvaluationT {

	// Atributos privados
	private static ArrayList<QRelT> q;
	private static ArrayList<ResultT> r;

	private static final int K = 10;

	public static void main(String[] args) throws FileNotFoundException {

		/* Funcionamiento del programa */
		q = DataExtractorT.getQRels("Prac3Files/qrels.txt");
		r = DataExtractorT.getResultados("Prac3Files/results.txt");

		ArrayList<ConsultDataT> data = getData();

		System.out.println();
		for (ConsultDataT c : data) {
			System.out.println("Information need: " + c.getConsult());

			// Precision
			System.out.printf("Precision: %.3f%n", getPrecision(c));
			System.out.printf("Recall: %.3f%n", getRecall(c));
			System.out.printf("F1 Score: %.3f%n", getF1Score(c));
			System.out.printf("Precision@%d: %.3f%n", K, getPrecision(c, K));
			System.out.printf("Mean Average Precision: %.3f%n", getMeanAveragePrecision(c));

			/*
			 * System.out.println("Tp " + c.getTp()); System.out.println("Fp " +
			 * c.getFp()); System.out.println("Tn " + c.getTn());
			 * System.out.println("Fn " + c.getFn());
			 */

			ArrayList<RecPrecPoint> rpp = c.getRec_prec_points();
			System.out.println("recall_precision");

			for (int i = 0; i < K && i < rpp.size(); i++) {
				RecPrecPoint p = rpp.get(i);
				System.out.printf("%.3f\t%.3f%n", p.getRecall(), p.getPrecision());
			}

			System.out.println("interpolated_recall_precision");
			double[] interpolated = getInterpolatedRecallPrecision(c);
			double r = 0.0;
			for (double d : interpolated) {
				System.out.printf("%.3f\t%.3f%n", r, d);
				r += 0.1;
			}

			System.out.println();
			System.out.println("**************************************");
			System.out.println();
		}

		printConslusion(data);
	}

	private static ArrayList<ConsultDataT> getData() {

		HashMap<String, ConsultDataT> data = new HashMap<String, ConsultDataT>();
		ArrayList<RecPrecPoint> rpp = new ArrayList<RecPrecPoint>();

		int numDocs = 1;
		int numRight = 1;
		String actualDoc = "";
		boolean found = false;
		ArrayList<String> needs = new ArrayList<String>();

		// Searching for true positives & false positives
		for (int i = 0; i < r.size();) {
			if (data.size() == 0 || !r.get(i).getInformation_need().equals(actualDoc)) {
				if (data.containsKey(actualDoc)) {
					data.get(actualDoc).setRec_prec_points(rpp);
				}
				actualDoc = r.get(i).getInformation_need();
				needs.add(actualDoc);
				data.put(actualDoc, (new ConsultDataT(actualDoc)));
				rpp = new ArrayList<RecPrecPoint>();
				numDocs = 1;
				numRight = 1;
			}

			for (int j = 0; j < q.size();) {

				if (r.get(i).getInformation_need().equals(q.get(j).getInformation_need())
						&& r.get(i).getDocument_id().equals(q.get(j).getDocument_id())) {

					if (q.get(j).getRelevancy() == 1) { // True positive
						rpp.add(new RecPrecPoint(numRight, (double) numRight / numDocs));

						numRight++;
						data.get(actualDoc).addTp();
					} else { // False positive
						data.get(actualDoc).addFp();
					}

					r.remove(i);
					q.remove(j);

					found = true;
					break;

				} else {
					j++;
				}

			}

			numDocs++;

			if (found) {
				found = false;
			} else {
				i++;
			}
		}

		data.get(actualDoc).setRec_prec_points(rpp);
		rpp = new ArrayList<RecPrecPoint>();

		for (QRelT rq : q) {
			if (rq.getRelevancy() == 1) { // False negative
				rpp.add(new RecPrecPoint(numRight, (double) numRight / numDocs));

				numRight++;
				data.get(rq.getInformation_need()).addFn();
			} else { // True negative
				data.get(rq.getInformation_need()).addTn();
			}
		}

		ArrayList<ConsultDataT> result = new ArrayList<ConsultDataT>();
		for (String cd : needs) {
			ArrayList<RecPrecPoint> points = data.get(cd).getRec_prec_points();

			for (RecPrecPoint p : points) {
				p.setRecall(p.getRecall() / (data.get(cd).getTp() + data.get(cd).getFn()));
			}
			result.add(data.get(cd));
		}

		return result;
	}

	private static double getPrecision(ConsultDataT v) {
		return v.getTp() / ((double) (v.getTp() + v.getFp()));
	}

	private static double getPrecision(ConsultDataT v, int k) {
		if (v.getSizeOfConsultData() >= 10) {
			return v.getTp(k) / ((double) (v.getTp(k) + v.getFp(k)));
		} else {
			double r = v.getTp(k) / ((double) (v.getTp(k) + v.getFp(k)));
			return r / 10.0;
		}

	}

	private static double getRecall(ConsultDataT v) {
		return v.getTp() / ((double) (v.getTp() + v.getFn()));
	}

	private static double getF1Score(ConsultDataT v) {
		return 2 * ((getPrecision(v) * getRecall(v)) / (getPrecision(v) + getRecall(v)));
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
					if (rec <= i + 0.1) {
						if (prec > max) {
							max = prec;
						}
					} else {
						break;
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

	private static void printConslusion(ArrayList<ConsultDataT> data) {
		System.out.println("TOTAL");

		double precision = 0, recall = 0, f1 = 0, prec10 = 0, MAP = 0;

		double[] interpolated = new double[11];

		for (ConsultDataT cd : data) {
			precision += getPrecision(cd);
			recall += getRecall(cd);
			f1 += getF1Score(cd);
			prec10 += getPrecision(cd, K);
			MAP += getMeanAveragePrecision(cd);

			double[] inter = getInterpolatedRecallPrecision(cd);
			for (int i = 0; i < inter.length; i++) {
				interpolated[i] += inter[i];
			}
		}

		int numConsults = data.size();

		System.out.printf("Precision: %.3f%n", precision / numConsults);
		System.out.printf("Recall: %.3f%n", recall / numConsults);
		System.out.printf("F1 Score: %.3f%n", f1 / numConsults);
		System.out.printf("Precision@%d: %.3f%n", K, prec10 / numConsults);
		System.out.printf("Mean Average Precision: %.3f%n", MAP / numConsults);
		System.out.println("interpolated_recall_precision");
		double r = 0.0;
		for (double d : interpolated) {
			System.out.printf("%.3f\t%.3f%n", r, d / numConsults);
			r += 0.1;
		}
	}
}
