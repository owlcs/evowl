package owl.cs.analysis.utilities;

import java.io.File;

public abstract class OWLAPIMetricsApp extends App {

	
	private boolean parseable = false;
	private boolean nonempty = false;

	public OWLAPIMetricsApp(File ontology, File out, String url) {
		super(ontology,out,url);
	}

	protected void analyse() {
		parseable = owlapiParse();
		if (parseable) {
			nonempty = isNonEmpty();
		}
		if(nonempty) {
			gatherMetrics();
		}
		addResult(MetricLabels.NONEMPTY, nonempty);
	}
	
	protected abstract void gatherMetrics();

	protected abstract boolean isNonEmpty();

	protected abstract boolean owlapiParse();
	
	

}
