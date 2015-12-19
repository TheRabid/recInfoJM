package trabajo;

import java.util.ArrayList;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         Clase que contiene toda la información asociada 
 *         a una consulta.
 */

public class ConsultDataT {

	private int tp, fp, fn, tn;		// Valores asociados una consulta
	private String consult;			// Nombre de la consulta

	private ArrayList<RecPrecPoint> rec_prec_points;	// Valores de la grafica recall_precision
	private ArrayList<String> kresults;

	public ConsultDataT(String consult) {
		this.consult = consult;
		this.kresults = new ArrayList<String>();
		this.rec_prec_points = new ArrayList<RecPrecPoint>();
	}

	public void addTp() {
		tp++;
		kresults.add("tp");
	}

	public void addFp() {
		fp++;
		kresults.add("fp");
	}

	public void addFn() {
		fn++;
		kresults.add("fn");
	}

	public void addTn() {
		tn++;
		kresults.add("tn");
	}

	public int getTp() {
		return tp;
	}

	public int getFp() {
		return fp;
	}

	public int getFn() {
		return fn;
	}

	public int getTn() {
		return tn;
	}

	public int getTp(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("tp")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	public int getFp(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("fp")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	public int getFn(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("fn")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	public int getTn(int k) {
		int cuenta = 0;
		for (int i = 0; i < k; i++) {
			if (kresults.get(i).equals("tn")) {
				cuenta++;
			}
		}
		return cuenta;
	}

	public String getConsult() {
		return consult;
	}

	public ArrayList<RecPrecPoint> getRec_prec_points() {
		return rec_prec_points;
	}

	public void setRec_prec_points(ArrayList<RecPrecPoint> rec_prec_points) {
		this.rec_prec_points = rec_prec_points;
	}

	public int getSizeOfConsultData() {
		return kresults.size();
	}

}
