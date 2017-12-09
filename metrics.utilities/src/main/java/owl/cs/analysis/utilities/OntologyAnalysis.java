package owl.cs.analysis.utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class OntologyAnalysis implements ExportToRDFXML {

	final private String url;
	final private File ontology;
	final private Map<String, String> rec = new HashMap<String, String>();
	private boolean parseable = false;
	private boolean nonempty = false;

	public OntologyAnalysis(File ontology, String url) {
		this.url = url;
		this.ontology = ontology;
		analyse();
	}

	protected void analyse() {
		parseable = owlapiParse();
		if (parseable) {
			nonempty = isNonEmpty();
		}
		if(nonempty) {
			gatherMetrics();
		}
		addResult(MetricLabels.OA3_PARSEABLE, parseable);
		addResult(MetricLabels.OA3_NONEMPTY, nonempty);
	}
	
	protected abstract void gatherMetrics();

	protected abstract boolean isNonEmpty();

	protected abstract boolean owlapiParse();
	
	protected void addResult(Object key, Object value) {
		rec.put(key.toString(), value.toString());
	}

	protected File getOntologyFile() {
		return this.ontology;
	}
	
	protected String getURL() {
		return this.url;
	}

	public Map<String, String> getSimpleRecord() {
		return rec;
	}

}
