package owl.cs.analysis.metrics.server;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import owl.cs.analysis.metrics.db.KB;

@Path("/add/metrics")
public class RESTfulOwlMetricsServerAddMetrics {
	@POST
	@Consumes(MediaType.TEXT_XML)
	public Response crunchifyREST(@QueryParam("format") String format,InputStream data) {
		KB.getInstance().rdfXMLInputStreamToSesame(data,format);
		return Response.status(200).entity(data.toString()).build();
	}

}