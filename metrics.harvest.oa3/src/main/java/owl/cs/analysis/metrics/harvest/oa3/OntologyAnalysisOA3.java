package owl.cs.analysis.metrics.harvest.oa3;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import owl.cs.analysis.metrics.utilities.oa3.ExportRDF;
import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OntologyAnalysis;

public class OntologyAnalysisOA3 extends OntologyAnalysis {

	OWLOntology o = null;
	
	public OntologyAnalysisOA3(File ontology, String url) {
		super(ontology,url);
	}

	@Override
	protected boolean isNonEmpty() {
		if(o!=null) {
			return o.getLogicalAxiomCount()>0;
		}
		return false;
	}

	@Override
	protected boolean owlapiParse() {
		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			o = man.loadOntologyFromOntologyDocument(getOntologyFile());
			return true;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			addResult(MetricLabels.OA3_PARSEABLE, false);
			addResult(MetricLabels.OA3_LOAD_EXCEPTION_MESSAGE,e.getMessage());
		}
		return false;
	}


	@Override
	protected void gatherMetrics() {
		// TODO Auto-generated method stub
		
	}

	public boolean exportRDFXML(File f) {
		return ExportRDF.exportMeasurements(getSimpleRecord(), getURL(), f);
	}

}
