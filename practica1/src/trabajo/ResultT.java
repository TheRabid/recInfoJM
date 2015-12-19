package trabajo;

public class ResultT {

	private String information_need;
	private String document_id;

	public ResultT(String information_need, String document_id) {
		this.information_need = information_need;
		this.document_id = document_id;
	}

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

}
