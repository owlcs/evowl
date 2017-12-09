package owl.cs.evowl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import owl.cs.analysis.utilities.WSConfig;


@Path("/evowl")

public class EvOWLWS {

	public static void main(String[] args) {
		System.out.println(new EvOWLWS().getMetrics("http://purl.obolibrary.org/obo/bco.owl"));
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String plainTextOA3(@QueryParam("url") String url) {
		return getMetrics(url);
	}

	private String getMetrics(String url) {
		OWLMetrics m = new OWLMetrics(new WSConfig(),url);
		return m.toJSONString().toString().replaceAll("\\\\", "");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String xmlOA3(@QueryParam("url") String url) {
		return getMetrics(url);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String htmlOA3(@QueryParam("url") String url) {
		return getMetrics(url);
	}
}