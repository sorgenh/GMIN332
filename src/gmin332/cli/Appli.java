package gmin332.cli;

import gmin332.modelunion.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map.Entry;

public class Appli {
	    private ArrayList<MQuery> requetes ;
	    private Union union;
	    
	    public Appli(){
	    	this.requetes= new ArrayList<MQuery>();
	    	this.union = new Union();
	    }
	public void run() {
		//PropertyConfigurator.configure("log4j.properties");
		
		requetes.add(new MQuery(Requetes.query1,"Listes des résidences(neo4j) du calvados(d2rq) "));
		requetes.add(new MQuery(Requetes.query2,"10 communes (cog d2rq) avec le plus grand de nombre de tourismes (neo4j)."));
		requetes.add(new MQuery(Requetes.query3,"10 communes (cog d2rq) avec le plus grand de nombre de residences tourismes (neo4j) associées a leur population(geonames). (probleme lien avec geonames)"));

		Requetes.afficherRequetesDisponibles();
		System.out.println("\nEntrez le numéro de la requète à executer, ou 0 pour quitter");
		Scanner sc = new Scanner(System.in);
		afficherListeRequetes();
		while(sc.hasNext()){
			System.out.println("\nEntrez le numéro de la requète à executer, ou 0 pour quitter");
			int n = sc.nextInt();
			if(n==0)
				break;
			System.out.println(requetes.get(n-1).getDescription());
			String r = Requetes.prefixes +requetes.get(n-1).getQueryText();
			union.runQuery(System.out,Requetes.prefixes + r);
//			String s = sc.nextLine();
//			union.runQuery(System.out,Requetes.prefixes + s);
		}
		sc.close();

	}
	private void afficherListeRequetes() {
		System.out.println("Liste des requêtes:");
		int i=1;
		for(MQuery r : requetes){
			System.out.println(i + ": "+r.getDescription());
		    i++;
		}
		
	}

}
