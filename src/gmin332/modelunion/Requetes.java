package gmin332.modelunion;

public class Requetes {
	//Préfixes
		public static final String prefixes = "PREFIX db: <http://localhost:2020/>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"	PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				"	PREFIX map: <http://localhost:2020/#>"+
				"	PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"	PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"	PREFIX vocab: <file:////home/had/workspace/ProjetDonnéesComplexes/src/gmin332/d2rq/vocab/> ";

		//Noms des communes associées à leur noms de départements
		public static final String query1 = 
				"SELECT ?nomCommune ?nomdepartement WHERE { " +
						"				[ vocab:cog_r_nccenr ?nomCommune ] vocab:cog_r_codeDep ?codeDepartement ." +
						"				[ vocab:departement_departement ?codeDepartement ] vocab:departement_nccenr ?nomdepartement." +
						"					} ";
		//Noms des communes en Languedoc Roussillon
		public static final String query2 = 
				"SELECT ?nomCommune WHERE { " +
				"				[vocab:cog_r_nccenr ?nomCommune] vocab:cog_r_codeReg ?codeRegion." +
						"		[vocab:region_region ?codeRegion] vocab:region_nccenr \"Languedoc-Roussillon\"." +
						"					} ";
		//Nombre de communes par départements
		public static final String query3 = 
				"SELECT (count(?c) as ?nbCommunes) ?nomdepartement WHERE { ?c vocab:cog_r_codeDep ?codeDepartement ." +
						"				  [vocab:departement_departement ?codeDepartement] vocab:departement_nccenr ?nomdepartement." +
						"					} " +
						"GROUP BY ?nomdepartement "+
						"ORDER BY DESC(?nbCommunes) ";
		//Noms des communes qui sont des chefs lieux de Régions
		public static final String query4 = 
				"SELECT ?nomCommune ?nomregion WHERE {[vocab:region_CHEFLIEU ?codeCommune] vocab:region_nccenr ?nomregion." +
						"				  [vocab:cog_r_codeInsee ?codeCommune] vocab:cog_r_nccenr ?nomCommune." +
						"					} " ;
		//nom des localités soumises à l’ISF ainsi que le nom de leur département et de leur région
		//d’appartenance
		public static final String query5 =
				"SELECT distinct ?nomCommune ?nomdepartement ?nomregion WHERE { " +
						//Commmunes
						"				{ []  vocab:impot_codeInsee ?codeInsee ." +
						"				  ?s  vocab:localite_codeInsee ?codeInsee."+
						"				  ?s  vocab:localite_TYPElocalite \"commune\"."+
						"				  ?s2 vocab:cog_r_codeInsee ?codeInsee ." +
						"				  ?s2 vocab:cog_r_nccenr ?nomCommune." +
						"				  ?s2 vocab:cog_r_codeDep ?codeDepartement." +
						"				  ?s3 vocab:departement_departement ?codeDepartement ." +
						"				  ?s3 vocab:departement_nccenr ?nomdepartement." +
						"				  ?s2 vocab:cog_r_codeReg ?codeRegion ." +
						"				  ?s4 vocab:region_region ?codeRegion ." +
						"				  ?s4 vocab:region_nccenr ?nomregion. }" +
						"UNION" +
						//Arrondissements
						"              { []  vocab:impot_codeInsee ?codeInsee ." +
						"				  ?ss vocab:localite_codeInsee ?codeInsee." +
						"				  ?ss vocab:localite_TYPElocalite \"arrondissement_municipal\"."+
						"				  ?ss2 vocab:arrondissement_municipal_codeInsee ?codeInsee."+
						"				  ?ss2 vocab:arrondissement_municipal_narm ?nomCommune."+
						"				  ?ss2 vocab:arrondissement_municipal_codeCommune ?codeCommune."+
						"				  ?ss3 vocab:cog_r_codeInsee ?codeCommune ." +
						"				  ?ss3 vocab:cog_r_codeDep ?codeDepartement." +
						"				  ?ss4 vocab:departement_departement ?codeDepartement ." +
						"				  ?ss4 vocab:departement_nccenr ?nomdepartement." +
						"				  ?ss3 vocab:cog_r_codeReg ?codeRegion ." +
						"				  ?ss5 vocab:region_region ?codeRegion ." +
						"				  ?ss5 vocab:region_nccenr ?nomregion. }" +
						"					} " ;


		//Nom des localités dont le patrimoine moyen des gens payant l'ISF est supérieur a 2000000€ 
		public static final String query6 = 
				"SELECT ?codeInsee ?nomlocalite ?patrimoinemoyen "
						+ "WHERE { "
						+ "  [vocab:impot_codeInsee ?codeInsee] vocab:impot_patrimoinem ?patrimoinemoyen . "
						+ "  { "
						+ "    [vocab:cog_r_codeInsee ?codeInseeCommune] vocab:cog_r_nccenr ?nomlocalite. "
						+ "  } "
						+ " UNION "
						+ "  { "
						+ "    [ vocab:arrondissement_municipal_codeInsee ?codeInseeArrondissement] vocab:arrondissement_municipal_narm ?nomlocalite. "
						+ "  } . "
						+ " FILTER ((?patrimoinemoyen > 2000000) && ((?codeInsee = ?codeInseeCommune) || (?codeInsee = ?codeInseeArrondissement))) "
						+ "}";

		//Valeur ISF moyen par région (hors arrondissements)
		public static final String query7sansarr = 
				"SELECT ?nomregion (avg(?impot) as ?impotmoyen) " +
						"WHERE { " +
						"       [vocab:impot_codeInsee ?codeInsee] vocab:impot_impotmoyen ?impot." +
						"		[vocab:cog_r_codeInsee ?codeInsee] vocab:cog_r_codeReg ?codeReg." +
						"       [vocab:region_region ?codeReg] vocab:region_nccenr ?nomregion."+
						"}" +
						"GROUP BY ?nomregion " +
						"ORDER BY DESC(?impotmoyen)";
						

		//Valeur ISF moyen par région 
		public static final String query7 = 
				"SELECT ?nomregion (avg(?impot) as ?impotmoyen) " +
						"WHERE { " 
						+ "  { "
						+ "    [vocab:impot_codeInsee ?codeInsee] vocab:impot_impotmoyen ?impot . "
						+ "    [vocab:cog_r_codeInsee ?codeInsee] vocab:cog_r_codeReg ?codeReg. "
						+ "    [vocab:region_region ?codeReg] vocab:region_nccenr ?nomregion."
						+ "  } "
						+ " UNION "
						+ "  { "
						+ "  [vocab:impot_codeInsee ?codeInsee] vocab:impot_impotmoyen ?impot . "
						+ "    [vocab:arrondissement_municipal_codeInsee ?codeInsee] vocab:arrondissement_municipal_codeCommune ?codeCommune." 
						+ "    [vocab:cog_r_codeInsee ?codeCommune] vocab:cog_r_codeReg ?codeReg."
						+ "    [vocab:region_region ?codeReg] vocab:region_nccenr ?nomregion."
						+ "  } . "
						+    "}" 
						+ "GROUP BY ?nomregion " 
						+ "ORDER BY DESC(?impotmoyen)";

		//Nombre de redevables a l'isf par département en 2008
		public static final String query8 = 
				"SELECT ?nomdepartement (sum(?nbRedevables) as ?nbRedevablesTot) " 
						+"WHERE { " 
						+ "  { "
						+"       [vocab:impot_codeInsee ?codeInsee] vocab:impot_nbreredevable ?nbRedevables;" 
						+"											vocab:impot_annee 2008." 
						+"       [vocab:cog_r_codeInsee ?codeInsee] vocab:cog_r_codeDep ?codeDep." 
						+"       [vocab:departement_departement ?codeDep] vocab:departement_nccenr ?nomdepartement."
						+ "  } "
						+ " UNION "
						+ "  { "
						+"       [vocab:impot_codeInsee ?codeInsee] vocab:impot_nbreredevable ?nbRedevables;" 
						+"											vocab:impot_annee 2008." 
						+ "      [vocab:arrondissement_municipal_codeInsee ?codeInsee] vocab:arrondissement_municipal_codeCommune ?codeCommune." 
						+ "      [vocab:cog_r_codeInsee ?codeCommune] vocab:cog_r_codeDep ?codeDep."					
						+"       [vocab:departement_departement ?codeDep] vocab:departement_nccenr ?nomdepartement."
						+ "	 }" 
					    + "}"
						+"GROUP BY ?nomdepartement " 
						+"ORDER BY DESC(?nbRedevablesTot)";

		
		 // insee des localités payant plus d'ISF que la moyenne nationale 
		public static final String query9 =
				"SELECT ?codeInsee  "
			  + "WHERE "
			  + "{ "
	          + "  { "
			  + "    SELECT (AVG(?i) AS ?moyenneNationale) "
			  + "    WHERE "
			  + "    { "
			  + "      [] vocab:impot_impotmoyen ?i . "
			  + "    } "
			  + "  } "
	          + "  [vocab:impot_impotmoyen ?impot] vocab:impot_codeInsee  ?codeInsee ."
	          + " FILTER (?impot > ?moyenneNationale)"
			  + "} ";
		public static final String querytest = 
				"SELECT * WHERE { ?s ?r ?o }" ;

		public static void afficherRequetesDisponibles() {
			// TODO Stub de la méthode généré automatiquement
			
		}

}
