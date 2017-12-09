package owl.cs.analysis.metrics.harvest.oa3;

import java.io.File;

import owl.cs.analysis.utilities.OntologyAnalysis;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String url = args[0];
		File ontology = new File(args[1]);
		File export = new File(args[2],"metrics.harvest.oa3"+ontology.getName()+".rdf");
		
		OntologyAnalysis oa = new OntologyAnalysisOA3(ontology,url);
		oa.exportRDFXML(export);
    }
}