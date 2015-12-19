package trabajo;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase QRelT define un tipo de objeto para almacenar informacion
 *         acerca de los juicios de relevancia dados un identificador de
 *         documento y una necesidad de informacion. El distintivo 'T' en su
 *         nombre lo diferencia de la clase QRel de la practica 3.
 */

public class QRelT {

	/* Atributos privados */
	private String information_need;
	private String document_id;
	private int relevancy;

	/**
	 * Metodo que devuelve la necesidad de informacion asociada a este juicio de
	 * relevancia
	 * 
	 * @return: Nombre de la necesidad de informacion
	 */
	public String getInformation_need() {
		return information_need;
	}

	/**
	 * Metodo que cambia la necesidad de informacion asociada a este juicio de
	 * relevancia
	 * 
	 * @param information_need:
	 *            La nueva necesidad de informacion
	 */
	public void setInformation_need(String information_need) {
		this.information_need = information_need;
	}

	/**
	 * Metodo que devuelve el id del documento asociado a este juicio de
	 * relevancia
	 * 
	 * @return: El id del documento
	 */
	public String getDocument_id() {
		return document_id;
	}

	/**
	 * Metodo que cambia el id del documento asociado a este juicio de
	 * relevancia
	 * 
	 * @param document_id:
	 *            El nuevo id del documento
	 */
	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}

	/**
	 * Metodo que devuelve 1 si para la necesidad de informacion y el documento
	 * de este juicio de relevancia el documento es relevante. 0 en caso
	 * contrario
	 * 
	 * @return: 0 o 1 segunla relevancia del documento
	 */
	public int getRelevancy() {
		return relevancy;
	}

	/**
	 * Metodo que cambia la relevancia de un documento frente a una necesidad de
	 * informacion de este juicio de relevancia
	 * 
	 * @param relevancy:
	 *            La nueva relevancia
	 */
	public void setRelevancy(int relevancy) {
		this.relevancy = relevancy;
	}

	/**
	 * Constructor
	 * 
	 * @param information_need:
	 *            nombre de la necesidad de informacion
	 * @param document_id:
	 *            id de la necesidad de informacion
	 * @param relevancy:
	 *            1 si es relevante. 0 en caso contrario
	 */
	public QRelT(String information_need, String document_id, int relevancy) {
		this.information_need = information_need;
		this.document_id = document_id;
		this.relevancy = relevancy;
	}

}
