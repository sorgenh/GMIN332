package gmin332.modelunion;

import gmin332.neo4j.Neo4jConstructor;

import java.io.OutputStream;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class Union {
	private static final String ttlFile = "src/gmin332/d2rq/mapping.ttl";
	private static final String geonamesFile = "/home/had/Données complexes/data/geonames_v3.rdf";
	private static final String pathTDB = "/home/had/Données complexes/data/BaseTDB";
	private static final String nameTDB = "geonames";
	private static final String pathNeo4J = "/home/had/Données complexes/data/graph.db";
	

    private Model d2rqModel;
    private Model tdbModel;
    private Model hbaseModel;
    private Model neo4jModel;
    private Model unionModel;
    public Union(){
    	this.d2rqModel = construireD2RQModel();
    	this.tdbModel = construireTDBModel();
    	this.neo4jModel = new Neo4jConstructor(pathNeo4J).getModel();
    	this.unionModel = ModelFactory.createOntologyModel();
    	unionModel = unionModel.union(d2rqModel);
    	unionModel = unionModel.union(tdbModel);
//    	unionModel = unionModel.union(hbaseModel);
    	unionModel = unionModel.union(neo4jModel);
    	mapping(unionModel);
    	
    }
	private void mapping(Model unionModel2) {
		// TODO Stub de la méthode généré automatiquement
		
	}

	private Model construireTDBModel() {
        Dataset geoDataset = TDBFactory.createDataset(pathTDB) ;
    	Model m = geoDataset.getNamedModel(nameTDB);
    	geoDataset.close();
		return m;
	}
	private Model construireD2RQModel() {
		ModelD2RQ m = new ModelD2RQ("file:"+ttlFile);
		return m;
	}
	public void runQuery(OutputStream os, String query) {
		Query q = QueryFactory.create(query); 
		ResultSet rs = QueryExecutionFactory.create(q, unionModel).execSelect();
		ResultSetFormatter.out(os,rs, q.getPrefixMapping());
	}

}
