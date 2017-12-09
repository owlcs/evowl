package owl.cs.analysis.metrics.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import owl.cs.analysis.metrics.db.KB;
import owl.cs.analysis.utilities.StringUtilities;

@Path("/add/url")
public class RESTfulOwlMetricsServerAddUrl {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String plainTextOA3(@QueryParam("url") String url) {
		if (addUrlToKB(url)) {
			return url + " added to repository. Please wait for analysis job to finish.";
		} else {
			return url + " was NOT added.";
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public String xmlOA3(@QueryParam("url") String url) {
		return processWithXHTMLResponse(url);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String htmlOA3(@QueryParam("url") String url) {
		return processWithXHTMLResponse(url);
	}
	
	private boolean addUrlToKB(String url) {
		return KB.getInstance().addUrl(url);
	}


	private String processWithXHTMLResponse(String url) {
		if (addUrlToKB(url)) {
			return StringUtilities
					.formatSimpleXHTMLMessage(url + " added to repository. Please wait for analysis job to finish.");
		} else {
			return StringUtilities
					.formatSimpleXHTMLMessage(url + " was NOT added.");
		}
	}
}