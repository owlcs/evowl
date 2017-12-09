package owl.cs.evowl.util;

import owl.cs.analysis.utilities.MetricLabels;

public class OWLBadgeImpl implements OWLBadge {

	private final String metric;
	private final String value;
	private final String badgelocation;

	public OWLBadgeImpl(String metric, String value, String badgelocation) {
		this.metric = metric;
		this.value = value;
		this.badgelocation = badgelocation;
	}

	@Override
	public boolean isMain() {
		return metric.equals(MetricLabels.EVOWLMAIN);
	}

	@Override
	public String getBadgeLocation() {
		return badgelocation;
	}

	@Override
	public String getMetric() {
		return metric;
	}

	@Override
	public String getValue() {
		return value;
	}

}
