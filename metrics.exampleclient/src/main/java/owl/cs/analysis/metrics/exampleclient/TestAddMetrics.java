package owl.cs.analysis.metrics.exampleclient;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestAddMetrics {

  public static void main(String[] args) {
	try {
		String q = "http://purl.obolibrary.org/obo/aeo.owl";
		Client client = Client.create();

		WebResource webResource = client
		   .resource("http://localhost:8080/owlapi3/rest/?url="+q);
		//webResource.queryParam("url", URLEncoder.encode(q, "UTF-8"));
		ClientResponse response = webResource.accept("text/plain")
                   .get(ClientResponse.class);

		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}

		String output = response.getEntity(String.class);

		System.out.println("Output from Server .... \n");
		System.out.println(output);

	  } catch (Exception e) {

		e.printStackTrace();

	  }

	}
}