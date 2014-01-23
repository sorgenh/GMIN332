package gmin332.cli;

import gmin332.modelunion.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map.Entry;

public class Appli {
	    private ArrayList<Entry<String,String>> requetes ;
	    private Union union;
	    
	    public Appli(){
	    	this.requetes= new ArrayList<Entry<String,String>>();
	    	this.union = new Union();
	    }
	public void run() {
		//PropertyConfigurator.configure("log4j.properties");
		
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query1,"Noms des communes associées à leur noms de départements"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query2,"Noms des communes en Languedoc Roussillon"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query3,"Nombre de communes par départements"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query4,"Noms des communes qui sont des chefs lieux de Régions"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query5,"Nom des localités soumises à l’ISF ainsi que le nom de leur département et de leur région d’appartenance"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query6,"Nom des localités dont le patrimoine moyen des gens payant l'ISF est supérieur a 2000000€ "));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query7,"Valeur ISF moyen par région"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query8,"Nombre de redevables a l'isf par département"));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.query9,"Insee des localités payant plus d'ISF que la moyenne nationale "));
		requetes.add(new AbstractMap.SimpleEntry<String,String>(Requetes.querytest,"Insee des localités payant plus d'ISF que la moyenne nationale "));
		Requetes.afficherRequetesDisponibles();
		System.out.println("\nEntrez le numéro de la requète à executer, ou 0 pour quitter");
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			System.out.println("\nEntrez le numéro de la requète à executer, ou 0 pour quitter");
			int n = sc.nextInt();
			if(n==0)
				break;
			System.out.println(requetes.get(n-1).getValue());
			String r = Requetes.prefixes +requetes.get(n-1).getKey();
			union.runQuery(System.out,Requetes.prefixes + r);
		}
		sc.close();

	}

}
