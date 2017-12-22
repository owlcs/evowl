package owl.cs.analysis.metrics.harvest.meta.obo;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

public class TEST {

	public static void main(String[] args) {
		try {
			URL obourl = new URL("http://www.obofoundry.org/registry/ontologies.jsonld");
			InputStream inputStream = obourl.openStream();
			Object jsonObject = JsonUtils.fromInputStream(inputStream);
			Map context = new HashMap();
			JsonLdOptions options = new JsonLdOptions();
			Object compact = JsonLdProcessor.compact(jsonObject, context, options);
			LinkedHashMap map = (LinkedHashMap)compact;
			for(Object key:map.keySet()) {
				System.out.println(key.toString());
				System.out.println(map.get(key).getClass());
				List allontologies = (ArrayList)map.get(key);
				System.out.println(JsonUtils.toPrettyString(compact));
				System.exit(0);
				for (Object o:allontologies) {
					LinkedHashMap ont = (LinkedHashMap)o;
					System.out.println(ont);
					String id = ont.containsKey("@id") ? ont.get("@id").toString() : null;
					String url =  ont.containsKey("http://www.w3.org/ns/dcat#accessURL") ? ont.get("http://www.w3.org/ns/dcat#accessURL").toString() : null;
					String title =  ont.containsKey("http://purl.org/dc/elements/1.1/title") ? ont.get("http://purl.org/dc/elements/1.1/title").toString() : null;
					String desc =  ont.containsKey("http://purl.org/dc/elements/1.1/description") ? ont.get("http://purl.org/dc/elements/1.1/description").toString() : null;
					String theme =  ont.containsKey("http://purl.org/dc/terms/1.1/theme") ? ont.get("http://purl.org/dc/terms/1.1/theme").toString() : null;
					String homepage =  ont.containsKey("http://xmlns.com/foaf/0.1/homepage") ? ((LinkedHashMap)ont.get("http://xmlns.com/foaf/0.1/homepage")).get("@id").toString() : null;
				}
				System.out.println(JsonUtils.toPrettyString(compact));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
