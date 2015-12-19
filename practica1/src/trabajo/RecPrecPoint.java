package trabajo;

/**
 * @author Alberto Sabater Bailon (546297)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 *         La clase RecPrecPoint contiene el recall y la precision
 *         asociados a una cosulta, para el calculo de la grafica
 */

public class RecPrecPoint {
	
	double recall, precision;

	public RecPrecPoint(double recall, double precision) {
		this.recall = recall;
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}
	
	
	
	

}
