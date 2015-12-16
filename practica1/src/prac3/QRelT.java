package prac3;

public class QRelT {

	private String information_need;
	private String document_id;
	private int relevancy;

	public String getInformation_need() {
		return information_need;
	}

	public void setInformation_need(String information_need) {
		this.information_need = information_need;
	}

	public String getDocument_id() {
		return document_id;
	}

	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}

	public int getRelevancy() {
		return relevancy;
	}

	public void setRelevancy(int relevancy) {
		this.relevancy = relevancy;
	}

	public QRelT(String information_need, String document_id, int relevancy) {
		this.information_need = information_need;
		this.document_id = document_id;
		this.relevancy = relevancy;
	}

}
