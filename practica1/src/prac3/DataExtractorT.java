package prac3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataExtractorT {

	public static ArrayList<QRelT> getQRels(String ficheroQRels) throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroQRels);
		Scanner s = new Scanner(f);
		ArrayList<QRelT> returned = new ArrayList<QRelT>();
		int fila = 1;

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				String infNeed = s.next();
				String docId = s.next();
				int rel = s.nextInt();
				QRelT a = new QRelT(infNeed, docId, rel);
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

	public static ArrayList<ResultT> getResultados(String ficheroResultados) throws FileNotFoundException {
		// Crear fichero, scanner y ArrayList
		File f = new File(ficheroResultados);
		Scanner s = new Scanner(f);
		ArrayList<ResultT> returned = new ArrayList<ResultT>();
		int fila = 1;

		// Iterar hasta encontrar todos los juicios de relevancia
		while (s.hasNextLine()) {
			try {
				String infNeed = s.next();
				String docId = s.next();
				ResultT a = new ResultT(infNeed, docId);
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
