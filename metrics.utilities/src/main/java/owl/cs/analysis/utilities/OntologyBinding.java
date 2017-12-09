package owl.cs.analysis.utilities;


public class OntologyBinding {

	public static String ONTOLOGYIRI = "http://ontothenextone.net/mowl.owl";
	public static String HASMEASUREMENT = "hasMeasurement";
	public static String HASMEASUREMENTVALUE = "hasMeasurementValue";
	public static String ONTOLOGYDEPLOYLOCATIONCLASS = "OntologyDeployLocation";

	public static String entityIRI(String value) {
		return ONTOLOGYIRI+"#"+value;
	}

	public static String getHasMeasurementIRI() {
		return entityIRI(HASMEASUREMENT);
	}

	public static String getHasMeasurementValueIRI() {
		return entityIRI(HASMEASUREMENTVALUE);
	}

	public static String getOntologyDeployLocationClass() {
		return entityIRI(ONTOLOGYDEPLOYLOCATIONCLASS);
	}
	
}
