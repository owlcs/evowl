package owl.cs.analysis.metrics.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import owl.cs.analysis.metrics.db.KB;

@Path("/get/measurements/group")
public class RESTfulOwlMetricsServerGetMetricsByGroup {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String plainTextOA3(@QueryParam("group") String group) {
		return getMetrics(group);
	}

	private String getMetrics(String group) {
		return KB.getInstance().getMetricsByGroup(group).toJSONString().replaceAll("\\\\", "");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String xmlOA3(@QueryParam("group") String group) {
		return getMetrics(group);	
	}
}