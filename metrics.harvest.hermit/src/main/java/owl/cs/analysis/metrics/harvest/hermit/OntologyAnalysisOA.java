package owl.cs.analysis.metrics.harvest.hermit;

import java.io.File;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import owl.cs.analysis.metrics.utilities.oa5.ExportRDF;
import owl.cs.analysis.metrics.utilities.oa5.ReasonerUtilities;
import owl.cs.analysis.utilities.App;
import owl.cs.analysis.utilities.AppUtils;
import owl.cs.analysis.utilities.MetricLabels;

public class OntologyAnalysisOA extends App {

	OWLOntology o = null;
	OWLReasonerFactory rf = new ReasonerFactory();
	long timeout = 7200000;

	public OntologyAnalysisOA(File ontology, File out, String url,String group,long timeout) {
		super(ontology, out, url,group);
	}

	protected void runReasoner() {
		OWLReasoner r = null;
		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			o = man.loadOntologyFromOntologyDocument(getOntologyFile());
			try {
				long prep = System.currentTimeMillis();
				r = rf.createReasoner(o,new SimpleConfiguration(timeout));
				long start = System.currentTimeMillis();
				boolean consistent = r.isConsistent();
				long end = System.currentTimeMillis();
				addResult(MetricLabels.CONSISTENT, consistent + "");
				addResult(MetricLabels.CREATEREASONER_TIME, start - prep + "");
				addResult(MetricLabels.REASONING_CONSISTENCY_TIME, end - prep + "");
			} catch (Exception e) {
				e.printStackTrace();
				addResult(MetricLabels.REASONER_EXCEPTION, e.getClass().getSimpleName());
				addResult(MetricLabels.REASONER_EXCEPTION_MESSAGE, e.getMessage());
				addResult(MetricLabels.CONSISTENT, "unknown");
			}

		} catch (Exception e) {
			e.printStackTrace();
			addResult(MetricLabels.OWLAPILOAD_EXCEPTION, e.getClass().getSimpleName());
			addResult(MetricLabels.OWLAPILOAD_EXCEPTION_MESSAGE, e.getMessage());
			addResult(MetricLabels.CONSISTENT, "unknown");
		}
		addResult(MetricLabels.REASONER_JAR, AppUtils.getJARName(ReasonerFactory.class));
		addResult(MetricLabels.REASONERNAME, ReasonerUtilities.getReasonerFullname(r, "hermit"));
	}

	@Override
	protected void analyse() {
		runReasoner();
	}

	@Override
	protected void exportResults() {
		ExportRDF.exportMeasurements(getSimpleRecord(), getURL(), getOutfile(),
				AppUtils.getJARName(ReasonerFactory.class),getGroup());
	}

}
