package owl.cs.evowl.util;

public class MetricImpl implements Metric {

	private final String id;
	private final String value;

	public MetricImpl(String metric, String value) {
		this.id = metric;
		this.value = value;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getValue() {
		return value;
	}

}
