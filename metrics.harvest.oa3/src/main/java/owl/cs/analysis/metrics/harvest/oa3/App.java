package owl.cs.analysis.metrics.harvest.oa3;

import java.io.File;

import owl.cs.analysis.metrics.utilities.oa3.ExperimentUtilities;
import owl.cs.analysis.utilities.OntologyAnalysis;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		String url = args[0];
		File ontology = new File(args[1]);
		File export = new File(args[2], "metrics.harvest.oa3" + ontology.getName() + ".rdf");
		boolean overwrite = args.length > 3 ? args[3].equals("o") : false;
		boolean run = export.exists() ? overwrite : true;
		if (run) {
			System.out.println("Running: "+ExperimentUtilities.getJARName(App.class)+": "+ontology.getName());
			OntologyAnalysis oa = new OntologyAnalysisOA(ontology, url);
			oa.exportRDFXML(export);
		}
	}
}
