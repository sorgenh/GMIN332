
	package gmin332.tdb;

	import java.util.Iterator;

	import com.hp.hpl.jena.query.Dataset;
	import com.hp.hpl.jena.rdf.model.Model;
	import com.hp.hpl.jena.tdb.TDBFactory;
	import com.hp.hpl.jena.util.FileManager;

	public class TDBEngine {
		public static final void chargerTDB(String pathFichier,String pathDossier,String nomBase){
			Dataset ds = TDBFactory.createDataset(pathDossier) ;
	        
	        Model model = ds.getNamedModel(nomBase);    
	        
	        FileManager.get().readModel( model, pathFichier );

	        Iterator<String> graphNames = ds.listNames();
	        
	        while (graphNames.hasNext()) 
	        {
	            String graphName = graphNames.next();       
	            Model m = ds.getNamedModel(graphName);
	            System.out.println("Nom du graphe " + graphName + " taille: " + m.size());
	       	}  	      
	        
	        ds.close();
		}
	}


