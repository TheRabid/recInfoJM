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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

public class PersistenciaRDF {
	
	/**
	 * guarda un modelo de rdf en un triplestore, lo carga y lo visualiza
	 * en diferentes formatos
	 */
	public static void main (String args[]) {
		
		//generamos un modelo de ejemplo
		Model model = CreacionRDF.generarEjemplo();
		
		//lo guardamos en un fichero rdf
		try {
			model.write(new FileOutputStream(new File("nombre.rdf")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//cargamos el fichero recien guardado   
	    Model model2 =  FileManager.get().loadModel("nombre.rdf");
	    
	    //lo mostramos por pantalla en diferentes formatos de RDF
	    System.out.println("------------------------------");
	    model2.write(System.out,"N-TRIPLE"); 
	    System.out.println("------------------------------");
	    model2.write(System.out,"TURTLE"); 
	    
	}
	
}
