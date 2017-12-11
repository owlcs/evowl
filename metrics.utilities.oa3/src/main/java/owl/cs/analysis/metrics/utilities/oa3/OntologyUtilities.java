package owl.cs.analysis.metrics.utilities.oa3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.profiles.CycleInDatatypeDefinition;
import org.semanticweb.owlapi.profiles.DatatypeIRIAlsoUsedAsClassIRI;
import org.semanticweb.owlapi.profiles.EmptyOneOfAxiom;
import org.semanticweb.owlapi.profiles.IllegalPunning;
import org.semanticweb.owlapi.profiles.InsufficientIndividuals;
import org.semanticweb.owlapi.profiles.InsufficientOperands;
import org.semanticweb.owlapi.profiles.InsufficientPropertyExpressions;
import org.semanticweb.owlapi.profiles.LastPropertyInChainNotInImposedRange;
import org.semanticweb.owlapi.profiles.LexicalNotInLexicalSpace;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWL2Profile;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.profiles.OntologyIRINotAbsolute;
import org.semanticweb.owlapi.profiles.OntologyVersionIRINotAbsolute;
import org.semanticweb.owlapi.profiles.UseOfAnonymousIndividual;
import org.semanticweb.owlapi.profiles.UseOfBuiltInDatatypeInDatatypeDefinition;
import org.semanticweb.owlapi.profiles.UseOfDataOneOfWithMultipleLiterals;
import org.semanticweb.owlapi.profiles.UseOfDefinedDatatypeInDatatypeRestriction;
import org.semanticweb.owlapi.profiles.UseOfIllegalAxiom;
import org.semanticweb.owlapi.profiles.UseOfIllegalClassExpression;
import org.semanticweb.owlapi.profiles.UseOfIllegalDataRange;
import org.semanticweb.owlapi.profiles.UseOfIllegalFacetRestriction;
import org.semanticweb.owlapi.profiles.UseOfNonAbsoluteIRI;
import org.semanticweb.owlapi.profiles.UseOfNonAtomicClassExpression;
import org.semanticweb.owlapi.profiles.UseOfNonEquivalentClassExpression;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInCardinalityRestriction;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInDisjointPropertiesAxiom;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInFunctionalPropertyAxiom;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInIrreflexivePropertyAxiom;
import org.semanticweb.owlapi.profiles.UseOfNonSimplePropertyInObjectHasSelf;
import org.semanticweb.owlapi.profiles.UseOfNonSubClassExpression;
import org.semanticweb.owlapi.profiles.UseOfNonSuperClassExpression;
import org.semanticweb.owlapi.profiles.UseOfObjectOneOfWithMultipleIndividuals;
import org.semanticweb.owlapi.profiles.UseOfObjectPropertyInverse;
import org.semanticweb.owlapi.profiles.UseOfPropertyInChainCausesCycle;
import org.semanticweb.owlapi.profiles.UseOfReservedVocabularyForAnnotationPropertyIRI;
import org.semanticweb.owlapi.profiles.UseOfReservedVocabularyForClassIRI;
import org.semanticweb.owlapi.profiles.UseOfReservedVocabularyForDataPropertyIRI;
import org.semanticweb.owlapi.profiles.UseOfReservedVocabularyForIndividualIRI;
import org.semanticweb.owlapi.profiles.UseOfReservedVocabularyForObjectPropertyIRI;
import org.semanticweb.owlapi.profiles.UseOfTopDataPropertyAsSubPropertyInSubPropertyAxiom;
import org.semanticweb.owlapi.profiles.UseOfUndeclaredAnnotationProperty;
import org.semanticweb.owlapi.profiles.UseOfUndeclaredClass;
import org.semanticweb.owlapi.profiles.UseOfUndeclaredDataProperty;
import org.semanticweb.owlapi.profiles.UseOfUndeclaredDatatype;
import org.semanticweb.owlapi.profiles.UseOfUndeclaredObjectProperty;
import org.semanticweb.owlapi.profiles.UseOfUnknownDatatype;

public class OntologyUtilities {

	public static final Set<Class<? extends OWLProfileViolation>> permittedViolationsForRepair = new HashSet<>();
	public static final Set<Class<? extends OWLProfileViolation>> allviolations = preparePermittedViolations();

	private static Set<Class<? extends OWLProfileViolation>> preparePermittedViolations() {
		Set<Class<? extends OWLProfileViolation>> permittedViolationsForRepair = new HashSet<Class<? extends OWLProfileViolation>>();
		permittedViolationsForRepair.add(EmptyOneOfAxiom.class);
		permittedViolationsForRepair.add(InsufficientIndividuals.class);
		permittedViolationsForRepair.add(InsufficientPropertyExpressions.class);
		permittedViolationsForRepair.add(InsufficientOperands.class);

		permittedViolationsForRepair.add(OntologyIRINotAbsolute.class);
		permittedViolationsForRepair.add(OntologyVersionIRINotAbsolute.class);
		permittedViolationsForRepair.add(UseOfNonAbsoluteIRI.class);
		permittedViolationsForRepair.add(UseOfUndeclaredAnnotationProperty.class);
		permittedViolationsForRepair.add(UseOfUndeclaredClass.class);
		permittedViolationsForRepair.add(UseOfUndeclaredDataProperty.class);
		permittedViolationsForRepair.add(UseOfUndeclaredDatatype.class);
		permittedViolationsForRepair.add(UseOfUndeclaredObjectProperty.class);

		permittedViolationsForRepair.add(CycleInDatatypeDefinition.class);
		permittedViolationsForRepair.add(DatatypeIRIAlsoUsedAsClassIRI.class);
		permittedViolationsForRepair.add(IllegalPunning.class);
		permittedViolationsForRepair.add(InsufficientOperands.class);
		permittedViolationsForRepair.add(LastPropertyInChainNotInImposedRange.class);
		permittedViolationsForRepair.add(LexicalNotInLexicalSpace.class);
		permittedViolationsForRepair.add(UseOfAnonymousIndividual.class);
		permittedViolationsForRepair.add(UseOfBuiltInDatatypeInDatatypeDefinition.class);
		permittedViolationsForRepair.add(UseOfDataOneOfWithMultipleLiterals.class);
		permittedViolationsForRepair.add(UseOfDefinedDatatypeInDatatypeRestriction.class);
		permittedViolationsForRepair.add(UseOfIllegalAxiom.class);
		permittedViolationsForRepair.add(UseOfIllegalClassExpression.class);
		permittedViolationsForRepair.add(UseOfIllegalDataRange.class);
		permittedViolationsForRepair.add(UseOfIllegalFacetRestriction.class);
		permittedViolationsForRepair.add(UseOfNonAbsoluteIRI.class);
		permittedViolationsForRepair.add(UseOfNonAtomicClassExpression.class);
		permittedViolationsForRepair.add(UseOfNonEquivalentClassExpression.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInAsymmetricObjectPropertyAxiom.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInCardinalityRestriction.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInDisjointPropertiesAxiom.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInFunctionalPropertyAxiom.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInInverseFunctionalObjectPropertyAxiom.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInIrreflexivePropertyAxiom.class);
		permittedViolationsForRepair.add(UseOfNonSimplePropertyInObjectHasSelf.class);
		permittedViolationsForRepair.add(UseOfNonSubClassExpression.class);
		permittedViolationsForRepair.add(UseOfNonSuperClassExpression.class);
		permittedViolationsForRepair.add(UseOfObjectOneOfWithMultipleIndividuals.class);
		permittedViolationsForRepair.add(UseOfObjectPropertyInverse.class);
		permittedViolationsForRepair.add(UseOfPropertyInChainCausesCycle.class);
		permittedViolationsForRepair.add(UseOfReservedVocabularyForAnnotationPropertyIRI.class);
		permittedViolationsForRepair.add(UseOfReservedVocabularyForClassIRI.class);
		permittedViolationsForRepair.add(UseOfReservedVocabularyForDataPropertyIRI.class);
		permittedViolationsForRepair.add(UseOfReservedVocabularyForIndividualIRI.class);
		permittedViolationsForRepair.add(UseOfReservedVocabularyForObjectPropertyIRI.class);
		permittedViolationsForRepair.add(UseOfTopDataPropertyAsSubPropertyInSubPropertyAxiom.class);
		permittedViolationsForRepair.add(UseOfUnknownDatatype.class);
		return permittedViolationsForRepair;
	}

	public static Set<OWLProfileViolation> repair(OWLOntology ontology, OWLOntologyManager manager) {
		Set<OWLProfileViolation> fixed = repairOntology(ontology, new OWL2Profile());
		fixed.addAll(repairOntology(ontology, new OWL2DLProfile()));
		return fixed;
	}

	public static Map<String, Set<OWLProfileViolation>> dlify(OWLOntology ontology) {
		Map<String, Set<OWLProfileViolation>> map = new HashMap<String, Set<OWLProfileViolation>>();
		Set<OWLProfileViolation> fixed1 = fullyRepairOntology(ontology, new OWL2Profile());
		fixed1.addAll(fullyRepairOntology(ontology, new OWL2DLProfile()));
		Set<OWLProfileViolation> fixed2 = fullyRepairOntology(ontology, new OWL2Profile());
		fixed2.addAll(fullyRepairOntology(ontology, new OWL2DLProfile()));
		map.put("fixed_round1", fixed1);
		map.put("fixed_round2", fixed2);
		return map;
	}

	private static Set<OWLProfileViolation> fullyRepairOntology(OWLOntology o, OWLProfile owl2Profile) {
		Set<OWLProfileViolation> fixedViolations = new HashSet<OWLProfileViolation>();
		List<OWLProfileViolation> violations = owl2Profile.checkOntology(o).getViolations();

		for (OWLProfileViolation violation : violations) {
			// TODO o.getOWLOntologyManager().applyChanges(violation.repair());
			fixedViolations.add(violation);
		}
		return fixedViolations;
	}

	private static Set<OWLProfileViolation> repairOntology(OWLOntology o, OWLProfile owl2Profile) {
		Set<OWLProfileViolation> fixedViolations = new HashSet<OWLProfileViolation>();
		List<OWLProfileViolation> violations = owl2Profile.checkOntology(o).getViolations();

		for (OWLProfileViolation violation : violations) {
			if (permittedViolationsForRepair.contains(violation.getClass())) {
				// in case of undeclaredEntity, check whether reserved vocabulary!
				// System.out.println(violation.getClass());
				// TODO o.getOWLOntologyManager().applyChanges(violation.repair());
				fixedViolations.add(violation);
			} else if (violation instanceof OntologyVersionIRINotAbsolute) {
				o.getOWLOntologyManager().applyChanges(repair(violation, o));
				fixedViolations.add(violation);
			}
		}
		return fixedViolations;
	}

	private static List<? extends OWLOntologyChange> repair(OWLProfileViolation violation, OWLOntology ontology) {
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		if (violation instanceof OntologyVersionIRINotAbsolute) {
			// new OWLOntologyID changed in OA4
			changes.add(new SetOntologyID(ontology, new OWLOntologyID(IRI.create("urn:ontology#" + UUID.randomUUID()),
					IRI.create("urn:ontology#" + UUID.randomUUID()))));
		}
		return changes;
	}

	public static Set<OWLProfileViolation> repair(OWLOntology o) {
		return repair(o, o.getOWLOntologyManager());
	}

}
