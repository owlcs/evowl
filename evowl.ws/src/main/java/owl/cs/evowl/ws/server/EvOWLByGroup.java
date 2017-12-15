package owl.cs.evowl.ws.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import owl.cs.analysis.utilities.WSConfig;
import owl.cs.evowl.ws.db.OWLMetrics;


@Path("/evowl/group")
public class EvOWLByGroup {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getEvowlByGroupPlainText(@QueryParam("group") String group) {
		return getMetrics(group);
	}

	private String getMetrics(String group) {
		OWLMetrics m = new OWLMetrics(new WSConfig());
		return m.getOWLMetricsByGroup(group).toString().replaceAll("\\\\", "");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getEvowlByGroup(@QueryParam("group") String group) {
		return getMetrics(group);
	}
}