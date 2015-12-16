package prac3;

import java.util.ArrayList;

public class ConsultDataT {

	private int tp, fp, fn, tn;
	private String consult;

	private ArrayList<RecPrecPoint> rec_prec_points;
	private ArrayList<String> kresults;

	public ConsultDataT(String consult) {
		this.consult = consult;
		this.kresults = new ArrayList<String>();
		this.rec_prec_points = new ArrayList<RecPrecPoint>();
	}

	public void addTp() {
		if (kresults.size() < 50) {
			tp++;
			kresults.add("tp");
		}
	}

	public void addFp() {
		if (kresults.size() < 50) {
			fp++;
			kresults.add("fp");
		}
	}

	public void addFn() {
		if (kresults.size() < 50) {
			fn++;
			kresults.add("fn");
		}
	}

	public void addTn() {
		if (kresults.size() < 50) {
			tn++;
			kresults.add("tn");
		}
	}

	public int getTp() {
		return tp;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public int getFp() {
		return fp;
	}

	public void setFp(int fp) {
		this.fp = fp;
	}

	public int getFn() {
		return fn;
	}

	public void setFn(int fn) {
		this.fn = fn;
	}

	public int getTn() {
		return tn;
	}

	public void setTn(int tn) {
		this.tn = tn;
	}

	public String getConsult() {
		return consult;
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

	public void setConsult(String consult) {
		this.consult = consult;
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
