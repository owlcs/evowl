package owl.cs.evowl.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OntologyBinding;

public class EvOWLMetrics {
	public final static String NONEMPTY = OntologyBinding.entityIRI(MetricLabels.NONEMPTY);
	public final static String OA3 = OntologyBinding.entityIRI(MetricLabels.OA3_PARSEABLE);
	public final static String OA4 = OntologyBinding.entityIRI(MetricLabels.OA4_PARSEABLE);
	public final static String OA5 = OntologyBinding.entityIRI(MetricLabels.OA5_PARSEABLE);
	public final static String OWL2 = OntologyBinding.entityIRI(MetricLabels.BOOL_PROFILE_OWL2);
	public final static String OWL2DL = OntologyBinding.entityIRI(MetricLabels.BOOL_PROFILE_OWL2_DL);
	public final static String OWL2EL = OntologyBinding.entityIRI(MetricLabels.BOOL_PROFILE_OWL2_EL);
	public final static String OWL2QL = OntologyBinding.entityIRI(MetricLabels.BOOL_PROFILE_OWL2_QL);
	public final static String OWL2RL = OntologyBinding.entityIRI(MetricLabels.BOOL_PROFILE_OWL2_RL);
	public final static String RDFS = OntologyBinding.entityIRI(MetricLabels.BOOL_PROFILE_RDFS);
	public final static String URL_HEALTHY = OntologyBinding.entityIRI(MetricLabels.URL_DOWNLOADABLE);
	public final static String CONSISTENT = OntologyBinding.entityIRI(MetricLabels.CONSISTENT);

	public final static String MAIN_BROKEN = OntologyBinding.entityIRI("evowl_main_broken");
	public final static String MAIN_BRONZE = OntologyBinding.entityIRI("evowl_main_bronze");
	public final static String MAIN_GREY = OntologyBinding.entityIRI("evowl_main_grey");
	public final static String MAIN_UNEVALUATED = OntologyBinding.entityIRI("evowl_main_bronze");
	public final static String MAIN_SILVER = OntologyBinding.entityIRI("evowl_main_silver");
	public final static String MAIN_GOLD = OntologyBinding.entityIRI("evowl_main_gold");

	public static final Map<String, String> badges;
	static {
		Map<String, String> aMap = new HashMap<>();
		aMap.put(EvOWLMetrics.MAIN_BROKEN, "https://purl.org/evowl/broken");
		aMap.put(EvOWLMetrics.MAIN_GOLD, "https://purl.org/evowl/gold");
		aMap.put(EvOWLMetrics.MAIN_SILVER, "https://purl.org/evowl/silver");
		aMap.put(EvOWLMetrics.MAIN_BRONZE, "https://purl.org/evowl/bronze");
		aMap.put(EvOWLMetrics.MAIN_UNEVALUATED, "https://purl.org/evowl/unevaluated");
		aMap.put(EvOWLMetrics.MAIN_GREY, "https://purl.org/evowl/grey");

		aMap.put(EvOWLMetrics.URL_HEALTHY, "https://purl.org/evowl/urlhealthy");
		aMap.put(EvOWLMetrics.NONEMPTY, "https://purl.org/evowl/nonempty");
		aMap.put(EvOWLMetrics.OA3, "https://purl.org/evowl/owlapi/3");
		aMap.put(EvOWLMetrics.OA4, "https://purl.org/evowl/owlapi/4");
		aMap.put(EvOWLMetrics.OA5, "https://purl.org/evowl/owlapi/5");
		aMap.put(EvOWLMetrics.OWL2, "https://purl.org/evowl/owl2");
		aMap.put(EvOWLMetrics.OWL2DL, "https://purl.org/evowl/owl2/dl");
		aMap.put(EvOWLMetrics.OWL2EL, "https://purl.org/evowl/owl2/el");
		aMap.put(EvOWLMetrics.OWL2QL, "https://purl.org/evowl/owl2/ql");
		aMap.put(EvOWLMetrics.OWL2RL, "https://purl.org/evowl/owl2/rl");
		aMap.put(EvOWLMetrics.RDFS, "https://purl.org/evowl/rdfs");
		aMap.put(EvOWLMetrics.CONSISTENT, "https://purl.org/evowl/consistent");

		badges = Collections.unmodifiableMap(aMap);
	}

	public static final Set<String> generalMetrics;
	static {
		Set<String> aMap = new HashSet<>();

		aMap.add(EvOWLMetrics.URL_HEALTHY);
		aMap.add(EvOWLMetrics.OA3);
		aMap.add(EvOWLMetrics.OA4);
		aMap.add(EvOWLMetrics.OA5);

		generalMetrics = Collections.unmodifiableSet(aMap);
	}

	public static OWLBadge getBadge(String metric) {
		String location = MAIN_UNEVALUATED;
		if (badges.containsKey(metric)) {
			location = badges.get(metric);
		}
		return new OWLBadgeImpl(metric, location);
	}

}
