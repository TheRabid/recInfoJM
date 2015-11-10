package prac3;

public class ConsultData {

	private int tp, fp, fn, tn;
	private int consult;

	public ConsultData(int consult) {
		this.consult = consult;
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
	
	

}
