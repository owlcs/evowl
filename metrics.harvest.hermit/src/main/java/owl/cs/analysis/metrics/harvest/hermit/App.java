package owl.cs.analysis.metrics.harvest.hermit;

import java.io.File;

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
		File export = new File(args[2],"metrics.harvest.hermit"+ontology.getName()+".rdf");
		
		OntologyAnalysisOA oa = new OntologyAnalysisOA(ontology,url);
		oa.exportRDFXML(export);
    }
}
