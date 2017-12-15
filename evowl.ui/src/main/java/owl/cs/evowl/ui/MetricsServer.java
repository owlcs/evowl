package owl.cs.evowl.ui;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import owl.cs.analysis.utilities.WSConfig;
import owl.cs.analysis.utilities.WebServiceBindings;
import owl.cs.evowl.util.EvOWLMetrics;
import owl.cs.evowl.util.OWLBadge;
import owl.cs.evowl.util.Ontology;
import owl.cs.evowl.util.OntologyImpl;

public class MetricsServer {

	private final WSConfig conf;
	Client client = Client.create();

	MetricsServer(WSConfig conf) {
		this.conf = conf;
	}

	public Set<String> getOIDs() {
		Set<String> oids = new HashSet<>();
		try {

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

	public Ontology getOntologyMetrics(String url) {
		Ontology o = new OntologyImpl(url);
		try {

			String q = conf.getBaseAdress(WebServiceBindings.GETMEASUREMENTS) + "?url=" + url.replace("#", "%23");
			System.out.println(q);

			JSONObject jsonroot = getJSONResponse(q);
			JSONArray meas = (JSONArray) jsonroot.get("measurements");

			for (Iterator iterator = meas.iterator(); iterator.hasNext();) {
				JSONObject measure = (JSONObject) iterator.next();
				String metric = measure.get("metric").toString();
				String value = measure.get("value").toString();
				o.setMetric(metric, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	public List<Ontology> getOntologyEvowl(String group) {
		List<Ontology> os = new ArrayList<>();

		try {

			String q = conf.getBaseAdress(WebServiceBindings.EVOWLBYGROUP) + "?group=" + group.replace("#", "%23");
			System.out.println(q);

			JSONObject jsonroot = getJSONResponse(q);

			JSONArray onts = (JSONArray) jsonroot.get("ontologies");

			Iterator ontit = onts.iterator();
			while (ontit.hasNext()) {
				JSONObject ont = (JSONObject) ontit.next();

				String oid = ont.get("oid").toString();

				Ontology o = new OntologyImpl(oid);

				if (ont.containsKey("mainbadge")) {
					// TODO make sure it does
					JSONObject jsonmain = (JSONObject) ont.get("mainbadge");
					OWLBadge b = parseBadge(jsonmain);
					o.setMainBadge(b);
				}

				JSONArray meas = (JSONArray) ont.get("badges");

				for (Iterator iterator = meas.iterator(); iterator.hasNext();) {
					JSONObject badgeo = (JSONObject) iterator.next();
					o.addBadge(parseBadge(badgeo));
				}
				os.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return os;
	}

	private OWLBadge parseBadge(JSONObject jsonmain) {
		String metric = jsonmain.get("metric").toString();
		return EvOWLMetrics.getBadge(metric);
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

	public Collection<String> getGroups() {
		Set<String> oids = new HashSet<>();
		try {

			String q = conf.getBaseAdress(WebServiceBindings.GETGROUPS);
			System.out.println(q);

			JSONObject jsonroot = getJSONResponse(q);
			JSONArray meas = (JSONArray) jsonroot.get("groups");

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

}
