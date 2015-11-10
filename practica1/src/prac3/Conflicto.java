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
		
		System.out.println(q.size() + "  " + r.size());
		
		ConsultData[] data = getData();
		double precision1 = (double) data[0].getTp()/(data[0].getTp() + data[0].getFp());
		
		System.out.println(data[0].getTp() + "  " + data[0].getFp() + "  " + data[0].getFn() + "  " + data[0].getTn());
		System.out.println(precision1);
		
		double precision2 = (double) data[1].getTp()/(data[1].getTp() + data[1].getFp());
		System.out.println(precision2);
	}
	
	
	private static ConsultData[] getData() {
		
		ConsultData[] data = new ConsultData[2];
		data[0] = new ConsultData(1);
		data[1] = new ConsultData(2);
		
		for (int i=0; i<q.size(); ) {
			for (int j=0; j<r.size(); ) {
				if (q.get(i).getInformation_need() == r.get(j).getInformation_need()) {	// Misma consulta
					if (q.get(i).getDocument_id() == r.get(j).getDocument_id()) {	// Mismo documento
						
						if (q.get(i).getRelevancy() == 1) {						// True positive
							data[q.get(i).getInformation_need()-1].addTp();
						}else {													// False positive
							data[q.get(i).getInformation_need()-1].addFp();
						}
						
						i++; j++;
						
					}
					else if (q.get(i).getDocument_id() < r.get(j).getDocument_id()){
						if (q.get(i).getRelevancy() == 1) {						// False positive
							System.out.println("False positive");
							data[q.get(i).getInformation_need()-1].addFn();
						}
						else {													// True negative
							System.out.println("True negative");
							data[q.get(i).getInformation_need()-1].addTn();
						}
						i++;
					}
					else {
						j++;
					}
				}
				else if (q.get(i).getInformation_need() < r.get(j).getInformation_need()) {
					if (q.get(i).getRelevancy() == 1) {						// False positive
						System.out.println("False positive");
						data[q.get(i).getInformation_need()-1].addFn();
					}
					else {													// True negative
						System.out.println("True negative");
						data[q.get(i).getInformation_need()-1].addTn();
					}
					i++;
				}
				else {
					j++;
				}
				
			}
		}
		return data;
	}

}
