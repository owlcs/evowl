package owl.cs.analysis.metrics.harvest.oa5;

import owl.cs.analysis.utilities.MetricsServerAppRunner;
import owl.cs.analysis.utilities.OWLAPIMetricsApp;

/**
 * Hello world!
 *
 */
public class App extends MetricsServerAppRunner
{
	public static void main(String[] args) {
		run(args, new App());
	}

	@Override
	protected OWLAPIMetricsApp prepare(String[] args) {
		prepareBasic(args, this.getClass().getName());
		return new OntologyAnalysisOA(getOntologyFile(),getCSVFile(), getURL(),getGroup());
	}
	
	
}
