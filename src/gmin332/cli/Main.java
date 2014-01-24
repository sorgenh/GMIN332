package gmin332.cli;

import gmin332.tdb.TDBEngine;

public class Main {
	public static void main(String[] argv) {
		
		//TDBEngine.chargerTDB("/home/had/Données complexes/data/geonames_v3.rdf", "/home/had/Données complexes/data/BaseTDB","geonames");
		
		Appli a =  new Appli();
		a.run();
	}
}
