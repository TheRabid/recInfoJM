package prac3;

import java.util.ArrayList;

public class ConsultData {

	private int tp, fp, fn, tn;
	private int consult;
	
	private ArrayList<RecPrecPoint> rec_prec_points;

	public ConsultData(int consult) {
		this.consult = consult;
		this.rec_prec_points = new ArrayList<RecPrecPoint>();
	}

	public void addTp() {
		tp++;
	}

	public void addFp() {
		fp++;
	}

	public void addFn() {
		fn++;
	}

	public void addTn() {
		tn++;
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

	public int getConsult() {
		return consult;
	}

	public void setConsult(int consult) {
		this.consult = consult;
	}

	public ArrayList<RecPrecPoint> getRec_prec_points() {
		return rec_prec_points;
	}

	public void setRec_prec_points(ArrayList<RecPrecPoint> rec_prec_points) {
		this.rec_prec_points = rec_prec_points;
	}
	
	

}
