/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

public class Persistencia2RDF {
	
	/**
	 * guarda un modelo de rdf tdb y lo car
	 */
	public static void main (String args[]) {
		
		//generamos un modelo de ejemplo
		Model model = CreacionRDF.generarEjemplo();
		
		//creamos un tdb (triplet data base) para almacenar el modelo 
		//(el directorio tiene existir)
		String directory = "DB1" ;
		Dataset tdb = TDBFactory.createDataset(directory) ;
		
		//añadimos el modelo y cerramos el tdb
		tdb.addNamedModel("http://uridelmodelo", model);
		tdb.close();
		
		//lo abrimos para lectura (igual que la creación)
		Dataset tdb2 = TDBFactory.createDataset(directory) ;
		
		//cargamos el modelo
		Model modelo2 = tdb2.getNamedModel("http://uridelmodelo");
		
		//lo mostramos por pantalla
		modelo2.write(System.out);
		
	}
	
}
