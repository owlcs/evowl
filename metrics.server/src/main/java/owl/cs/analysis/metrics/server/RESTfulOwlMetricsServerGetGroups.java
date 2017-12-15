package owl.cs.analysis.metrics.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import owl.cs.analysis.metrics.db.KB;

@Path("/get/groups")
public class RESTfulOwlMetricsServerGetGroups {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String plainTextOA3() {
		return getGroups();
	}

	private String getGroups() {
		return KB.getInstance().getGroups().toJSONString().replaceAll("\\\\", "");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String xmlOA3() {
		return getGroups();	
	}
}