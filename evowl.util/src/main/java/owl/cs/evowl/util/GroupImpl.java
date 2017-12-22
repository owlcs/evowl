package owl.cs.evowl.util;

import java.net.URI;

public class GroupImpl implements Group {

	final private String groupid;
	final private String name;

	public GroupImpl(String g) {
		groupid = g;
		name = URI.create(g).getFragment();
	}

	@Override
	public Integer getOntologyCount() {
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getGroupID() {
		return groupid;
	}

}
