package owl.cs.evowl.util;

public class OWLBadgeImpl implements OWLBadge {

	private final String metric;
	private final String badgelocation;

	public OWLBadgeImpl(String metric, String badgelocation) {
		this.metric = metric;
		this.badgelocation = badgelocation;
	}

	@Override
	public boolean isMain() {
		return metric.contains("evowl_main");
	}

	@Override
	public String getBadgeLocation() {
		return badgelocation;
	}

	@Override
	public String getMetric() {
		return metric;
	}
}
