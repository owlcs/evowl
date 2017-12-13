package owl.cs.analysis.metrics.harvest.hermit;

import owl.cs.analysis.utilities.MetricsServerAppRunner;

/**
 * Hello world!
 *
 */
public class App extends MetricsServerAppRunner {
	public static void main(String[] args) {
		run(args, new App());
	}

	@Override
	protected owl.cs.analysis.utilities.App prepare(String[] args) {
		prepareBasic(args, this.getClass().getName());
		return new OntologyAnalysisOA(getOntologyFile(), getCSVFile(), getURL());
	}
}
