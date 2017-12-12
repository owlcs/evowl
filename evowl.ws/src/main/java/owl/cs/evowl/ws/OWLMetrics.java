package owl.cs.evowl.ws;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import owl.cs.analysis.utilities.WSConfig;
import owl.cs.analysis.utilities.WebServiceBindings;
import owl.cs.evowl.util.EvOWLMetrics;
import owl.cs.evowl.util.OWLBadge;
import owl.cs.evowl.util.OWLBadgeImpl;

public class OWLMetrics {

	String url;
	Set<String> metrics = new HashSet<>();
	List<OWLBadge> data = new ArrayList<>();
	OWLBadge mainbadge = new OWLBadgeImpl(EvOWLMetrics.MAIN_UNEVALUATED,
			EvOWLMetrics.badges.get(EvOWLMetrics.MAIN_UNEVALUATED));
	private String OAHARVEST = "metrics.harvest.oa3-0.0.1-jar-with-dependencies.jar";
	private String REASONERHARVEST = "metrics.harvest.hermit-0.0.1-jar-with-dependencies.jar";

	public OWLMetrics(WSConfig conf, String oid) {
		url = oid;

		try {
			Client client = Client.create();

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
			Object obj = parser.parse(new StringReader(output));
			JSONObject jsonroot = (JSONObject) obj;

			JSONArray meas = (JSONArray) jsonroot.get("measurements");

			boolean url_assessed = false;

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
							data.add(new OWLBadgeImpl(metric, EvOWLMetrics.badges.get(metric)));
							metrics.add(metric);
						}
						if (metric.equals(EvOWLMetrics.URL_HEALTHY)) {
							url_assessed = true;
						}
					}
				}
			}

			if (url_assessed) {
				mainbadge = getMainBadge();
			} else {
				System.out.println("URL not evaluated yet, no judgement possible.");
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private boolean evowlMetric(String metric, String instrument) {
		return instrument.equals(OAHARVEST) || instrument.equals(REASONERHARVEST)
				|| EvOWLMetrics.generalMetrics.contains(metric);
	}

	private OWLBadge getMainBadge() {
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

	public JSONObject toJSONString() {
		JSONObject obj = new JSONObject();
		obj.put("oid", url);
		JSONArray badgesjson = new JSONArray();
		for (OWLBadge b : data) {
			JSONObject o = new JSONObject();
			o.put("metric", b.getMetric());
			o.put("badge", b.getBadgeLocation());
			badgesjson.add(o);
		}
		obj.put("badges", badgesjson);

		JSONObject mainbadge = new JSONObject();
		mainbadge.put("metric", this.mainbadge.getMetric());
		mainbadge.put("badge", this.mainbadge.getBadgeLocation());
		obj.put("mainbadge", mainbadge);
		return obj;
	}

}
