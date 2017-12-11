package owl.cs.analysis.utilities;

public class OntologyBinding {

	public static String ONTOLOGYIRI = "http://ontothenextone.net/mowl.owl";
	public static String HASMEASUREMENT = "hasMeasurement";
	public static String CL_MEASUREMENT = "Measurement";
	public static String HASMEASUREMENTVALUE = "hasMeasurementValue";
	public static String HASMEASUREMENTINSTRUMENT = "hasMeasurementInstrument";
	public static String HASRECORDINGDATE = "hasRecordingDate";
	public static String CL_MACHINEREADABLE = "machineReadableLabel";
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

	public static String getHasMeasurementInstrumentIRI() {
		return entityIRI(HASMEASUREMENTINSTRUMENT);
	}

	public static String getHasRecordingDateIRI() {
		return entityIRI(HASRECORDINGDATE);
	}

	public static String getMeasurementClass() {
		return entityIRI(CL_MEASUREMENT);
	}

	public static String getMachineReadableIRI() {
		return entityIRI(CL_MACHINEREADABLE);
	}
	
}
