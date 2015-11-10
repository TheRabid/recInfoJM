package prac3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataExtractor {

	public static ArrayList<QRel> getQRels(String ficheroQRels) throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroQRels);
		Scanner s = new Scanner(f);
		ArrayList<QRel> returned = new ArrayList<QRel>();

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				int infNeed = s.nextInt();
				int docId = s.nextInt();
				int rel = s.nextInt();
				QRel a = new QRel(infNeed, docId, rel);
				returned.add(a);
			} catch (InputMismatchException e) {
				System.err.println("Fichero qrels mal formado");
			} catch (NoSuchElementException e) {
				System.err.println("Fichero qrels mal formado");
			}
		}
		s.close();
		return returned;
	}

	public static ArrayList<Result> getResultados(String ficheroResultados) throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroResultados);
		Scanner s = new Scanner(f);
		ArrayList<Result> returned = new ArrayList<Result>();

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				int infNeed = s.nextInt();
				int docId = s.nextInt();
				Result a = new Result(infNeed, docId);
				returned.add(a);
			} catch (InputMismatchException e) {
				System.err.println("Fichero results mal formado");
			}
		}
		s.close();
		return returned;
	}
}
