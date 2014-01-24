package gmin332.modelunion;

import gmin332.neo4j.Neo4jConstructor;

import java.io.OutputStream;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class Union {
	private static final String INSEEOnto = "data/insee-geo-onto.ttl";
	private static final String d2RQttl = "data/mappingD2RQ.ttl";
	private static final String geonamesFile1 = "data/geonames_v3.rdf";
	private static final String geonamesFile2 = "data/geonames.rdf";
	private static final String pathTDB = "data/BaseTDB";
	private static final String nameTDB = "geonames";
	private static final String pathNeo4J = "data/graph.db";
	

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
    	unionModel = unionModel.union(neo4jModel);
    	FileManager.get().readModel(unionModel, INSEEOnto);
    	mapping(unionModel);
    	unionModel = ModelFactory.createRDFSModel(unionModel);
    	
    }
	private void mapping(Model unionModel2) {	
		String prefInsee = unionModel2.getNsPrefixURI("igeo");
		unionModel2.add(this.neo4jModel.getProperty("http://gmin332.fr/codePostal"),
				        RDFS.subPropertyOf,
				        unionModel2.getResource(prefInsee + "codeINSEE"));
		String gn = "http://www.geonames.org/ontology#";
		String vocab = unionModel2.getNsPrefixURI("vocab");

		unionModel2.add(this.tdbModel.getProperty(gn+"name"),
		        RDFS.subPropertyOf,
		        unionModel2.getResource(vocab+"cog_r_nccenr"));
	}

	private Model construireTDBModel() {
        //Dataset geoDataset = TDBFactory.createDataset(pathTDB) ;
    	Model m = ModelFactory.createDefaultModel();
    	FileManager.get().readModel(m, geonamesFile1);
    	FileManager.get().readModel(m, geonamesFile2);
    	
    	//geoDataset.close();
		return m;
	}
	private Model construireD2RQModel() {
		ModelD2RQ m = new ModelD2RQ("file:"+d2RQttl);
		return m;
	}
	public void runQuery(OutputStream os, String query) {
		Query q = QueryFactory.create(query); 
		ResultSet rs = QueryExecutionFactory.create(q, unionModel).execSelect();
		ResultSetFormatter.out(os,rs, q.getPrefixMapping());
	}

}
