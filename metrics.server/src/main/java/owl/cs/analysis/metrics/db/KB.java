package owl.cs.analysis.metrics.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import owl.cs.analysis.utilities.OntologyBinding;

public class KB {

	private static KB instance = null;
	Repository db = null;
	int TRUNCATESTRING = 300;

	protected KB() {
		File dataDir = new File("triplestore");
		db = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore(dataDir)));
		db.initialize();
	}

	public static KB getInstance() {
		if (instance == null) {
			instance = new KB();
		}
		return instance;
	}

	public void rdfXMLInputStreamToSesame(InputStream input) {
		// Open a connection to the database
		if (!isValidRDFXMLInputStream(input)) {
			throw new IllegalArgumentException("Not a valid input stream");
		}
		try (RepositoryConnection conn = db.getConnection()) {
			try {
				// ByteArrayOutputStream os = new ByteArrayOutputStream();
				// o.getOWLOntologyManager().saveOntology(o, os);
				// ByteArrayInputStream input = new ByteArrayInputStream( os.toByteArray() );
				conn.add(input, "", RDFFormat.RDFXML);
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
		
		String query = "SELECT ?measure ?metric ?value ?instrument ?date "
				+ "WHERE " + "{ " 
				+ "<" + url + "> <"+ OntologyBinding.getHasMeasurementIRI() + "> ?measure . " 
				+ "?measure rdf:type ?metric . "
				+ "?measure <" + OntologyBinding.getHasMeasurementValueIRI() + "> ?value . " 
				+ "?measure <" + OntologyBinding.getHasMeasurementInstrumentIRI() + "> ?instrument . " 
				+ "?measure <" + OntologyBinding.getHasRecordingDateIRI() + "> ?date . " 
				+ " }";

		List<BindingSet> results = new ArrayList<>();
		results.addAll(Repositories.tupleQuery(db, query, r -> QueryResults.asList(r)));

		// System.out.println(results.size()+"################");

		for (BindingSet bs : results) {
			JSONObject measure = new JSONObject();
			Value measurei = bs.getValue("measure");
			Value metrici = bs.getValue("metric");
			Value valuei = bs.getValue("value");
			Value instrumenti = bs.getValue("instrument");
			Value datei = bs.getValue("date");
			measure.put("measureid", measurei.stringValue());
			measure.put("metric", metrici.stringValue());
			measure.put("value", jsonpreprocess(valuei.stringValue()));
			measure.put("instrument", instrumenti.stringValue());
			measure.put("recordingdate", datei.stringValue());
			measures.add(measure);
		}
		obj.put("measurements", measures);
		return obj;
	}

	public JSONObject getIndexedOntologies() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();

		String query = "SELECT ?oid WHERE " + "{" + "?oid rdf:type <" + OntologyBinding.getOntologyDeployLocationClass()
				+ "> . " + "}";
		List<BindingSet> results = Repositories.tupleQuery(db, query, r -> QueryResults.asList(r));
		// System.out.println(results.size()+"################");
		for (BindingSet bs : results) {
			Value oid = bs.getValue("oid");
			arr.add(oid.stringValue());
		}
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
		String s = stringValue.length()<=TRUNCATESTRING? stringValue : stringValue.substring(0, TRUNCATESTRING)+" [...]";
		return JSONValue.escape(s);
	}

}
