package trabajo;

import java.util.ArrayList;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase ConsultDataT define un tipo de objeto para almacenar
 *         informacion acerca de las relaciones entre los aciertos y los fallos
 *         para una consulta y unos resultados del sistema de recuperacion de
 *         informacion que se quiere evaluar. El distintivo 'T' en su nombre lo
 *         diferencia de la clase ConsultData de la practica 3.
 */

public class ConsultDataT {

	/* Atributos privados */
	private int tp, fp, fn, tn;
	private String consult;
	private ArrayList<RecPrecPoint> rec_prec_points;
	private ArrayList<String> kresults;

	/**
	 * Constructor
	 * 
	 * @param consult:
	 *            el identificador de la necesidad de informacion
	 */
	public ConsultDataT(String consult) {
		this.consult = consult;
		this.kresults = new ArrayList<String>();
		this.rec_prec_points = new ArrayList<RecPrecPoint>();
	}

	/**
	 * Metodo que suma 1 al total de True Positives de esta consulta
	 */
	public void addTp() {
		tp++;
		kresults.add("tp");
	}

	/**
	 * Metodo que suma 1 al total de False Positives de esta consulta
	 */
	public void addFp() {
		fp++;
		kresults.add("fp");
	}

	/**
	 * Metodo que suma 1 al total de False Negatives de esta consulta
	 */
	public void addFn() {
		fn++;
		kresults.add("fn");
	}

	/**
	 * Metodo que suma 1 al total de True Negatives de esta consulta
	 */
	public void addTn() {
		tn++;
		kresults.add("tn");
	}

	/**
	 * Metodo que devuelve los True Positives de esta consulta
	 * 
	 * @return: true positives de esta consulta
	 */
	public int getTp() {
		return tp;
	}

	/**
	 * Metodo que devuelve los False Positives de esta consulta
	 * 
	 * @return: false positives de esta consulta
	 */
	public int getFp() {
		return fp;
	}

	/**
	 * Metodo que devuelve los False Negatives de esta consulta
	 * 
	 * @return: false negatives de esta consulta
	 */
	public int getFn() {
		return fn;
	}

	/**
	 * Metodo que devuelve los True Negatives de esta consulta
	 * 
	 * @return: true negatives de esta consulta
	 */
	public int getTn() {
		return tn;
	}

	/**
	 * Metodo que devuelve los True Positives de esta consulta para el numero k
	 * de resultados pasados por parametro
	 * 
	 * @param k:
	 *            entero con los k resultados pedidos
	 * @return: true positives de esta consulta
	 */
	public int getTp(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("tp")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	/**
	 * Metodo que devuelve los False Positives de esta consulta para el numero k
	 * de resultados pasados por parametro
	 * 
	 * @param k:
	 *            entero con los k resultados pedidos
	 * @return: false positives de esta consulta
	 */
	public int getFp(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("fp")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	/**
	 * Metodo que devuelve los False Negatives de esta consulta para el numero k
	 * de resultados pasados por parametro
	 * 
	 * @param k:
	 *            entero con los k resultados pedidos
	 * @return: false negatives de esta consulta
	 */
	public int getFn(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("fn")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	/**
	 * Metodo que devuelve los True Negatives de esta consulta para el numero k
	 * de resultados pasados por parametro
	 * 
	 * @param k:
	 *            entero con los k resultados pedidos
	 * @return: true negatives de esta consulta
	 */
	public int getTn(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("tn")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	/**
	 * Metodo que devuelve el nombre de la consulta
	 * 
	 * @return el nombre de la consulta
	 */
	public String getConsult() {
		return consult;
	}

	/**
	 * Metodo que devuelve los puntos de precision y recall de esta consulta
	 * 
	 * @return un arraylist con los puntos de precision/recall de esta consulta
	 */
	public ArrayList<RecPrecPoint> getRec_prec_points() {
		return rec_prec_points;
	}

	/**
	 * Metodo que modifica los puntos de precision y recall de esta consulta
	 * 
	 * @param rec_prec_points:
	 *            los nuevos puntos de precision y recall de esta consulta
	 */
	public void setRec_prec_points(ArrayList<RecPrecPoint> rec_prec_points) {
		this.rec_prec_points = rec_prec_points;
	}

	/**
	 * Metodo que devuelve el total de true positives, false positives, true
	 * negatives y false negatives de esta consulta
	 * 
	 * @return el total de true positives, false positives, true negatives y
	 *         false negatives de la consulta
	 */
	public int getSizeOfConsultData() {
		return kresults.size();
	}

}
