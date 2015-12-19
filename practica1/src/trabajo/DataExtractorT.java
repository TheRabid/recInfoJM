package trabajo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataExtractorT {

	public static HashMap<String, ArrayList<QRelT>> getQRels(String ficheroQRels) throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroQRels);
		Scanner s = new Scanner(f);
		HashMap<String, ArrayList<QRelT>> hreturned = new HashMap<String, ArrayList<QRelT>>();
		int fila = 1;

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				String infNeed = s.next();
				String docId = s.next();
				int rel = s.nextInt();
				if (rel == 1) {
					QRelT a = new QRelT(infNeed, docId, rel);
					if (!hreturned.containsKey(infNeed)) {
						hreturned.put(infNeed, new ArrayList<QRelT>());
					}
					hreturned.get(infNeed).add(a);
				}
				fila++;
			} catch (InputMismatchException e) {
				if (s.hasNextLine())
					s.nextLine();
				System.err.println("Fila " + fila + " del fichero qrels mal formada");
				fila++;
			} catch (NoSuchElementException e) {
				if (s.hasNextLine())
					s.nextLine();
				System.err.println("Fila " + fila + " del fichero qrels mal formada");
				fila++;
			}
		}
		s.close();
		return hreturned;
	}

	public static HashMap<String, ArrayList<ResultT>> getResultados(String ficheroResultados)
			throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroResultados);
		Scanner s = new Scanner(f);
		HashMap<String, ArrayList<ResultT>> hreturned = new HashMap<String, ArrayList<ResultT>>();
		int fila = 1;

		// Iterar hasta encontrar todos los resultados
		while (s.hasNextLine()) {
			try {
				String infNeed = s.next();
				String docId = s.next();
				ResultT a = new ResultT(infNeed, docId);
				if (!hreturned.containsKey(infNeed)) {
					hreturned.put(infNeed, new ArrayList<ResultT>());
				}
				if (hreturned.get(infNeed).size() < 50) {
					hreturned.get(infNeed).add(a);
				}
				fila++;
			} catch (InputMismatchException e) {
				if (s.hasNextLine())
					s.nextLine();
				System.err.println("Fila " + fila + " del fichero resultados mal formada");
				fila++;
			} catch (NoSuchElementException e) {
				if (s.hasNextLine())
					s.nextLine();
				System.err.println("Fila " + fila + " del fichero resultados mal formada");
				fila++;
			}
		}
		s.close();
		return hreturned;
	}

	public static ArrayList<String> getNeeds(String ficheroResultados) throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroResultados);
		Scanner s = new Scanner(f);
		ArrayList<String> returned = new ArrayList<String>();

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				String infNeed = s.next();
				if (!returned.contains(infNeed)) {
					returned.add(infNeed);
				}
				s.nextLine();
			} catch (InputMismatchException e) {
				if (s.hasNextLine())
					s.nextLine();
			} catch (NoSuchElementException e) {
				if (s.hasNextLine())
					s.nextLine();
			}
		}
		s.close();
		return returned;
	}
}
