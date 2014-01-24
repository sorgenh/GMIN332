package gmin332.modelunion;

public class Requetes {
	//Préfixes
		public static final String prefixes = "PREFIX db: <http://localhost:2020/>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"	PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				"	PREFIX map: <http://localhost:2020/#>"+
				"	PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"	PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"   PREFIX igeo: <http://rdf.insee.fr/def/geo#>"+
				"   PREFIX gmin332: <http://gmin332.fr/> "+
				"	PREFIX vocab: <file:////home/had/workspace/ProjetDonnéesComplexes/data/vocab/> "+
				"   PREFIX gn: <http://www.geonames.org/ontology#>";

		//Listes des résidences(neo4j) du calvados(d2rq) 
		public static final String query1 = "SELECT ?c ?nom WHERE { " +
				"[vocab:departement_nccenr \"Calvados\"] vocab:departement_departement ?d . " +
				"[vocab:cog_r_codeDep ?d] igeo:codeINSEE ?c ." +
				" ?r gmin332:APourCodePostal [igeo:codeINSEE ?c]." +
				" ?r gmin332:nomCommercial ?nom }";
		//10 communes (cog d2rq) avec le plus grand de nombre de tourismes (neo4j).
		public static final String query2 = "SELECT ?nom (count(?r) as ?nbResidences) WHERE {" +
				"?r gmin332:APourCodePostal [igeo:codeINSEE ?codeInsee]. " +
				"[igeo:codeINSEE ?codeInsee] vocab:cog_r_nccenr ?nom." +
				"}" +
				" GROUP BY ?nom ORDER BY DESC (?nbResidences) LIMIT 10";
		
		//10 communes (cog d2rq) avec le plus grand de nombre de residences tourismes (neo4j) associées a leur population(geonames). (probleme lien avec geonames)
				public static final String query3 = "SELECT ?nom (count(?r) as ?nbResidences) ?pop WHERE {" +
						"?r gmin332:APourCodePostal [igeo:codeINSEE ?codeInsee]." +
						" [igeo:codeINSEE ?codeInsee] vocab:cog_r_nccenr ?nom." +
						" [gn:name ?nom] gn:population ?pop}" +
						" GROUP BY ?nom ?pop ORDER BY DESC (?nbResidences) LIMIT 10";
		
		public static void afficherRequetesDisponibles() {
			// TODO Stub de la méthode généré automatiquement
			
		}

}
