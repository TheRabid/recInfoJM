package prac3;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Conflicto {

	// Atributos privados
	private static ArrayList<QRel> q;
	private static ArrayList<Result> r;

	public static void main(String[] args) throws FileNotFoundException {

		/* Funcionamiento del programa */
		q = DataExtractor.getQRels("Prac3Files/qrels.txt");
		r = DataExtractor.getResultados("Prac3Files/results.txt");
		
		ArrayList<ConsultData> data = getData();
		
		for (ConsultData c:data) {
			System.out.println("Information need: " + c.getConsult());

			// Precision
			System.out.printf("Precision: %.2f%n", getPrecision(c));
			System.out.printf("Recall: %.2f%n", getRecall(c));
			System.out.printf("F1 Score: %.2f%n", getF1Score(c));
		}
	}
	
	
	private static ArrayList<ConsultData> getData() {
		
		ArrayList<ConsultData> data = new ArrayList<ConsultData>();
		data.add(new ConsultData(0));
		
		int numConsult = 0;
		
		for (int i=0; i<q.size(); ) {
			for (int j=0; j<r.size(); ) {
				if (q.get(i).getInformation_need() == r.get(j).getInformation_need()) {	// Misma consulta
					
					if (q.get(i).getDocument_id() == r.get(j).getDocument_id()) {	// Mismo documento
						
						if (q.get(i).getRelevancy() == 1) {						// True positive
							data.get(q.get(i).getInformation_need()-1).addTp();
						}else {													// False positive
							data.get(q.get(i).getInformation_need()-1).addFp();
						}
						
						i++; j++;
						
					}
					else if (q.get(i).getDocument_id() < r.get(j).getDocument_id()){
						if (q.get(i).getRelevancy() == 1) {						// False positive
							System.out.println("False positive");
							data.get(q.get(i).getInformation_need()-1).addFn();
						}
						else {													// True negative
							System.out.println("True negative");
							data.get(q.get(i).getInformation_need()-1).addTn();
						}
						i++;
					}
					else {
						j++;
					}
				}
				else if (q.get(i).getInformation_need() < r.get(j).getInformation_need()) {
					numConsult ++;
					data.add(new ConsultData(numConsult));
					if (q.get(i).getRelevancy() == 1) {						// False positive
						System.out.println("False positive");
						data.get(q.get(i).getInformation_need()-1).addFn();
					}
					else {													// True negative
						System.out.println("True negative");
						data.get(q.get(i).getInformation_need()-1).addTn();
					}
					i++;
				}
				else {
					numConsult ++;
					data.add(new ConsultData(numConsult));
					j++;
				}
				
			}
		}
		data.remove(data.size()-1);
		return data;
	}

	
	private static double getPrecision(ConsultData v) {
		return v.getTp() / ((double) (v.getTp() + v.getFp()));
	}

	private static double getRecall(ConsultData v) {
		return v.getTp() / ((double) (v.getTp() + v.getFn()));
	}

	private static double getF1Score(ConsultData v) {
		return 2 * ((getPrecision(v) * getRecall(v)) / (getPrecision(v) + getRecall(v)));
	}
}
