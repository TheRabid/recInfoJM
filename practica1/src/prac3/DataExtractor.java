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
		int fila = 1;

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				int infNeed = s.nextInt();
				int docId = s.nextInt();
				int rel = s.nextInt();
				QRel a = new QRel(infNeed, docId, rel);
				returned.add(a);
				fila++;
			} catch (InputMismatchException e) {
				if(s.hasNextLine())
					s.nextLine();
				System.err.println("Fila "+fila+" del fichero qrels mal formada");
				fila++;
			} catch (NoSuchElementException e) {
				if(s.hasNextLine())
					s.nextLine();
				System.err.println("Fila "+fila+" del fichero qrels mal formada");
				fila++;
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
		int fila = 1;

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				int infNeed = s.nextInt();
				int docId = s.nextInt();
				Result a = new Result(infNeed, docId);
				returned.add(a);
				fila++;
			} catch (InputMismatchException e) {
				if(s.hasNextLine())
					s.nextLine();
				System.err.println("Fila "+fila+" del fichero qrels mal formada");
				fila++;
			} catch (NoSuchElementException e) {
				if(s.hasNextLine())
					s.nextLine();
				System.err.println("Fila "+fila+" del fichero qrels mal formada");
				fila++;
			}
		}
		s.close();
		return returned;
	}
}
