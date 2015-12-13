package prac3;

public class Values {

	/* Atributos privados */
	private int infNeed;
	private int truePositives;
	private int falsePositives;
	private int trueNegatives;
	private int falseNegatives;
	
	public Values(int infNeed, int tp, int fp, int tn, int fn){
		this.setInfNeed(infNeed);
		truePositives = tp;
		falsePositives = fp;
		trueNegatives = tn;
		falseNegatives = fn;
	}

	public int getInfNeed() {
		return infNeed;
	}

	public void setInfNeed(int infNeed) {
		this.infNeed = infNeed;
	}

	public int getTP() {
		return truePositives;
	}

	public void setTP(int truePositives) {
		this.truePositives = truePositives;
	}

	public int getFP() {
		return falsePositives;
	}

	public void setFP(int falsePositives) {
		this.falsePositives = falsePositives;
	}

	public int getTN() {
		return trueNegatives;
	}

	public void setTN(int trueNegatives) {
		this.trueNegatives = trueNegatives;
	}

	public int getFN() {
		return falseNegatives;
	}

	public void setFN(int falseNegatives) {
		this.falseNegatives = falseNegatives;
	}
}
