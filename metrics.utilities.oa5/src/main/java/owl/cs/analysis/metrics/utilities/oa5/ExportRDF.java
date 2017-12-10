package owl.cs.analysis.metrics.utilities.oa5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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

import owl.cs.analysis.utilities.OntologyBinding;


public class ExportRDF {

	public static boolean exportMeasurements(Map<String,String> rec, String url, File f, String instrument) {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = man.getOWLDataFactory();
		try {
			OWLOntology out = man.createOntology();
			OWLNamedIndividual subject = df.getOWLNamedIndividual(IRI.create(url));
			OWLObjectProperty hasMeasurement = df.getOWLObjectProperty(IRI.create(OntologyBinding.getHasMeasurementIRI()));
			OWLClass deployloc = df.getOWLClass(IRI.create(OntologyBinding.getOntologyDeployLocationClass()));
			OWLDataProperty hasMeasurementValue = df.getOWLDataProperty(IRI.create(OntologyBinding.getHasMeasurementValueIRI()));
			OWLDataProperty hasMeasurementInstrument = df.getOWLDataProperty(IRI.create(OntologyBinding.getHasMeasurementInstrumentIRI()));
			OWLDataProperty hasRecordingDate = df.getOWLDataProperty(IRI.create(OntologyBinding.getHasRecordingDateIRI()));
			OWLLiteral date = df.getOWLLiteral(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			OWLLiteral instrumentlit = df.getOWLLiteral(instrument);
			for(String key:rec.keySet()) {
				String value = rec.get(key);
				OWLLiteral val = df.getOWLLiteral(value);
				OWLClass metric = df.getOWLClass(IRI.create(OntologyBinding.entityIRI(key)));
				OWLNamedIndividual measurement = df.getOWLNamedIndividual(IRI.create(OntologyBinding.entityIRI("Measurement"+UUID.randomUUID())));
				man.addAxiom(out, df.getOWLObjectPropertyAssertionAxiom(hasMeasurement, subject, measurement));
				man.addAxiom(out, df.getOWLClassAssertionAxiom(metric, measurement));
				man.addAxiom(out, df.getOWLClassAssertionAxiom(deployloc, subject));
				man.addAxiom(out, df.getOWLDataPropertyAssertionAxiom(hasMeasurementValue, measurement, val));
				man.addAxiom(out, df.getOWLDataPropertyAssertionAxiom(hasMeasurementInstrument, measurement, instrumentlit));
				man.addAxiom(out, df.getOWLDataPropertyAssertionAxiom(hasRecordingDate, measurement, date));
			}
			man.saveOntology(out, new RDFXMLDocumentFormat(),new FileOutputStream(f));
			return true;
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
