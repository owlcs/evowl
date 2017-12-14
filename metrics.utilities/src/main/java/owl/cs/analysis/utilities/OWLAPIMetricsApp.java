package owl.cs.analysis.utilities;

import java.io.File;

public abstract class OWLAPIMetricsApp extends App {

	
	private boolean parseable = false;
	private boolean nonempty = false;

	public OWLAPIMetricsApp(File ontology, File out, String url, String group) {
		super(ontology,out,url, group);
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
