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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

public class CreacionRDF {

	/**
	 * muestra un modelo de jena de ejemplo por pantalla
	 */
	public static void main(String args[]) {
		Model model = CreacionRDF.generarEjemplo();
		// write the model in the standar output
		model.write(System.out);
	}

	/**
	 * Genera un modelo de jena de ejemplo
	 */
	@SuppressWarnings("unused")
	public static Model generarEjemplo() {
		// definiciones
		String personURI = "http://somewhere/JohnSmith";
		String givenName = "John";
		String familyName = "Smith";
		String fullName = givenName + " " + familyName;

		String person2URI = "http://somewhere/MikeJohnson";
		String givenName2 = "Mike";
		String familyName2 = "Johnson";
		String fullName2 = givenName2 + " " + familyName2;

		String person3URI = "http://somewhere/AliceMcTetis";
		String givenName3 = "Alice";
		String familyName3 = "McTetis";
		String fullName3 = givenName3 + " " + familyName3;

		String propertyURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
		String knowsURI = "http://xmlns.com/foaf/0.1/knows";

		String casaURI = "http://casa/Tarradellas";
		String numHabitaciones = "4";
		String comida = "fuet";
		String tamano = "Mu grande";

		// crea un modelo vacio
		Model model = ModelFactory.createDefaultModel();
		String URI = "http://recinfo.com/";
		model.setNsPrefix("recinfo", URI);
		String personaURI = "http://www.recinfo.com/Persona";
		Property propNom = model.createProperty("http://recinfo.com/Nombre");
		Property type = model.createProperty(propertyURI);
		String am = "Alberto Mostro";
		String jr = "Jaime Ruiz-Borau";
		System.out.println(VCARD.FN.toString());

		Resource lacasta = model.createResource("http://www.recinfo.com/" + "lacasta").addProperty(VCARD.FN,
				"La castaza");
		Resource albmos = model.createResource("http://www.recinfo.com/" + am).addProperty(propNom, am).addProperty(type, personaURI);
		Resource jairui = model.createResource("http://www.recinfo.com/" + jr).addProperty(propNom, jr);

		// Property prop = model.createProperty(propertyURI);
		// Resource persona1 =
		// model.createResource("http://xmlns.com/foaf/0.1/person");
		//
		// // le añade las propiedades
		// Resource johnSmith = model.createResource(personURI)
		// .addProperty(VCARD.FN, fullName)
		// .addProperty(VCARD.N,
		// model.createResource()
		// .addProperty(VCARD.Given, givenName)
		// .addProperty(VCARD.Family, familyName));
		//
		// johnSmith.addProperty(prop, persona1);
		//
		//
		// Resource mikeJohnson = model.createResource(person2URI)
		// .addProperty(VCARD.FN, fullName2)
		// .addProperty(VCARD.N,
		// model.createResource()
		// .addProperty(VCARD.Given, givenName2)
		// .addProperty(VCARD.Family, familyName2));
		//
		// Resource aliceMcTetis = model.createResource(person3URI)
		// .addProperty(VCARD.FN, fullName3)
		// .addProperty(VCARD.N,
		// model.createResource()
		// .addProperty(VCARD.Given, givenName3)
		// .addProperty(VCARD.Family, familyName3));
		//
		//
		// Resource relacion =
		// model.createResource("http://xmlns.com/foaf/0.1/knows")
		// .addProperty(VCARD.FN, mikeJohnson)
		// .addProperty(VCARD.FN, aliceMcTetis);
		//
		//
		// Resource tarradellas = model.createResource(casaURI)
		// .addProperty(VCARD.FN, numHabitaciones)
		// .addProperty(VCARD.FN, tamano)
		// .addProperty(VCARD.FN, comida);

		return model;
	}

}
