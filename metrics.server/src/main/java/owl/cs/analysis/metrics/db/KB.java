package owl.cs.analysis.metrics.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import owl.cs.analysis.utilities.OntologyBinding;

public class KB {

	private static String store = "http://localhost:8080/rdf4j-server/repositories/evowl_one";
	private static KB instance = null;
	Repository db = null;
	int TRUNCATESTRING = 300;

	protected KB() {
		File dataDir = new File("triplestore");
		String serverUrl = "http://localhost:8080/rdf4j-server";
		RemoteRepositoryManager manager = new RemoteRepositoryManager(serverUrl);
		manager.initialize();

		db = manager.getRepository("evowl_one");
		// new SailRepository(new ForwardChainingRDFSInferencer(new
		// MemoryStore(dataDir)));
		db.initialize();
	}

	public static KB getInstance() {
		if (instance == null) {
			instance = new KB();
		}
		return instance;
	}

	public void rdfXMLInputStreamToSesame(InputStream input, String format, String con) {
		long e1 = System.currentTimeMillis();
		IRI context = db.getValueFactory().createIRI(con);
		// Open a connection to the database
		if (!isValidRDFXMLInputStream(input)) {
			throw new IllegalArgumentException("Not a valid input stream");
		}
		try (RepositoryConnection conn = db.getConnection()) {
			try {
				long e2 = System.currentTimeMillis();
				System.out.println(".. done (" + (e2 - e1) + "). Clear context..");
				conn.clear(context);

				RDFFormat f = RDFFormat.RDFXML;

				if (format.equals("ttl")) {
					f = RDFFormat.TURTLE;
				}
				long e3 = System.currentTimeMillis();
				System.out.println(".. done (" + (e3 - e2) + "). Adding input..");
				conn.add(input, "", f, context);
				long e4 = System.currentTimeMillis();
				System.out.println(".. done (" + (e4 - e3) + ")");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private boolean isValidRDFXMLInputStream(InputStream input) {
		// TODO Auto-generated method stub
		System.err.println("isValidRDFXMLInputStream() should be implemented, but is not.");
		return true;
	}

	public boolean addUrl(String url) {
		ValueFactory factory = SimpleValueFactory.getInstance();
		Statement nameStatement = factory.createStatement(factory.createIRI(url), RDF.TYPE,
				factory.createIRI(OntologyBinding.getOntologyDeployLocationClass()));

		try (RepositoryConnection conn = db.getConnection()) {
			conn.add(nameStatement);
		}

		return true;
	}

	public JSONObject getMetrics(String url) {
		JSONObject obj = new JSONObject();
		obj.put("oid", url);
		JSONArray measures = new JSONArray();

		// TODO: add triple pattern: ?metric rdf:type <http:\\metricclass>

		String query = "SELECT ?measure ?metric ?value ?instrument ?date ?metric_label " + "WHERE " + "{ " + "<" + url
				+ "> <" + OntologyBinding.getHasMeasurementIRI() + "> ?measure . " + "?measure rdf:type ?metric . "
				+ "?metric rdfs:subClassOf <" + OntologyBinding.getMeasurementClass() + "> ." + "?metric <"
				+ OntologyBinding.getMachineReadableIRI() + "> ?metric_label ." + "?measure <"
				+ OntologyBinding.getHasMeasurementValueIRI() + "> ?value . " + "?measure <"
				+ OntologyBinding.getHasMeasurementInstrumentIRI() + "> ?instrument . " + "?measure <"
				+ OntologyBinding.getHasRecordingDateIRI() + "> ?date . " + "FILTER (?metric != <"
				+ OntologyBinding.getMeasurementClass() + ">) " + " }";

		List<BindingSet> results = new ArrayList<>();
		results.addAll(Repositories.tupleQuery(db, query, r -> QueryResults.asList(r)));

		System.out.println(results.size() + "################");

		for (BindingSet bs : results) {
			JSONObject measure = new JSONObject();
			Value measurei = bs.getValue("measure");
			Value metrici = bs.getValue("metric");
			Value valuei = bs.getValue("value");
			Value instrumenti = bs.getValue("instrument");
			Value metric_labeli = bs.getValue("metric_label");
			Value datei = bs.getValue("date");
			measure.put("measureid", measurei.stringValue());
			measure.put("metric", metrici.stringValue());
			measure.put("value", jsonpreprocess(valuei.stringValue()));
			measure.put("instrument", instrumenti.stringValue());
			measure.put("recordingdate", datei.stringValue());
			measure.put("metric_label", metric_labeli.stringValue());
			measures.add(measure);
		}
		obj.put("measurements", measures);
		return obj;
	}

	public JSONObject getIndexedOntologies() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();

		String query = "SELECT DISTINCT ?oid WHERE " + "{" + "?oid rdf:type <"
				+ OntologyBinding.getOntologyDeployLocationClass() + "> . " + "}";
		List<BindingSet> results = Repositories.tupleQuery(db, query, r -> QueryResults.asList(r));
		// System.out.println(results.size()+"################");
		Set<String> bss = new HashSet<>();
		for (BindingSet bs : results) {
			Value oid = bs.getValue("oid");
			bss.add(oid.stringValue());
		}
		bss.forEach(e -> arr.add(e));
		obj.put("oids", arr);
		return obj;
	}

	public JSONObject getAllTriples() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();

		String query = "SELECT ?x ?y ?z  WHERE " + "{" + "?x ?y ?z . " + "}";

		List<BindingSet> results = Repositories.tupleQuery(db, query, r -> QueryResults.asList(r));
		// System.out.println(results.size()+"################");
		for (BindingSet bs : results) {
			JSONObject trip = new JSONObject();
			Value x = bs.getValue("x");
			Value y = bs.getValue("y");
			Value z = bs.getValue("z");
			trip.put("subject", x.stringValue());
			trip.put("relation", y.stringValue());
			trip.put("object", jsonpreprocess(z.stringValue()));
			arr.add(trip);
		}
		obj.put("oids", arr);
		return obj;
	}

	private String jsonpreprocess(String stringValue) {
		String s = stringValue.length() <= TRUNCATESTRING ? stringValue
				: stringValue.substring(0, TRUNCATESTRING) + " [...]";
		return JSONValue.escape(s);
	}

	public JSONObject getMetricsByGroup(String group) {

		String filter = !validURI(group) ? OntologyBinding.entityIRI("default") : group;

		JSONObject obj = new JSONObject();
		
		obj.put("group", filter);

		String query = "SELECT ?measure ?metric ?value ?instrument ?date ?metric_label ?o " + "WHERE " + "{ " + "?o <"
				+ OntologyBinding.gethasGroupIRI() + "> <" + filter + "> . " 
				+ "?o <http://ontothenextone.net/ev.owl#hasMeasurement> ?measure . " 
				+ "?measure rdf:type ?metric . "
				+ "?metric rdfs:subClassOf <" + OntologyBinding.getMeasurementClass() + "> . " 
				+ "?metric <" + OntologyBinding.getMachineReadableIRI() + "> ?metric_label . " 
				+ "?measure <" + OntologyBinding.getHasMeasurementValueIRI() + "> ?value . " 
				+ "?measure <" + OntologyBinding.getHasMeasurementInstrumentIRI() + "> ?instrument . " 
				+ "?measure <"+ OntologyBinding.getHasRecordingDateIRI() + "> ?date . " 
				//+ "FILTER (?metric != <"+ OntologyBinding.getMeasurementClass() + ">) " 
				+ " }";
		
		

		System.out.println(query);
		
		List<BindingSet> results = new ArrayList<>();
		results.addAll(Repositories.tupleQuery(db, query, r -> QueryResults.asList(r)));

		System.out.println(results.size() + "################");

	

		Map<String, JSONArray> measures_by_oid = new HashMap<>();

		for (BindingSet bs : results) {
			String oi = bs.getValue("o").stringValue();
			if (!measures_by_oid.containsKey(oi)) {
				measures_by_oid.put(oi, new JSONArray());
			}
			JSONArray measures = measures_by_oid.get(oi);
			JSONObject measure = new JSONObject();

			Value measurei = bs.getValue("measure");
			Value metrici = bs.getValue("metric");
			Value valuei = bs.getValue("value");
			Value instrumenti = bs.getValue("instrument");
			Value metric_labeli = bs.getValue("metric_label");
			Value datei = bs.getValue("date");
			measure.put("measureid", measurei.stringValue());
			measure.put("metric", metrici.stringValue());
			measure.put("value", jsonpreprocess(valuei.stringValue()));
			measure.put("instrument", instrumenti.stringValue());
			measure.put("recordingdate", datei.stringValue());
			measure.put("metric_label", metric_labeli.stringValue());
			measures.add(measure);
		}
		JSONArray ontsjson = new JSONArray();
		for (String oid : measures_by_oid.keySet()) {
			JSONObject ontobj = new JSONObject();
			ontobj.put("oid", oid);
			ontobj.put("measurements", measures_by_oid.get(oid));
			ontsjson.add(ontobj);
		}
		obj.put("ontologies", ontsjson);

		return obj;
	}

	private boolean validURI(String group) {
		System.err.println("KB.validURI() not implemented..");
		return true;
	}

	public JSONObject getGroups() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();

		String query = "SELECT DISTINCT ?group WHERE " + "{" + "?oid <" + OntologyBinding.gethasGroupIRI()
				+ "> ?group . " + "}";
		List<BindingSet> results = Repositories.tupleQuery(db, query, r -> QueryResults.asList(r));
		// System.out.println(results.size()+"################");
		Set<String> bss = new HashSet<>();
		for (BindingSet bs : results) {
			Value oid = bs.getValue("group");
			bss.add(oid.stringValue());
		}
		bss.forEach(e -> arr.add(e));
		obj.put("groups", arr);
		return obj;
	}

}
