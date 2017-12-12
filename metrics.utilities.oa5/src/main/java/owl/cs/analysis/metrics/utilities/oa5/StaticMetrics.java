package owl.cs.analysis.metrics.utilities.oa5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.metrics.AbstractOWLMetric;
import org.semanticweb.owlapi.metrics.AverageAssertedNamedSuperclassCount;
import org.semanticweb.owlapi.metrics.DLExpressivity;
import org.semanticweb.owlapi.metrics.GCICount;
import org.semanticweb.owlapi.metrics.HiddenGCICount;
import org.semanticweb.owlapi.metrics.MaximumNumberOfNamedSuperclasses;
import org.semanticweb.owlapi.metrics.NumberOfClassesWithMultipleInheritance;
import org.semanticweb.owlapi.metrics.ReferencedClassCount;
import org.semanticweb.owlapi.metrics.ReferencedDataPropertyCount;
import org.semanticweb.owlapi.metrics.ReferencedIndividualCount;
import org.semanticweb.owlapi.metrics.ReferencedObjectPropertyCount;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWL2Profile;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWL2RLProfile;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.util.DLExpressivityChecker;

import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.StringUtilities;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectOneOfImpl;

public class StaticMetrics {

	private final OWLOntology item;
	private final List<String> owlprofileviolations = new ArrayList<String>();

	public StaticMetrics(OWLOntology item) {
		this.item = item;

	}

	// ENTITIES

	public int getSignatureSize(Imports includeImportsClosure) {
		return getOntology().getSignature(includeImportsClosure).size();
	}

	public int getUndeclaredEntitiesCount(Imports includeImportsClosure) {
		int undeclared = 0;
		for (OWLEntity entity : getOntology().getSignature(includeImportsClosure)) {
			if (!getOntology().isDeclared(entity)) {
				undeclared++;
			}
		}
		return undeclared;
	}

	private boolean isTBoxContainsNominals(Imports b) {
		for (OWLAxiom ax : ExperimentUtilities.getTBoxAxioms(getLogicalAxioms(b, true))) {
			for (OWLClassExpression cl : ax.getNestedClassExpressions()) {
				if (cl instanceof OWLObjectOneOfImpl) {
					// System.out.println(ax);
					return true;
				} else if (cl instanceof OWLObjectHasValue) {
					// System.out.println(ax);
					return true;
				}
			}
		}
		return false;
	}

	private boolean isABoxContainsNominals(Imports b) {
		for (OWLAxiom ax : ExperimentUtilities.getABoxAxioms(getLogicalAxioms(b, true))) {
			for (OWLClassExpression cl : ax.getNestedClassExpressions()) {
				if (cl instanceof OWLObjectOneOfImpl) {
					return true;
				} else if (cl instanceof OWLObjectHasValue) {
					return true;
				}
			}
		}
		return false;
	}

	public int getClassCount(Imports includeImportsClosure) {
		return getOntology().getClassesInSignature(includeImportsClosure).size();
	}

	public int getObjectPropertyCount(Imports includeImportsClosure) {
		return getOntology().getObjectPropertiesInSignature(includeImportsClosure).size();
	}

	public int getDataPropertyCount(Imports included) {
		return getOntology().getDataPropertiesInSignature(included).size();
	}

	public int getDatatypesCount(Imports included) {
		return getOntology().getDatatypesInSignature(included).size();
	}

	public Map<String, Integer> getDatatypesWithAxiomOccurrenceCount(Imports includeImportsClosure) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<OWLAxiom> axioms = getAxioms(includeImportsClosure);
		for (OWLAxiom axiom : axioms) {
			Set<OWLDatatype> dtypes = axiom.getDatatypesInSignature();
			for (OWLDatatype datatype : dtypes) {
				String dtname = datatype.toString();
				if (datatype.isBuiltIn()) {
					dtname = datatype.getBuiltInDatatype().toString();
				}
				if (map.containsKey(dtname)) {
					Integer itemp = map.get(dtname);
					itemp++;
					map.put(dtname, itemp);
				} else {
					map.put(dtname, 1);
				}
			}
		}
		return map;
	}

	public Map<String, Integer> getBuiltInDatatypes(Imports included) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<OWLDatatype> datatypes = getOntology().getDatatypesInSignature(included);

		for (OWLDatatype datatype : datatypes) {
			if (datatype.isBuiltIn()) {
				String type = datatype.getBuiltInDatatype().getShortForm();
				if (map.containsKey(type)) {
					Integer i = map.get(type);
					map.put(type, (i + 1));
				} else {
					map.put(type, 1);
				}
			}
		}

		return map;
	}

	public Set<String> getNotBuiltInDatatypes(Imports included) {
		Set<String> set = new HashSet<String>();
		Set<OWLDatatype> properties = getOntology().getDatatypesInSignature(included);
		for (OWLDatatype datatype : properties) {
			if (!datatype.isBuiltIn()) {
				set.add(datatype.toString());
			}
		}
		return set;
	}

	public int getAnnotationPropertyCount() {
		return getOntology().getAnnotationPropertiesInSignature().size();
	}

	public int getAnnotationsCount() {
		return getOntology().getAnnotations().size();
	}

	public int getIndividualsCount(Imports included) {
		return getOntology().getIndividualsInSignature(included).size();
	}

	// AXIOMS

	public int getNumberOfRules(Imports includeImportsClosure) {
		int ct = 0;
		Set<OWLAxiom> logicalaxiom = getLogicalAxioms(includeImportsClosure, false);
		for (OWLAxiom ax : logicalaxiom) {
			if (ax.isLogicalAxiom()) {
				if (ax.getAxiomType().toString().equals("Rule")) {
					ct++;
				}
			}
		}
		return ct;
	}

	public int getAxiomsWithComplexRHS(Imports included) {
		// complex: RHS does not only contain nested conjuctions / atomic
		// classnames
		int ct = 0;
		Set<OWLAxiom> logicalaxiom = getLogicalAxioms(included, false);
		for (OWLAxiom ax : logicalaxiom) {
			if (ax instanceof OWLSubClassOfAxiom) {
				OWLSubClassOfAxiom scax = (OWLSubClassOfAxiom) ax;
				OWLClassExpression RHS = scax.getSuperClass();
				if (isComplex(RHS)) {
					ct++;
				}
			} else if (ax instanceof OWLEquivalentClassesAxiom) {
				OWLEquivalentClassesAxiom scax = (OWLEquivalentClassesAxiom) ax;
				for (OWLClassExpression ex : scax.getClassExpressions()) {
					if (isComplex(ex)) {
						ct++;
						break;
					}
				}
			}
		}
		return ct;
	}

	public double getAVGSizeOfRHS(Imports included) {
		// complex: RHS does not only contain nested conjuctions / atomic
		// classnames
		double ct = 0;
		double ct_complex = 0;
		Set<OWLAxiom> logicalaxiom = getLogicalAxioms(included, false);
		for (OWLAxiom ax : logicalaxiom) {
			if (ax instanceof OWLSubClassOfAxiom) {
				OWLSubClassOfAxiom scax = (OWLSubClassOfAxiom) ax;
				OWLClassExpression RHS = scax.getSuperClass();
				if (isComplex(RHS)) {
					// System.out.println(RHS);
					ct_complex++;
					for (OWLClassExpression ex : RHS.getNestedClassExpressions()) {
						if (isComplex(ex)) {
							ct++;
						}
					}
				}
			} else if (ax instanceof OWLEquivalentClassesAxiom) {
				OWLEquivalentClassesAxiom scax = (OWLEquivalentClassesAxiom) ax;
				for (OWLClassExpression oper : scax.getClassExpressions()) {
					if (isComplex(oper)) {
						ct_complex++;
						for (OWLClassExpression ex : oper.getNestedClassExpressions()) {
							if (isComplex(ex)) {
								ct++;
							}
						}
					}
				}
			}
		}
		return (ct / ct_complex);
	}

	private boolean isComplex(OWLClassExpression ex) {
		for (OWLClassExpression exnested : ex.getNestedClassExpressions()) {
			if (!exnested.isClassExpressionLiteral()) {
				if (!(exnested instanceof OWLObjectIntersectionOf)) {
					return true;
				}
			}
		}
		return false;
	}

	private Set<OWLAxiom> getLogicalAxioms(Imports includeImportsClosure, boolean skiprules) {
		Set<AxiomType<?>> at = new HashSet<AxiomType<?>>();
		at.addAll(AxiomType.TBoxAxiomTypes);
		at.addAll(AxiomType.RBoxAxiomTypes);
		at.addAll(AxiomType.ABoxAxiomTypes);
		return ExperimentUtilities.getLogicalAxioms(getOntology(), includeImportsClosure, skiprules, at);
	}

	public Set<OWLAxiom> getAxioms(Imports included) {
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		axioms.addAll(getOntology().getAxioms(Imports.INCLUDED));
		return axioms;
	}

	public int getAxiomCount(Imports includeImportsClosure) {
		return getAxioms(includeImportsClosure).size();
	}

	public int getLogicalAxiomCount(Imports includeImportsClosure) {
		return getLogicalAxioms(includeImportsClosure, true).size();
	}

	public int getTBoxSize(Imports useImportsClosure) {
		return getOntology().getTBoxAxioms(useImportsClosure).size();
	}

	public int getTBoxRboxSize(Imports useImportsClosure) {
		int i = 0;
		Set<AxiomType<?>> axty = ExperimentUtilities.getTBoxAxiomTypes(true);
		for (AxiomType<?> at : axty) {
			i += getOntology().getAxioms(at, useImportsClosure).size();
		}
		return i;
	}

	public int getABoxSize(Imports useImportsClosure) {
		return getOntology().getABoxAxioms(useImportsClosure).size();
	}

	public int getRBoxSize(Imports useImportsClosure) {
		return getOntology().getRBoxAxioms(useImportsClosure).size();
	}

	public Map<String, Integer> getAxiomTypeCounts(Imports includeImportsClosure) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<OWLAxiom> axioms = getAxioms(includeImportsClosure);

		for (OWLAxiom ax : axioms) {
			String type = ax.getAxiomType().getName();
			if (map.containsKey(type)) {
				Integer i = map.get(type);
				map.put(type, (i + 1));
			} else {
				map.put(type, 1);
			}
		}

		return map;
	}

	private Map<String, Integer> getOWLClassExpressionCount(Imports includeImportsClosure) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<OWLAxiom> axioms = getAxioms(includeImportsClosure);

		for (OWLAxiom ax : axioms) {
			for (OWLClassExpression cl : ax.getNestedClassExpressions()) {
				String type = cl.getClassExpressionType().getName();
				if (map.containsKey(type)) {
					Integer i = map.get(type);
					map.put(type, (i + 1));
				} else {
					map.put(type, 1);
				}
			}
		}

		return map;
	}

	public Set<AxiomType<?>> getAxiomTypes(Imports includeImportsClosure) {
		Set<AxiomType<?>> axtypes = new HashSet<AxiomType<?>>();
		for (OWLAxiom ax : getAxioms(includeImportsClosure)) {
			axtypes.add(ax.getAxiomType());
		}
		return axtypes;
	}

	public boolean isOWL2Profile() {
		OWLProfile profile = new OWL2Profile();
		return profile.checkOntology(getOntology()).isInProfile();
	}

	public boolean isOWL2ELProfile() {
		OWLProfile profile = new OWL2ELProfile();
		return profile.checkOntology(getOntology()).isInProfile();
	}

	public boolean isOWL2DLProfile() {
		OWLProfile profile = new OWL2DLProfile();
		OWLProfileReport report = profile.checkOntology(getOntology());
		for (OWLProfileViolation vio : report.getViolations()) {
			String s = vio.getClass().getSimpleName();
			owlprofileviolations.add(s);
		}
		return report.isInProfile();
	}

	public boolean isOWL2RLProfile() {
		OWLProfile profile = new OWL2RLProfile();
		return profile.checkOntology(getOntology()).isInProfile();
	}

	public boolean isOWL2QLProfile() {
		OWLProfile profile = new OWL2QLProfile();
		return profile.checkOntology(getOntology()).isInProfile();
	}

	public boolean isRDFS() {
		// TODO: verify
		boolean isRDFS = true;

		loopAxioms: for (OWLAxiom ax : getAxioms(Imports.INCLUDED)) {
			if (ax.isLogicalAxiom()) {
				if (ax.isOfType(AxiomType.SUBCLASS_OF)) {
					OWLSubClassOfAxiom subAx = (OWLSubClassOfAxiom) ax;
					if (subAx.getSubClass().isAnonymous() || subAx.getSuperClass().isAnonymous()) {
						isRDFS = false;
						// System.out.println("SubClassAx: " + ax);
						break loopAxioms;
					}
				} else if (ax.isOfType(AxiomType.SUB_OBJECT_PROPERTY)) {
					OWLSubObjectPropertyOfAxiom subProp = (OWLSubObjectPropertyOfAxiom) ax;
					if (subProp.getSubProperty().isAnonymous() || subProp.getSuperProperty().isAnonymous()) {
						isRDFS = false;
						// System.out.println("SupPropertyAx: " + ax);
						break loopAxioms;
					}
				} else if (ax.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN) || ax.isOfType(AxiomType.OBJECT_PROPERTY_RANGE)
						|| ax.isOfType(AxiomType.DATA_PROPERTY_ASSERTION)
						|| ax.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)
						|| ax.isOfType(AxiomType.DATA_PROPERTY_DOMAIN) || ax.isOfType(AxiomType.DATA_PROPERTY_RANGE)) {
					// do nothing
					// System.out.println(ax.getAxiomType().getName()+" axiom: "
					// + ax);
				} else if (ax.isOfType(AxiomType.CLASS_ASSERTION)) {
					OWLClassAssertionAxiom assAx = (OWLClassAssertionAxiom) ax;
					if (assAx.getClassExpression().isAnonymous()) {
						isRDFS = false;
						// System.out.println("Class Assertion: " + ax);
						break loopAxioms;
					}
				} else {
					// System.out.println("Some other axiom: " + ax);
					isRDFS = false;
					break loopAxioms;
				}
			}
		}

		return isRDFS;
	}

	// REFERENCED CLASSES AND PROPERTIES

	public int getReferencedClassCount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new ReferencedClassCount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public int getReferencedDataPropertyCount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new ReferencedDataPropertyCount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public int getReferencedIndividualCount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new ReferencedIndividualCount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public int getReferencedObjectPropertyCount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new ReferencedObjectPropertyCount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	// RANDOM METRICS

	public int getMultipleInheritanceCount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new NumberOfClassesWithMultipleInheritance(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public int getMaximumNumberOfNamedSuperclasses(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new MaximumNumberOfNamedSuperclasses(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public int getGCICount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new GCICount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public int getHiddenGCICount(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Integer> metric = new HiddenGCICount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public double getAverageAssertedNamedSuperclasses(boolean useImportsClosure) {
		// TODO verify this
		AbstractOWLMetric<Double> metric = new AverageAssertedNamedSuperclassCount(getOntology());
		metric.setImportsClosureUsed(useImportsClosure);
		metric.setOntology(getOntology());
		return metric.getValue();
	}

	public double getAverageAssertedNamedSubclasses(Imports useImportsClosure) {
		int count = 0;
		for (OWLClass c : getOntology().getClassesInSignature(useImportsClosure)) {
			count += getOntology().getSubClassAxiomsForSuperClass(c).size();

		}
		double avg = (((double) count) / ((double) getOntology().getClassesInSignature().size()));
		return avg;
	}

	public int getClassesWithSingleSubclassCount(Imports useImportsClosure) {
		int count = 0;
		for (OWLClass c : getOntology().getClassesInSignature(useImportsClosure)) {
			if (getOntology().getSubClassAxiomsForSuperClass(c).size() == 1) {
				count++;
			}
		}
		return count;
	}

	public double getAverageInstancesPerClass(Imports useImportsClosure) {
		int count = 0;
		for (OWLClass c : getOntology().getClassesInSignature(useImportsClosure)) {
			count += getOntology().getClassAssertionAxioms(c).size();
		}
		/**
		 * *** added getDouble, added boolean useIMportsclosure to second divisor
		 */
		double avg = (((double) count) / ((double) getOntology().getClassesInSignature(useImportsClosure).size()));
		return avg;
	}

	public String getSyntax() {
		return getManager().getOntologyFormat(getOntology()).toString();
	}

	public String getOntologyId() {
		OWLOntologyID ontologyID = getOntology().getOntologyID();
		if (ontologyID.isAnonymous()) {
			return "anonymousId";
		} else {
			return ontologyID.getOntologyIRI().toString();
		}
	}

	public String getOntologyIdScheme() {
		OWLOntologyID ontologyID = getOntology().getOntologyID();
		if (ontologyID.isAnonymous()) {
			return "none";
		} else {
			// TODO OA4 Optional.or()
			return ontologyID.getOntologyIRI().orElse(IRI.create("")).getScheme();
		}
	}

	public String getExpressivity(boolean included) {
		DLExpressivity dl = new DLExpressivity(getOntology());
		dl.setImportsClosureUsed(included);
		dl.setOntology(getOntology());
		return dl.getValue();
	}

	// this is highly unpleasant and I wish we had a NoSQL DB or even just a
	// stupid XML file where we could throw it all in individually
	public Set<String> getConstructs(boolean includeImportsClosure) {
		Set<OWLOntology> onts = new HashSet<OWLOntology>();
		if (includeImportsClosure) {
			onts.addAll(getOntology().getImportsClosure());
		} else {
			onts.add(getOntology());
		}
		DLExpressivityChecker checker = new DLExpressivityChecker(onts);
		Set<String> constructs = new HashSet<String>();
		for (DLExpressivityChecker.Construct c : checker.getConstructs()) {
			constructs.add(c.name());
		}
		return constructs;
	}

	public boolean surelyContainsCycle(Imports includeImports) {
		return OntologyCycleDetector.containsCycle(getOntology(), includeImports);
	}

	public OWLOntology getOntology() {
		return this.item;
	}

	public OWLOntologyManager getManager() {
		return getOntology().getOWLOntologyManager();
	}

	public Map<String, Integer> getOwlprofileviolations() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String vio : owlprofileviolations) {
			if (map.containsKey(vio)) {
				Integer i = map.get(vio);
				map.put(vio, (i + 1));
			} else {
				map.put(vio, 1);
			}
		}

		return map;
	}

	private String getMostFrequentlyUsedClassInLogicalAxioms(Imports includeImportsClosure) {
		Map<String, Integer> classCountMap = new HashMap<String, Integer>();
		for (OWLAxiom eachAxiom : getLogicalAxioms(includeImportsClosure, true)) {
			if (!(eachAxiom instanceof OWLSubClassOfAxiom)) {
				continue;
			}
			OWLSubClassOfAxiom sax = (OWLSubClassOfAxiom) eachAxiom;
			String saxrhs = sax.getSuperClass().toString();
			for (OWLClass eachClass : eachAxiom.getClassesInSignature()) {
				int frequency = getNumberOfOccurences(saxrhs, eachClass.toString());
				if (classCountMap.containsKey(eachClass.toString())) {
					Integer currentClassCount = classCountMap.get(eachClass.toString());
					classCountMap.put(eachClass.toString(), currentClassCount + frequency);
				} else {
					classCountMap.put(eachClass.toString(), frequency);
				}
			}
		}
		int max = 0;
		String maxClassString = "";
		for (String eachKey : classCountMap.keySet()) {
			int eachCount = classCountMap.get(eachKey);
			if (eachCount > max) {
				max = eachCount;
				maxClassString = eachKey;
			}
		}
		return maxClassString;
	}

	private int getLongestAxiomLength(Imports includeImportsClosure) {
		int longestAxiomLength = 0;

		for (OWLAxiom eachAxiom : getLogicalAxioms(includeImportsClosure, true)) {
			int eachLength = getLengthOfAxiom(eachAxiom);
			if (eachLength > longestAxiomLength) {
				longestAxiomLength = eachLength;
			}
		}
		return longestAxiomLength;
	}

	private static int getLengthOfAxiom(OWLAxiom axiom) {
		int length = 0;

		String axiomstring = axiom.getAxiomWithoutAnnotations().toString();
		for (OWLEntity e : axiom.getSignature()) {
			length += getNumberOfOccurences(axiomstring, e.toString());
		}
		return length;
	}

	private static int getNumberOfOccurences(String haystack, String needle) {
		int length = 0;
		Pattern p = Pattern.compile(needle);
		Matcher m = p.matcher(haystack);
		while (m.find()) {
			length++;
		}
		return length;
	}

	private int getDatatypesNotBuiltinCount(Imports included) {
		return getNotBuiltInDatatypes(included).size();
	}

	private int getDatatypesBuiltinCount(Imports included) {
		return getBuiltInDatatypes(included).size();
	}

	public String getSignatureWithoutIRIs(Imports incl) {
		StringBuilder b = new StringBuilder();
		for (OWLEntity e : getOntology().getSignature(incl)) {
			if (e.isBottomEntity() || e.isTopEntity()) {
				continue;
			}
			// TODO OA4 Optional.or?
			if (e instanceof OWLClass) {
				b.append(e.getIRI().getRemainder().orElse("unknown") + "; ");
			} else if (e instanceof OWLObjectProperty) {
				b.append(e.getIRI().getRemainder().orElse("unknown") + "; ");
			} else if (e instanceof OWLDataProperty) {
				b.append(e.getIRI().getRemainder().orElse("unknown") + "; ");
			}
		}
		String returnstring = b.toString().contains(";") ? b.toString().substring(0, b.toString().lastIndexOf(";"))
				: "";
		return returnstring;
	}

	public Map<String, String> getEssentialMetrics() {
		Map<String, String> csvData = new HashMap<String, String>();
		csvData.put("owlapi_version", ExperimentUtilities.getResourcePath(getOntology()).getName());
		csvData.put(MetricLabels.CLASS_COUNT_INCL, getClassCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.CLASS_COUNT, getClassCount(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.ANNOTATION_PROP_COUNT, getAnnotationPropertyCount() + "");
		csvData.put(MetricLabels.ANNOTATIONS_COUNT, getAnnotationsCount() + "");
		csvData.put(MetricLabels.INDIVIDUAL_COUNT_INCL, getIndividualsCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.INDIVIDUAL_COUNT, getIndividualsCount(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.AXIOM_COMPLEXRHS_COUNT_INCL, getAxiomsWithComplexRHS(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.AXIOM_COMPLEXRHS_COUNT, getAxiomsWithComplexRHS(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.AVG_SIZE_COMPLEXRHS_INCL, getAVGSizeOfRHS(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.AVG_SIZE_COMPLEXRHS, getAVGSizeOfRHS(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.OBJPROPERTY_COUNT_INCL, getObjectPropertyCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.OBJPROPERTY_COUNT, getObjectPropertyCount(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.DATAPROPERTY_COUNT_INCL, getDataPropertyCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.DATAPROPERTY_COUNT, getDataPropertyCount(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.DATATYPE_COUNT_INCL, getDatatypesCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.DATATYPE_COUNT, getDatatypesCount(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.EXPRESSIVITY, getExpressivity(false) + "");
		csvData.put(MetricLabels.EXPRESSIVITY_INCL, getExpressivity(true) + "");
		csvData.put(MetricLabels.LOGICAL_AXIOM_COUNT, getLogicalAxiomCount(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.LOGICAL_AXIOM_COUNT_INCL, getLogicalAxiomCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.TBOX_SIZE, getTBoxSize(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.TBOX_SIZE_INCL, getTBoxSize(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.TBOXRBOX_SIZE, getTBoxRboxSize(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.TBOXRBOX_SIZE_INCL, getTBoxRboxSize(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.RULE_CT, getNumberOfRules(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.RULE_CT_INCL, getNumberOfRules(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.RBOX_SIZE, getRBoxSize(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.RBOX_SIZE_INCL, getRBoxSize(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.ABOX_SIZE, getABoxSize(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.ABOX_SIZE_INCL, getABoxSize(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.SIGNATURE_SIZE_INCL, getSignatureSize(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.SIGNATURE_SIZE, getSignatureSize(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.BOOL_PROFILE_OWL2, isOWL2Profile() + "");
		csvData.put(MetricLabels.BOOL_PROFILE_OWL2_DL, isOWL2DLProfile() + "");
		csvData.put(MetricLabels.BOOL_PROFILE_OWL2_EL, isOWL2ELProfile() + "");
		csvData.put(MetricLabels.BOOL_PROFILE_OWL2_QL, isOWL2QLProfile() + "");
		csvData.put(MetricLabels.BOOL_PROFILE_OWL2_RL, isOWL2RLProfile() + "");
		csvData.put(MetricLabels.BOOL_PROFILE_RDFS, isRDFS() + "");
		csvData.putAll(StringUtilities.createPrefixedMap(getOwlprofileviolations(), "viol"));
		csvData.putAll(StringUtilities.createPrefixedSet(getConstructs(true), "const_incl"));
		csvData.putAll(StringUtilities.createPrefixedSet(getConstructs(false), "const"));
		csvData.putAll(StringUtilities.createPrefixedMap(getAxiomTypeCounts(Imports.EXCLUDED), "axt"));
		csvData.putAll(StringUtilities.createPrefixedMap(getAxiomTypeCounts(Imports.INCLUDED), "axt_incl"));
		csvData.put(MetricLabels.TBOX_CONTAINS_NOMINALS_INCL, isTBoxContainsNominals(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.TBOX_CONTAINS_NOMINALS, isTBoxContainsNominals(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.ABOX_CONTAINS_NOMINALS_INCL, isABoxContainsNominals(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.ABOX_CONTAINS_NOMINALS, isABoxContainsNominals(Imports.EXCLUDED) + "");
		return csvData;
	}

	public Map<String, String> getMetrics() {
		Map<String, String> csvData = new HashMap<String, String>();
		csvData.putAll(getEssentialMetrics());

		csvData.put(MetricLabels.MAX_AXIOMLENGTH, getLongestAxiomLength(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.MAX_AXIOMLENGTH_INCL, getLongestAxiomLength(Imports.INCLUDED) + "");

		csvData.put(MetricLabels.AVG_ASSERT_N_SUBCLASS_INCL, getAverageAssertedNamedSubclasses(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.AVG_ASSERT_N_SUBCLASS, getAverageAssertedNamedSubclasses(Imports.EXCLUDED) + "");
		csvData.put(MetricLabels.AVG_ASSERT_N_SUPERCLASS_INCL, getAverageAssertedNamedSuperclasses(true) + "");
		csvData.put(MetricLabels.AVG_ASSERT_N_SUPERCLASS, getAverageAssertedNamedSuperclasses(false) + "");
		csvData.put(MetricLabels.AVG_INSTANCE_PER_CLASS_INCL, getAverageInstancesPerClass(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.AVG_INSTANCE_PER_CLASS, getAverageInstancesPerClass(Imports.EXCLUDED) + "");

		csvData.put(MetricLabels.CLASS_SGL_SUBCLASS_COUNT_INCL,
				getClassesWithSingleSubclassCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.CLASS_SGL_SUBCLASS_COUNT, getClassesWithSingleSubclassCount(Imports.EXCLUDED) + "");

		csvData.put(MetricLabels.GCI_COUNT_INCL, getGCICount(true) + "");
		csvData.put(MetricLabels.GCI_COUNT, getGCICount(false) + "");
		csvData.put(MetricLabels.GCI_HIDDEN_COUNT_INCL, getHiddenGCICount(true) + "");
		csvData.put(MetricLabels.GCI_HIDDEN_COUNT, getHiddenGCICount(false) + "");

		csvData.put(MetricLabels.MAX_NUM_NAMED_SUPERCLASS_INCL, getMaximumNumberOfNamedSuperclasses(true) + "");
		csvData.put(MetricLabels.MAX_NUM_NAMED_SUPERCLASS, getMaximumNumberOfNamedSuperclasses(false) + "");
		csvData.put(MetricLabels.MULTI_INHERITANCE_COUNT_INCL, getMultipleInheritanceCount(true) + "");
		csvData.put(MetricLabels.MULTI_INHERITANCE_COUNT, getMultipleInheritanceCount(false) + "");
		csvData.put(MetricLabels.ONTOLOGY_ID, getOntologyId() + "");
		csvData.put(MetricLabels.ONTOLOGY_ID_SCHEME, getOntologyIdScheme() + "");

		csvData.put(MetricLabels.REF_CLASS_COUNT_INCL, getReferencedClassCount(true) + "");
		csvData.put(MetricLabels.REF_CLASS_COUNT, getReferencedClassCount(false) + "");
		csvData.put(MetricLabels.REF_DATAPROP_COUNT_INCL, getReferencedDataPropertyCount(true) + "");
		csvData.put(MetricLabels.REF_DATAPROP_COUNT, getReferencedDataPropertyCount(false) + "");
		csvData.put(MetricLabels.REF_INDIV_COUNT_INCL, getReferencedIndividualCount(true) + "");
		csvData.put(MetricLabels.REF_DATAPROP_COUNT, getReferencedIndividualCount(false) + "");
		csvData.put(MetricLabels.REF_OBJPROP_COUNT_INCL, getReferencedObjectPropertyCount(true) + "");
		csvData.put(MetricLabels.REF_OBJPROP_COUNT, getReferencedObjectPropertyCount(false) + "");

		csvData.put(MetricLabels.SYNTAX, getSyntax() + "");
		csvData.put(MetricLabels.UNDECLARED_ENTITY_COUNT_INCL, getUndeclaredEntitiesCount(Imports.INCLUDED) + "");
		csvData.put(MetricLabels.UNDECLARED_ENTITY_COUNT, getUndeclaredEntitiesCount(Imports.EXCLUDED) + "");

		if (surelyContainsCycle(Imports.INCLUDED)) {
			csvData.put(MetricLabels.CYCLE_INCL, "1");
		} else {
			csvData.put(MetricLabels.CYCLE_INCL, "unkown");
		}

		if (surelyContainsCycle(Imports.EXCLUDED)) {
			csvData.put(MetricLabels.CYCLE, "1");
		} else {
			csvData.put(MetricLabels.CYCLE, "unkown");
		}
		csvData.putAll(StringUtilities.createPrefixedMap(getBuiltInDatatypes(Imports.EXCLUDED), "dt_builtin"));
		csvData.putAll(StringUtilities.createPrefixedMap(getBuiltInDatatypes(Imports.INCLUDED), "dt_builtin_incl"));
		csvData.putAll(StringUtilities.createPrefixedMap(getOWLClassExpressionCount(Imports.EXCLUDED), "exp"));
		csvData.putAll(StringUtilities.createPrefixedMap(getOWLClassExpressionCount(Imports.INCLUDED), "exp_incl"));
		csvData.put(MetricLabels.DATATYPES_NOT_BUILT_IN,
				StringUtilities.createSpaceSeperatedStringFromSet(getNotBuiltInDatatypes(Imports.EXCLUDED)));
		csvData.put(MetricLabels.DATATYPES_NOT_BUILT_IN_INCL,
				StringUtilities.createSpaceSeperatedStringFromSet(getNotBuiltInDatatypes(Imports.INCLUDED)));

		csvData.put(MetricLabels.MOST_FRQUENTLY_USED_CONCEPT,
				getMostFrequentlyUsedClassInLogicalAxioms(Imports.EXCLUDED) + "");

		return csvData;
	}
}
