package owl.cs.analysis.metrics.exampleclient;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class PostMetricsToServer {

	public static void main(String[] args) {
		try {
			File f = new File(args[0]);
			String ws = args[1];

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource service = client.resource(ws);
			String response = service.type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).entity(f).post(String.class);
			System.out.println(response);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}