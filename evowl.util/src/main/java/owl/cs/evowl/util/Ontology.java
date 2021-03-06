package owl.cs.evowl.util;

import java.util.Collection;

public interface Ontology {
	public String getDownloadURL();
	public String getMainBadgeLocation();
	public OWLBadge getEvowlMainBadge();
	public void addBadge(OWLBadge b);
	public void setMetric(Metric metric);
	public void setMainBadge(OWLBadge b);
	public Collection<OWLBadge> getBadges();
	public Collection<Metric> getMetrics();

}
