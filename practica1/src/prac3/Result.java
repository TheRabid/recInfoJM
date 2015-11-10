package prac3;

public class Result {

	private int information_need;
	private int document_id;

	public Result(int information_need, int document_id) {
		this.information_need = information_need;
		this.document_id = document_id;
	}

	public int getInformation_need() {
		return information_need;
	}

	public void setInformation_need(int information_need) {
		this.information_need = information_need;
	}

	public int getDocument_id() {
		return document_id;
	}

	public void setDocument_id(int document_id) {
		this.document_id = document_id;
	}

}
