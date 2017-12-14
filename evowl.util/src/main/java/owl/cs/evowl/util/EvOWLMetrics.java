package owl.cs.evowl.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import owl.cs.analysis.utilities.MetricLabels;
import owl.cs.analysis.utilities.OntologyBinding;

public class EvOWLMetrics {
	
	public final static String BADGEHOME = "https://github.com/owlcs/evowl/tree/master/evowl.ui/src/main/webapp/VAADIN/themes/evowlTheme/img/";
	public final static String BADGEDEPLOYLOCATIONRAW = "https://raw.githubusercontent.com/owlcs/evowl/master/evowl.ui/src/main/webapp/VAADIN/themes/evowlTheme/img/";
	public final static String BADGEDEPLOYLOCATIONLOCALHOST = "http://localhost:8080/evowl.ui-1.0.0/VAADIN/themes/evowlTheme/img/";
	
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
	public final static String MAIN_UNEVALUATED = OntologyBinding.entityIRI("evowl_main_unevaluated");
	public final static String MAIN_SILVER = OntologyBinding.entityIRI("evowl_main_silver");
	public final static String MAIN_GOLD = OntologyBinding.entityIRI("evowl_main_gold");

	public static final Map<String, String> badges;
	static {
		Map<String, String> aMap = new HashMap<>();
		aMap.put(EvOWLMetrics.MAIN_BROKEN, "evowl_broken_small.svg");
		aMap.put(EvOWLMetrics.MAIN_GOLD, "evowl_gold_small.svg");
		aMap.put(EvOWLMetrics.MAIN_SILVER, "evowl_silver_small.svg");
		aMap.put(EvOWLMetrics.MAIN_BRONZE, "evowl_bronze_small.svg");
		aMap.put(EvOWLMetrics.MAIN_UNEVALUATED, "evowl_unevaluated_small.svg");
		aMap.put(EvOWLMetrics.MAIN_GREY, "evowl_grey_small.svg");

		aMap.put(EvOWLMetrics.URL_HEALTHY, "evowl_urlhealthy_small.svg");
		aMap.put(EvOWLMetrics.NONEMPTY, "evowl_nonempty_small.svg");
		aMap.put(EvOWLMetrics.OA3, "evowl_oa3_small.svg");
		aMap.put(EvOWLMetrics.OA4, "evowl_oa4_small.svg");
		aMap.put(EvOWLMetrics.OA5, "evowl_oa5_small.svg");
		aMap.put(EvOWLMetrics.OWL2, "evowl_owl2_small.svg");
		aMap.put(EvOWLMetrics.OWL2DL, "evowl_owl2dl_small.svg");
		aMap.put(EvOWLMetrics.OWL2EL, "evowl_owl2el_small.svg");
		aMap.put(EvOWLMetrics.OWL2QL, "evowl_owl2ql_small.svg");
		aMap.put(EvOWLMetrics.OWL2RL, "evowl_owl2rl_small.svg");
		aMap.put(EvOWLMetrics.RDFS, "evowl_rdfs_small.svg");
		aMap.put(EvOWLMetrics.CONSISTENT, "evowl_consistent_small.svg");

		badges = Collections.unmodifiableMap(aMap);
	}
	
	public static final List<String> badgeorder;
	static {
		List<String> aMap = new ArrayList<>();
		aMap.add(EvOWLMetrics.URL_HEALTHY);
		aMap.add(EvOWLMetrics.OA3);
		aMap.add(EvOWLMetrics.OA4);
		aMap.add(EvOWLMetrics.OA5);
		aMap.add(EvOWLMetrics.NONEMPTY);
		aMap.add(EvOWLMetrics.OWL2);
		aMap.add(EvOWLMetrics.RDFS);
		aMap.add(EvOWLMetrics.OWL2DL);
		aMap.add(EvOWLMetrics.OWL2EL);
		aMap.add(EvOWLMetrics.OWL2QL);
		aMap.add(EvOWLMetrics.OWL2RL);
		aMap.add(EvOWLMetrics.CONSISTENT);
		badgeorder = Collections.unmodifiableList(aMap);
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
		String badgename = badges.get(MAIN_UNEVALUATED);
		if (badges.containsKey(metric)) {
			location = BADGEDEPLOYLOCATIONRAW+badges.get(metric);
			badgename = badges.get(metric);
		}
		return new OWLBadgeImpl(metric, location,badgename);
	}

}
