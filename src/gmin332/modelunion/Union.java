package gmin332.modelunion;

import java.io.OutputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class Union {
	private static final String ttlFile = "src/gmin332/d2rq/mapping.ttl";

    private Model d2rqModel;
    private Model tdbModel;
    private Model hbaseModel;
    private Model neo4jModel;
    private Model unionModel;
    public Union(){
    	this.d2rqModel = construireD2RQModel();
    	this.tdbModel = construireTDBModel();
    	this.hbaseModel = construireHBASEModel();
    	this.neo4jModel = construireNEO4JModel();
    	this.unionModel = ModelFactory.createOntologyModel();
    	unionModel = unionModel.union(d2rqModel);
//    	unionModel = unionModel.union(tdbModel);
//    	unionModel = unionModel.union(hbaseModel);
//    	unionModel = unionModel.union(neo4jModel);
    	
    }
	private Model construireNEO4JModel() {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}
	private Model construireHBASEModel() {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}
	private Model construireTDBModel() {
		// TODO Stub de la méthode généré automatiquement
		return null;
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
