package owl.cs.analysis.metrics.harvest.hermit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import owl.cs.analysis.metrics.utilities.oa5.ExperimentUtilities;
import owl.cs.analysis.metrics.utilities.oa5.ExportRDF;
import owl.cs.analysis.metrics.utilities.oa5.MissingImportTracker;
import owl.cs.analysis.metrics.utilities.oa5.ReasonerUtilities;
import owl.cs.analysis.utilities.MetricLabels;

public class OntologyAnalysisOA {

	OWLOntology o = null;
	OWLReasonerFactory rf = new ReasonerFactory();
	MissingImportTracker missingImportTracker;
	File ontology = null;
	String url = null;
	final private Map<String, String> rec = new HashMap<String, String>();

	public OntologyAnalysisOA(File ontology, String url) {
		this.ontology = ontology;
		this.url = url;
		runReasoner();
	}

	protected void runReasoner() {
		OWLReasoner r = null;
		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			o = man.loadOntologyFromOntologyDocument(ontology);
			r = rf.createReasoner(o);
			try {
				boolean consistent = r.isConsistent();
				rec.put(MetricLabels.CONSISTENT, consistent + "");
			} catch (Exception e) {
				rec.put(MetricLabels.REASONER_EXCEPTION, e.getMessage());
				rec.put(MetricLabels.CONSISTENT, "unknown");
			}

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			rec.put(MetricLabels.OWLAPILOADEXCEPTION, e.getMessage());
			rec.put(MetricLabels.CONSISTENT, "unknown");
		}
		rec.put(MetricLabels.REASONER_JAR, ExperimentUtilities.getJARName(ReasonerFactory.class));
		rec.put(MetricLabels.REASONERNAME, ReasonerUtilities.getReasonerFullname(r, "hermit"));
	}

	public Map<String, String> getSimpleRecord() {
		return rec;
	}

	public boolean exportRDFXML(File f) {
		return ExportRDF.exportMeasurements(getSimpleRecord(), url, f,
				ExperimentUtilities.getJARName(ReasonerFactory.class));
	}

}
