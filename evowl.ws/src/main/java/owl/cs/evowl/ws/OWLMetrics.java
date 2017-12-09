package owl.cs.evowl.ws;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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

public class OWLMetrics {

	String url;
	/*
	 * boolean broken_link = false; boolean oa3 = false; boolean oa4 = false;
	 * boolean oa5 = false; boolean owl2 = false; boolean owl2dl = false; boolean
	 * owl2el = false; boolean owl2ql = false; boolean owl2rl = false; boolean rdfs
	 * = false; boolean nonempty = false;
	 */
	Map<String, String> data = new HashMap<>();
	Map<String,Map<String,String>> badges = new HashMap<>();

	public OWLMetrics(WSConfig conf, String oid) {
		url = oid;
		
		Map<String,String> nonempty = new HashMap<>();
		nonempty.put("true", "https://images.nature.com/lw300/nature-assets/bdjteam/2015/bdjteam201575/images_hires/bdjteam201575-i1.jpg");
		nonempty.put("false", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/Feedbin-Icon-check.svg/120px-Feedbin-Icon-check.svg.png");
		
		badges.put(EvOWLMetrics.URL_HEALTHY,nonempty);
		badges.put(EvOWLMetrics.NONEMPTY,nonempty);
		badges.put(EvOWLMetrics.OA3,nonempty);
		badges.put(EvOWLMetrics.OA4,nonempty);
		badges.put(EvOWLMetrics.OA5,nonempty);
		badges.put(EvOWLMetrics.OWL2,nonempty);
		badges.put(EvOWLMetrics.OWL2DL,nonempty);
		badges.put(EvOWLMetrics.OWL2EL,nonempty);
		badges.put(EvOWLMetrics.OWL2QL,nonempty);
		badges.put(EvOWLMetrics.OWL2RL,nonempty);
		badges.put(EvOWLMetrics.RDFS,nonempty);
	
		
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

			for (Iterator iterator = meas.iterator(); iterator.hasNext();) {
				
				JSONObject measure = (JSONObject) iterator.next();
				String metric = measure.get("metric").toString().replaceAll("\\\\", "");
				if(badges.keySet().contains(metric)) {
					data.put(metric, measure.get("value").toString());
				}
				/*
				 * if (metric.equals(EvOWLMetrics.NONEMPTY)) { broken_link =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OA3)) { oa3 =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OA4)) { oa4 =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OA5)) { oa5 =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OWL2)) { owl2 =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OWL2DL)) { owl2dl =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OWL2EL)) { owl2el =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OWL2QL)) { owl2ql =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.OWL2RL)) { owl2rl =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.RDFS)) { rdfs =
				 * measure.get("value").toString().equals("true"); } if
				 * (metric.equals(EvOWLMetrics.NONEMPTY)) { nonempty =
				 * measure.get("value").toString().equals("true"); }
				 */
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public JSONObject toJSONString() {
		JSONObject obj = new JSONObject();
		obj.put("oid", url);
		JSONArray badgesjson = new JSONArray();
		for (String metric : data.keySet()) {
			JSONObject o = new JSONObject();
			String value = data.get(metric);
			o.put("metric", metric);
			o.put("value", value);
			o.put("badge", badges.get(metric).get(value));
			badgesjson.add(o);
		}
		obj.put("badges", badgesjson);
		return obj;
	}

}
