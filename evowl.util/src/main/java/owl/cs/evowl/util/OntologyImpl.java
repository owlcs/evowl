package owl.cs.evowl.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OntologyImpl implements Ontology {

	private final String url;
	OWLBadge mainbadge = EvOWLMetrics.getBadge("default");
	private Set<OWLBadge> badges = new HashSet<>();
	private Map<String, String> metrics = new HashMap<>();
	private boolean urlhealthy = false;

	public OntologyImpl(String url) {
		this.url = url;
	}

	@Override
	public String getDownloadURL() {
		return url;
	}

	@Override
	public OWLBadge getEvowlMainBadge() {
		return mainbadge;
	}

	@Override
	public void addBadge(OWLBadge b) {
		if (b.isMain()) {
			setMainBadge(b);
		} else {
			badges.add(b);
		}
	}

	@Override
	public void setMetric(String metric, String value) {
		if (metric.equals(EvOWLMetrics.URL_HEALTHY)) {
			urlhealthy = value.equals("true");
		}

	}

	@Override
	public void setMainBadge(OWLBadge b) {
		mainbadge = b;
	}

	@Override
	public String getMainBadgeLocation() {
		return getEvowlMainBadge().getBadgeLocation();
	}

	@Override
	public Collection<OWLBadge> getBadges() {
		return badges;
	}

}
