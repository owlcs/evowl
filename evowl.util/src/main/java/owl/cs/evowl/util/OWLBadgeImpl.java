package owl.cs.evowl.util;

public class OWLBadgeImpl implements OWLBadge {

	private final String metric;
	private final String badgelocation;
	private final String badgename;

	public OWLBadgeImpl(String metric, String badgelocation, String badgename) {
		this.metric = metric;
		this.badgelocation = badgelocation;
		this.badgename = badgename;
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

	@Override
	public String getBadgeName() {
		return badgename;
	}
}
