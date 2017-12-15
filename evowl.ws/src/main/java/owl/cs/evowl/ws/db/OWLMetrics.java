package owl.cs.evowl.ws.db;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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

public class OWLMetrics {

	Client client = Client.create();
	final private WSConfig conf;

	private String OAHARVEST = "metrics.harvest.oa3-0.0.1-jar-with-dependencies.jar";
	private String REASONERHARVEST = "metrics.harvest.hermit-0.0.1-jar-with-dependencies.jar";

	public OWLMetrics(WSConfig conf) {
		this.conf = conf;
	}

	public JSONObject getOWLMetricsByGroup(String group) {
		List<Ontology> ontologies = new ArrayList<>();

		String q = conf.getBaseAdress(WebServiceBindings.GETMEASUREMENTSBYGROUP) + "?group=" + group.replace("#", "%23");
		System.out.println(q);

		WebResource webResource = client.resource(q);
		// webResource.queryParam("url", URLEncoder.encode(q, "UTF-8"));
		ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		System.out.println(output);
		System.out.println("#########");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new StringReader(output));

			JSONObject jsonroot = (JSONObject) obj;

			JSONArray onts = (JSONArray) jsonroot.get("ontologies");
			Iterator it = onts.iterator();
			while (it.hasNext()) {
				JSONObject o = (JSONObject) it.next();
				ontologies.add(extractOntology(o.get("oid").toString(), (JSONArray) o.get("measurements")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toJSONString(ontologies, group);
	}

	private JSONObject toJSONString(List<Ontology> ontologies, String group) {
		JSONObject o = new JSONObject();
		o.put("group", group);
		JSONArray oa = new JSONArray();
		ontologies.forEach(e -> oa.add(toJSONString(e)));
		o.put("ontologies", oa);
		return o;
	}

	public JSONObject getOWLMetricsByOid(String oid) {
		Ontology o = new OntologyImpl(oid);

		String q = conf.getBaseAdress(WebServiceBindings.GETMEASUREMENTS) + "?url=" + oid;
		System.out.println(q);

		WebResource webResource = client.resource(q);
		// webResource.queryParam("url", URLEncoder.encode(q, "UTF-8"));
		ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		System.out.println(output);
		System.out.println("#########");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new StringReader(output));

			JSONObject jsonroot = (JSONObject) obj;

			JSONArray meas = (JSONArray) jsonroot.get("measurements");
			o = extractOntology(oid, meas);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toJSONString(o);
	}

	private Ontology extractOntology(String oid, JSONArray meas) {
		Ontology o = new OntologyImpl(oid);

		boolean url_assessed = false;

		Set<String> metrics = new HashSet<>();

		for (Iterator<Object> iterator = meas.iterator(); iterator.hasNext();) {

			JSONObject measure = (JSONObject) iterator.next();
			String metric = measure.get("metric").toString().replaceAll("\\\\", "");
			System.out.println("LOOP METRICS #####################");

			if (EvOWLMetrics.badges.keySet().contains(metric)) {
				String val = measure.get("value").toString();
				String instrument = measure.get("instrument").toString();
				System.out.println(val);
				if (evowlMetric(metric, instrument)) {
					if (val.equals("true")) {
						metrics.add(metric);
					}
					if (metric.equals(EvOWLMetrics.URL_HEALTHY)) {
						url_assessed = true;
					}
				}
			}

		} // end looping all measurements

		if (url_assessed) {
			o.setMainBadge(getMainBadge(metrics));
			metrics.forEach(m -> o.addBadge(EvOWLMetrics.getBadge(m)));
		} else {
			System.out.println("URL not evaluated yet, no judgement possible.");
		}
		return o;
	}

	private boolean evowlMetric(String metric, String instrument) {
		return instrument.equals(OAHARVEST) || instrument.equals(REASONERHARVEST)
				|| EvOWLMetrics.generalMetrics.contains(metric);
	}

	private OWLBadge getMainBadge(Set<String> metrics) {
		if (metrics.contains(EvOWLMetrics.URL_HEALTHY) && (metrics.contains(EvOWLMetrics.OA3)
				|| metrics.contains(EvOWLMetrics.OA4) || metrics.contains(EvOWLMetrics.OA5))) {
			if (metrics.contains(EvOWLMetrics.NONEMPTY) && metrics.contains(EvOWLMetrics.OWL2)) {
				if (metrics.contains(EvOWLMetrics.OWL2DL)) {
					if (metrics.contains(EvOWLMetrics.CONSISTENT)) {
						return EvOWLMetrics.getBadge(EvOWLMetrics.MAIN_GOLD);
					} else {
						return EvOWLMetrics.getBadge(EvOWLMetrics.MAIN_SILVER);
					}
				} else {
					return EvOWLMetrics.getBadge(EvOWLMetrics.MAIN_BRONZE);
				}
			} else {
				return EvOWLMetrics.getBadge(EvOWLMetrics.MAIN_GREY);
			}
		} else {
			return EvOWLMetrics.getBadge(EvOWLMetrics.MAIN_BROKEN);
		}
	}

	private JSONObject toJSONString(Ontology o) {
		JSONObject obj = new JSONObject();
		obj.put("oid", o.getDownloadURL());
		JSONArray badgesjson = new JSONArray();
		for (OWLBadge b : o.getBadges()) {
			badgesjson.add(toJSONString(b));
		}
		obj.put("badges", badgesjson);
		obj.put("mainbadge", toJSONString(o.getEvowlMainBadge()));
		return obj;
	}

	private JSONObject toJSONString(OWLBadge b) {
		JSONObject badge = new JSONObject();
		badge.put("metric", b.getMetric());
		badge.put("badge", b.getBadgeLocation());
		badge.put("name", b.getBadgeName());
		return badge;
	}

}
