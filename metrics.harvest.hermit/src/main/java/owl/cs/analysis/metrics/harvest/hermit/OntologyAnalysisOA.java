package owl.cs.analysis.metrics.harvest.hermit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.Address;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import owl.cs.analysis.metrics.utilities.oa5.ExperimentUtilities;
import owl.cs.analysis.metrics.utilities.oa5.ExportRDF;
import owl.cs.analysis.metrics.utilities.oa5.MissingImportTracker;
import owl.cs.analysis.metrics.utilities.oa5.ReasonerUtilities;
import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OntologyBinding;

public class OntologyAnalysisOA {

	OWLOntology o = null;
	OWLReasonerFactory rf = new ReasonerFactory();
	MissingImportTracker missingImportTracker;
	File ontology = null;
	String url = null;
	final private Map<String, String> rec = new HashMap<String, String>();

	public OntologyAnalysisOA(File ontology, String url) {
		this.ontology = ontology;
		this.url= url;
		owlapiParse();
		gatherMetrics();
	}

	protected void owlapiParse() {
		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			o = man.loadOntologyFromOntologyDocument(ontology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}


	protected void gatherMetrics() {
		OWLReasoner r = rf.createReasoner(o);
		boolean consistent = false;
		try {
			consistent = r.isConsistent();
		} catch(Exception e) {
			rec.put(MetricLabels.REASONER_EXCEPTION, e.getMessage());
		}
		rec.put(MetricLabels.REASONERNAME, ReasonerUtilities.getReasonerFullname(r,"hermit"));
		rec.put(MetricLabels.REASONER_JAR, ExperimentUtilities.getJARName(ReasonerFactory.class));
		rec.put(MetricLabels.CONSISTENT, consistent+"");		
	}
	
	public Map<String, String> getSimpleRecord() {
		return rec;
	}

	public boolean exportRDFXML(File f) {
		return ExportRDF.exportMeasurements(getSimpleRecord(), url,f, ExperimentUtilities.getJARName(ReasonerFactory.class));
	}

}
