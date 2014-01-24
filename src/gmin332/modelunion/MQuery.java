package gmin332.modelunion;

import java.io.PrintStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class MQuery
{

	private String description;
	
	private String queryText;
	
	private Query query;
	
	public MQuery(String queryText, String description)
	{
		this.queryText = queryText;
		this.description = description;
		this.query = null;
	}

	public String getDescription()
	{
		return this.description;
	}

	public String getQueryText()
	{
		return this.queryText;
	}
	
	public ResultSet execute(Model model)
	{
		if (this.query == null)
		{
			this.query = QueryFactory.create(this.queryText);
		}
		
		return QueryExecutionFactory.create(this.query, model).execSelect();
	}
	
	public ResultSet executeAndPrint(ModelD2RQ model, PrintStream stream)
	{
		ResultSet rset = this.execute(model);
		ResultSetFormatter.out(stream, rset, this.query.getPrefixMapping());
		return rset;
	}

}
