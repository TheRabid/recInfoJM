package prac4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestParser {
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		String path = "dumps/dump";
		ArrayList<Par> list = parseDump(path);
		
		for (Par p:list) {
			System.out.println(p.docName + " - " + p.content);
		}
		
		System.out.println("Lenght : " + list.size());

//		numLines(path);
	}
	
	
	private static void numLines (String path) throws FileNotFoundException {
		File f = new File(path);
		Scanner s = new Scanner(f, "UTF-8");
		int numLines = 0;
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
			numLines++;
		}
		
		System.out.println("Num lines -> " + numLines);
	}
	private static ArrayList<Par> parseDump2(String path) throws FileNotFoundException {

		File f = new File(path);
		Scanner s = new Scanner(f, "UTF-8");
		boolean nameFound = false;
		boolean contentFound = false;
		ArrayList<Par> returned = new ArrayList<Par>();
		
		String url = "";
		String content = "";
		
		while (s.hasNextLine()) {
			String line = s.nextLine();
			
			if (line.startsWith("url:")) {
				s.next();
				url = s.next();
			}
			else if (line.startsWith("content:")) {
				content = s.nextLine();
			}
			
			if (!url.equals("") && !url.equals("")) {
				returned.add(new Par(url, content));
				url = "";
				content = "";
			}
		}
		
		return returned;
	}
	
	
	private static ArrayList<Par> parseDump(String path) throws FileNotFoundException {
		/* Initialize objects */
		File f = new File(path);
		Scanner s = new Scanner(f, "UTF-8");
		boolean nameFound = false;
		boolean contentFound = false;
		Par temp = new Par(null, null);
		ArrayList<Par> returned = new ArrayList<Par>();

		while (s.hasNextLine()) {
			String line = s.nextLine();
			int index = line.indexOf(':');

			if (index > 0) {
				/* Name found */
				if (line.substring(0, index).equals("url")) {
					// Get the name
					int index2 = line.lastIndexOf('/');
					String name = line.substring(index2);
					temp.docName = name;
					nameFound = true;
				}

				/* Content found */
				if (line.substring(0, index).equals("Content") && line.length() - 1 == index) {
					String newLine = s.nextLine();
					if (!newLine.startsWith("<?xml")) {
						// Esto no es
						temp = new Par(null, null);
						nameFound = false;
					} else {
						temp.content = newLine;
						contentFound = true;
					}
				}
			}
			/* If both found add to arraylist */
			if (nameFound && contentFound) {
				nameFound = false;
				contentFound = false;
				returned.add(temp);
				temp = new Par(null, null);
			}
		}
		s.close();
		return returned;
	}

}
