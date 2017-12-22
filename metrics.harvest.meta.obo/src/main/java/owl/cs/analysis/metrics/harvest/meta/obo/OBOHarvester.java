package owl.cs.analysis.metrics.harvest.meta.obo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import owl.cs.analysis.utilities.App;
import owl.cs.analysis.utilities.OntologyBinding;

public class OBOHarvester extends App {

	OWLOntology out;
	
	public OBOHarvester(File download) {
		super(new File("NA"), download, "NA", "NA");
	}

	protected void analyse() {

		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = man.getOWLDataFactory();
		OWLObjectProperty hasGroup = df.getOWLObjectProperty(IRI.create(OntologyBinding.gethasGroupIRI()));
		OWLClass deployloc = df.getOWLClass(IRI.create(OntologyBinding.getOntologyDeployLocationClass()));
		OWLNamedIndividual groupi = df.getOWLNamedIndividual(IRI.create(OntologyBinding.entityIRI("obofoundry")));
		
		try {
			out = man.createOntology();
			URL obourl = new URL("http://www.obofoundry.org/registry/ontologies.jsonld");
			InputStream inputStream = obourl.openStream();
			Object jsonObject = JsonUtils.fromInputStream(inputStream);
			Map context = new HashMap();
			JsonLdOptions options = new JsonLdOptions();
			Object compact = JsonLdProcessor.compact(jsonObject, context, options);
			LinkedHashMap map = (LinkedHashMap) compact;
			for (Object key : map.keySet()) {
				List allontologies = (ArrayList) map.get(key);
				for (Object o : allontologies) {
					LinkedHashMap ont = (LinkedHashMap) o;
					System.out.println(ont);
					String id = ont.containsKey("@id") ? ont.get("@id").toString() : null;
					String url = ont.containsKey("http://www.w3.org/ns/dcat#accessURL")
							? ont.get("http://www.w3.org/ns/dcat#accessURL").toString()
							: null;
					String title = ont.containsKey("http://purl.org/dc/elements/1.1/title")
							? ont.get("http://purl.org/dc/elements/1.1/title").toString()
							: null;
					String desc = ont.containsKey("http://purl.org/dc/elements/1.1/description")
							? ont.get("http://purl.org/dc/elements/1.1/description").toString()
							: null;
					String theme = ont.containsKey("http://purl.org/dc/terms/1.1/theme")
							? ont.get("http://purl.org/dc/terms/1.1/theme").toString()
							: null;
					String homepage = ont.containsKey("http://xmlns.com/foaf/0.1/homepage")
							? ((LinkedHashMap) ont.get("http://xmlns.com/foaf/0.1/homepage")).get("@id").toString()
							: null;

					OWLNamedIndividual subject = df.getOWLNamedIndividual(IRI.create(url));
					man.addAxiom(out, df.getOWLObjectPropertyAssertionAxiom(hasGroup, subject, groupi));
					man.addAxiom(out, df.getOWLClassAssertionAxiom(deployloc, subject));
					
					if(title!=null) man.addAxiom(out, df.getOWLAnnotationAssertionAxiom(subject.getIRI(), df.getOWLAnnotation(df.getOWLAnnotationProperty("http://purl.org/dc/elements/1.1/title"), df.getOWLLiteral(title))));
					if(desc!=null) man.addAxiom(out, df.getOWLAnnotationAssertionAxiom(subject.getIRI(), df.getOWLAnnotation(df.getOWLAnnotationProperty("http://purl.org/dc/elements/1.1/description"), df.getOWLLiteral(title))));
					if(theme!=null) man.addAxiom(out, df.getOWLAnnotationAssertionAxiom(subject.getIRI(), df.getOWLAnnotation(df.getOWLAnnotationProperty("http://purl.org/dc/terms/1.1/theme"), df.getOWLLiteral(title))));
					if(homepage!=null) man.addAxiom(out, df.getOWLAnnotationAssertionAxiom(subject.getIRI(), df.getOWLAnnotation(df.getOWLAnnotationProperty("http://xmlns.com/foaf/0.1/homepage"), df.getOWLLiteral(title))));
					if(id!=null) man.addAxiom(out, df.getOWLAnnotationAssertionAxiom(subject.getIRI(), df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(title))));
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void exportResults() {
		try {
			out.getOWLOntologyManager().saveOntology(out, new RDFXMLDocumentFormat(),new FileOutputStream(getOutfile()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
