package owl.cs.evowl.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DLExpressivityChecker;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import owl.cs.analysis.metrics.utilities.oa5.OntologyUtilities;
import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OntologyBinding;

public class EvOWLOntology {

	OWLOntology o;
	OWLOntologyManager man;

	/*
	 * csvData.putAll(StringUtilities
	 * .createPrefixedMap(getDatatypesWithAxiomOccurrenceCount(false),"dt_axct_"));
	 * csvData.putAll(StringUtilities
	 * .createPrefixedMap(getDatatypesWithAxiomOccurrenceCount(true),"dt_axct_incl")
	 * );
	 * csvData.putAll(StringUtilities.createPrefixedSet(getBuiltInDatatypes(false),
	 * "dt_builtin"));
	 * csvData.putAll(StringUtilities.createPrefixedSet(getBuiltInDatatypes(true),
	 * "dt_builtin_incl") );
	 * csvData.putAll(StringUtilities.createPrefixedSet(getNotBuiltInDatatypes(false
	 * ),"dt_notbuiltin"));
	 * csvData.putAll(StringUtilities.createPrefixedSet(getNotBuiltInDatatypes(true)
	 * ,"dt_notbuiltin_incl"));
	 */

	public EvOWLOntology() {
		man = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = man.getOWLDataFactory();
		OWLClass cl_measurement = df.getOWLClass(OntologyBinding.getMeasurementClass());
		OWLClass cl_axt = df.getOWLClass(OntologyBinding.entityIRI("AxiomTypeCountMeasurement"));
		OWLClass cl_const = df.getOWLClass(OntologyBinding.entityIRI("LogicalConstructMeasurement"));
		OWLClass cl_exp = df.getOWLClass(OntologyBinding.entityIRI("OWLClassExpressionCountMeasurement"));
		OWLClass cl_dt = df.getOWLClass(OntologyBinding.entityIRI("DatatypeCountMeasurement"));
		OWLClass cl_vio = df.getOWLClass(OntologyBinding.entityIRI("ProfileViolationMeasurement"));
		OWLClass cl_incl = df.getOWLClass(OntologyBinding.entityIRI("IncludedImportsMeasurement"));
		OWLClass cl_excl = df.getOWLClass(OntologyBinding.entityIRI("ExcludedImportsMeasurement"));

		Set<OWLClass> ignoreclasses = new HashSet<>();
		ignoreclasses.add(cl_measurement);
		ignoreclasses.add(cl_axt);
		ignoreclasses.add(cl_const);
		ignoreclasses.add(cl_exp);
		ignoreclasses.add(cl_dt);
		ignoreclasses.add(cl_incl);
		ignoreclasses.add(cl_excl);

		try {
			o = man.createOntology(IRI.create(OntologyBinding.ONTOLOGYIRI));
			Map<String, String> labels = MetricLabels.getLabels();
			for (String m : labels.keySet()) {
				o.add(df.getOWLSubClassOfAxiom(df.getOWLClass(OntologyBinding.entityIRI(labels.get(m))),
						cl_measurement));
			}
			o.add(df.getOWLSubClassOfAxiom(cl_axt, cl_measurement));
			o.add(df.getOWLSubClassOfAxiom(cl_const, cl_measurement));
			o.add(df.getOWLSubClassOfAxiom(cl_exp, cl_measurement));
			o.add(df.getOWLSubClassOfAxiom(cl_dt, cl_measurement));
			o.add(df.getOWLSubClassOfAxiom(cl_vio, cl_measurement));
			for (AxiomType obj : AxiomType.AXIOM_TYPES) {
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("axt_" + obj.getName().toLowerCase())), cl_axt));
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("axt_incl_" + obj.getName().toLowerCase())), cl_axt));
			}
			for (DLExpressivityChecker.Construct c : DLExpressivityChecker.Construct.values()) {
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("const_" + c.name().toLowerCase())), cl_const));
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("const_incl_" + c.name().toLowerCase())), cl_const));
			}
			for (ClassExpressionType c : ClassExpressionType.values()) {
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("exp_" + c.getName().toLowerCase())), cl_exp));
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("exp_incl_" + c.getName().toLowerCase())), cl_exp));
			}
			for (OWL2Datatype c : OWL2Datatype.values()) {
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("dt_builtin_" + c.getShortForm().toLowerCase())), cl_dt));
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("dt_builtin_incl_" + c.getShortForm().toLowerCase())), cl_dt));
			}
			
			for (Class<? extends OWLProfileViolation> c : OntologyUtilities.allviolations) {
				o.add(df.getOWLSubClassOfAxiom(
						df.getOWLClass(OntologyBinding.entityIRI("viol_" + c.getSimpleName().toLowerCase())), cl_vio));
			}

			OWLReasoner r = new StructuralReasonerFactory().createReasoner(o);
			
			for (OWLClass c : r.getSubClasses(cl_measurement).getFlattened()) {
				if (ignoreclasses.contains(c)) {
					continue;
				}
				String label_machine = c.getIRI().getFragment();
				String label_human = label_machine.replaceAll("_", " ").replaceAll("incl", "(Including Imports)");
				label_human = label_human.substring(0, 1).toUpperCase() + label_human.substring(1);
				o.add(df.getOWLAnnotationAssertionAxiom(c.getIRI(),
						df.getOWLAnnotation(df.getOWLAnnotationProperty(OntologyBinding.getMachineReadableIRI()), df.getOWLLiteral(label_machine))));
				o.add(df.getOWLAnnotationAssertionAxiom(c.getIRI(),
						df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(label_human))));
				System.out.println(label_machine);
				if (label_machine.contains("_incl")) {
					o.add(df.getOWLSubClassOfAxiom(c, cl_incl));
				} else {
					o.add(df.getOWLSubClassOfAxiom(c, cl_excl));
				}

			}
			man.saveOntology(o, new RDFXMLDocumentFormat(),
					new FileOutputStream(new File("D:\\metric.pipeline\\evowl_ontology\\ev.owl")));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
