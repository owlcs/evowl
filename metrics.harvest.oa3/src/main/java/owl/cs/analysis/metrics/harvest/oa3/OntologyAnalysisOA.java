package owl.cs.analysis.metrics.harvest.oa3;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import owl.cs.analysis.metrics.utilities.oa3.ExportRDF;
import owl.cs.analysis.metrics.utilities.oa3.MissingImportTracker;
import owl.cs.analysis.metrics.utilities.oa3.StaticMetrics;
import owl.cs.analysis.utilities.AppUtils;
import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OWLAPIMetricsApp;
import owl.cs.analysis.utilities.StringUtilities;

public class OntologyAnalysisOA extends OWLAPIMetricsApp {

	OWLOntology o = null;
	MissingImportTracker missingImportTracker;

	public OntologyAnalysisOA(File ontology, File out, String url, String group) {
		super(ontology, out, url,group);
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
			long start = System.currentTimeMillis();
			o = man.loadOntologyFromOntologyDocument(getOntologyFile());
			long end = System.currentTimeMillis();
			addResult(MetricLabels.OA3_PARSEABLE, true);
			addResult(MetricLabels.ONTOLOGYLOAD_TIME, end-start+"");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			addResult(MetricLabels.OA3_PARSEABLE, false);
			addResult(MetricLabels.OA3_LOAD_EXCEPTION_MESSAGE, e.getMessage());
			addResult(MetricLabels.OWLAPILOAD_EXCEPTION, e.getClass());
			if (e.getCause() != null) {
				addResult(MetricLabels.OWLAPILOAD_EXCEPTION_SOURCE, e.getCause().getClass());
			}
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

	@Override
	protected void exportResults() {
		ExportRDF.exportMeasurements(getSimpleRecord(), getURL(), getOutfile(), AppUtils.getJARName(OWLOntology.class),getGroup());
	}

}
