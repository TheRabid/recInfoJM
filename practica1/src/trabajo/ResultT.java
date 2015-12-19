package trabajo;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase ResultT define un tipo de objeto para almacenar los
 *         resultados devueltos por el sistema de recuperacion de informacion
 *         tradicional desarrollado para el trabajo de la asignatura. El
 *         distintivo 'T' en su nombre lo diferencia de la clase Result de la
 *         practica 3.
 */

public class ResultT {

	/* Atributos privados */
	private String information_need;
	private String document_id;

	/**
	 * Constructor
	 * 
	 * @param information_need:
	 *            nombre de la necesidad de informacion
	 * @param document_id:
	 *            id del documento
	 */
	public ResultT(String information_need, String document_id) {
		this.information_need = information_need;
		this.document_id = document_id;
	}

	/**
	 * Metodo que devuelve la necesidad de informacion asociada a este resultado
	 * 
	 * @return: el nombre de la necesidad de informacion
	 */
	public String getInformation_need() {
		return information_need;
	}

	/**
	 * Metodo que cambia la necesidad de informacion asociada a este resultado
	 * 
	 * @param information_need:
	 *            la nueva necesidad de informacion
	 */
	public void setInformation_need(String information_need) {
		this.information_need = information_need;
	}

	/**
	 * Metodo que devuelve el id del documento asociado a este resultado
	 * 
	 * @return: el id del documento asociado
	 */
	public String getDocument_id() {
		return document_id;
	}

	/**
	 * Metodo que cambia el id del documento asociado a este resultado
	 * 
	 * @param document_id:
	 *            el nuevo id del documento
	 */
	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}

}
