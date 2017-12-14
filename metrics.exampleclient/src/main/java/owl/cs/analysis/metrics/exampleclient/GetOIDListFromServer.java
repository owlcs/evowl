package owl.cs.analysis.metrics.exampleclient;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import owl.cs.analysis.utilities.WSConfig;
import owl.cs.analysis.utilities.WebServiceBindings;

public class GetOIDListFromServer {
	
	ClientConfig config = new DefaultClientConfig();
	Client client = Client.create(config);
	final private String ws;

	public GetOIDListFromServer(String ws) {
		this.ws = ws;
	}


	public static void main(String[] args) {
			String ws = args[0];
			File filelist = new File(args[1]);
			GetOIDListFromServer g = new GetOIDListFromServer(ws);
			List<String> oids = new ArrayList<>(g.getOIDs());
			try {
				FileUtils.writeLines(filelist,oids);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public Set<String> getOIDs() {
		Set<String> oids = new HashSet<>();
		try {
			WSConfig conf = new WSConfig(ws,"8080");
			String q = conf.getBaseAdress(WebServiceBindings.GETOIDS);
			System.out.println(q);

			JSONObject jsonroot = getJSONResponse(q);
			JSONArray meas = (JSONArray) jsonroot.get("oids");

			for (@SuppressWarnings("unchecked")
			Iterator<String> iterator = meas.iterator(); iterator.hasNext();) {
				String oid = iterator.next();
				System.out.println(oid);
				oids.add(oid);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oids;
	}
	
	private JSONObject getJSONResponse(String q) throws IOException, ParseException {
		WebResource webResource = client.resource(q);
		// webResource.queryParam("url", URLEncoder.encode(q, "UTF-8"));
		ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);

		String output = response.getEntity(String.class);
		System.out.println(output);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new StringReader(output));
		JSONObject jsonroot = (JSONObject) obj;
		return jsonroot;
	}
}