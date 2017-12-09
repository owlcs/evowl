package owl.cs.evowl.util;

import java.util.Collection;

public interface Ontology {
	public String getDownloadURL();
	public String getMainBadgeLocation();
	public OWLBadge getEvowlMainBadge();
	public void addBadge(OWLBadge b);
	public void setMetric(String metric, String value);
	public void setMainBadge(OWLBadge b);
	public Collection<OWLBadge> getBadges();

}
