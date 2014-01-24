package gmin332.neo4j;


//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//
//public class Neo4jConstructor implements IConstructor
//{
//    public Neo4jConstructor(String serverURI)
//    {
//        WebResource resource = Client.create().resource(serverURI);
//        ClientResponse response = resource.get(ClientResponse.class);
//         
//        System.out.println(String.format("GET on [%s], status code [%d]", serverURI, response.getStatus()));
//        response.close();
//    }
//    
//}

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class Neo4jConstructor
{
    static public final String prefURL = "http://gmin332.fr";
    
	static public final String prefixes =
			"PREFIX gmin332: <http://gmin332.fr/> " +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
			"PREFIX igeo: <http://rdf.insee.fr/def/geo#> ";
	
    private GraphDatabaseService graphDB;
    private Model model;
    private ExecutionEngine engine;
    private Hashtable<String, Property> properties;
    
    public Neo4jConstructor(String dbPath) 
    {
    	
        if (! this.check(dbPath))
        {
            System.out.println("Neo4j database directory does not exist");
        }
        
        this.properties = new Hashtable<String, Property>();
        
        this.graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
        Neo4jConstructor.registerShutdownHook(graphDB);      
        
        this.model = ModelFactory.createDefaultModel();
        
        this.engine = new ExecutionEngine(this.graphDB);
        
        this.constructModel();
        
        this.graphDB.shutdown();
    }
    
    protected boolean check(String path)
    {
        return new File(path).exists();
    }
    
    public Model getModel()
    {
        return this.model;
    }
    
    public Property getProperty(String key)
    {
    	Property property;
    	
    	if (this.properties.containsKey(key))
    	{
    		property = this.properties.get(key);
    	}
    	else
    	{
    		property = this.model.createProperty(prefURL + "/" + key);
    		this.properties.put(key, property);
    	}
    	
    	return property;
    }
    
    protected void constructModel()
    {
    	this.loadResidences();
    	this.loadCodePostaux();
    	this.loadRelationships();
    	
//    	try
//    	{
//			FileOutputStream fos = new FileOutputStream("out.n3");
//			this.model.write(fos, "N3");
//		} catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
    }
    
    protected void loadResidences()
    {
        try (Transaction tx = this.graphDB.beginTx())
        {
        	ExecutionResult result = this.engine.execute("MATCH (rt:ResidenceTourisme) RETURN rt");
        	
        	Iterator<Node> column = result.columnAs("rt");
        	for (Node node : IteratorUtil.asIterable(column))
        	{
        		Resource resource = this.model.createResource(prefURL + "/" + "residenceTourisme" + node.getId());
        		
                for (String propertyKey : node.getPropertyKeys())
                {
                	if (propertyKey.equals("codePostal"))
                	{
                		continue;
                	}
                	
                	String propertyValue = (String) node.getProperty(propertyKey);
                	
                	if (propertyValue.equals("-"))
                	{
                		continue;
                	}
                	
                	Property property = this.getProperty(propertyKey);
                	
                	resource.addProperty(property, propertyValue);
                }
        	}
        	
        	tx.success();
        }  	
    }
    
    protected void loadCodePostaux()
    {
        try (Transaction tx = this.graphDB.beginTx())
        {
        	ExecutionResult result = this.engine.execute("MATCH (cp:CodePostal) RETURN cp");
        	Property property = this.getProperty("codePostal");
        	
        	Iterator<Node> column = result.columnAs("cp");
        	for (Node node : IteratorUtil.asIterable(column))
        	{
        		Resource resource = this.model.createResource(prefURL + "/" + "codePostal" + node.getId());
        		String propertyValue = (String) node.getProperty("codePostal");
        		
        		resource.addProperty(property, propertyValue);
        	}
        	
        	tx.success();
        }  	
    }
    
    protected void loadRelationships()
    {
        try (Transaction tx = this.graphDB.beginTx())
        {
        	ExecutionResult result = this.engine.execute("MATCH ()-[r:APourCodePostal]->() RETURN r");
        	
        	Iterator<Relationship> column = result.columnAs("r");
        	for (Relationship relation : IteratorUtil.asIterable(column))
        	{
        		Node start = relation.getStartNode();
        		Node end = relation.getEndNode();
        		
        		Resource subject = this.model.getResource(prefURL + "/" + "residenceTourisme" + start.getId());
        		Resource object = this.model.getResource(prefURL + "/" + "codePostal" + end.getId());
        		Property property = this.getProperty("APourCodePostal");
        		
        		this.model.add(subject, property, object);
        	}
        	
        	tx.success(); 
        }
    }
    
    private static void registerShutdownHook(final GraphDatabaseService graphDb)
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    
}


