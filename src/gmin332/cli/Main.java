package gmin332.cli;

import gmin332.tdb.TDBEngine;

public class Main {
	public static void main(String[] argv) {
	//TDBEngine.chargerTDB("data/geonames_v3.rdf", "data/BaseTDB","geonames");
		//TDBEngine.chargerTDB("data/geonames.rdf", "data/BaseTDB","geonames");
		Appli a =  new Appli();
		a.run();
	}
}
