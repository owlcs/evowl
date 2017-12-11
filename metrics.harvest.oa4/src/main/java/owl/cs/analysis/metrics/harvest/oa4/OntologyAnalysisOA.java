package owl.cs.analysis.metrics.harvest.oa4;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import owl.cs.analysis.metrics.utilities.oa4.ExperimentUtilities;
import owl.cs.analysis.metrics.utilities.oa4.ExportRDF;
import owl.cs.analysis.metrics.utilities.oa4.MissingImportTracker;
import owl.cs.analysis.metrics.utilities.oa4.OntologyUtilities;
import owl.cs.analysis.metrics.utilities.oa4.StaticMetrics;
import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OntologyAnalysis;
import owl.cs.analysis.utilities.StringUtilities;

public class OntologyAnalysisOA extends OntologyAnalysis {

	OWLOntology o = null;
	MissingImportTracker missingImportTracker;

	public OntologyAnalysisOA(File ontology, String url) {
		super(ontology, url);
	}

	@Override
	protected boolean isNonEmpty() {
		if (o != null) {
			return o.getLogicalAxiomCount() > 0;
		}
		return false;
	}

	@Override
	protected boolean owlapiParse() {
		try {
			missingImportTracker = new MissingImportTracker();
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			man.addMissingImportListener(missingImportTracker);
			o = man.loadOntologyFromOntologyDocument(getOntologyFile());
			addResult(MetricLabels.OA4_PARSEABLE, true);
			return true;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			addResult(MetricLabels.OA4_PARSEABLE, false);
			addResult(MetricLabels.OA4_LOAD_EXCEPTION_MESSAGE, e.getMessage());
		}
		return false;
	}

	// IMPORTS
	public Set<String> getMissingImportsDeclarations() {
		return missingImportTracker.getMissingImports();
	}

	public Set<String> getValidImports(boolean direct) {
		Set<String> validImports = new HashSet<String>();
		Set<OWLOntology> imports = new HashSet<OWLOntology>();

		if (direct) {
			imports.addAll(o.getDirectImports());
		} else {
			imports.addAll(o.getImports());
		}

		for (OWLImportsDeclaration im : o.getImportsDeclarations()) {
			String iri = im.getIRI().toString();
			if (!missingImportTracker.getMissingImports().contains(iri)) {
				validImports.add(iri);
			}
		}
		return validImports;
	}

	@Override
	protected void gatherMetrics() {
		StaticMetrics sm = new StaticMetrics(o);
		Map<String, String> data = sm.getMetrics();
		for (String r : data.keySet()) {
			addResult(r, data.get(r));
		}
		addResult(MetricLabels.MISSING_INPORTS,
				StringUtilities.createSpaceSeperatedStringFromSet(getMissingImportsDeclarations()) + "");
		addResult(MetricLabels.VALID_IMPORTS,
				StringUtilities.createSpaceSeperatedStringFromSet(getValidImports(false)) + "");
		addResult(MetricLabels.VALID_IMPORTS_INCL,
				StringUtilities.createSpaceSeperatedStringFromSet(getValidImports(true)) + "");
	}

	public boolean exportRDFXML(File f) {
		return ExportRDF.exportMeasurements(getSimpleRecord(), getURL(), f,ExperimentUtilities.getJARName(OWLOntology.class));
	}

}
