package owl.cs.analysis.utilities;

import java.io.File;

public abstract class MetricsServerAppRunner extends AppRunner {

	String url = null;

	protected void prepareBasic(String[] args, String appname) {

		if (args.length != 5) {
			throw new IllegalArgumentException(
					"You need exactly 5 parameters (Ontology IRI, Ontology directory, Output directory, overwrite condition, timeout in milliseconds");
		}

		String url = args[0];
		File ontology = new File(args[1], AppUtils.getFilenameFromURL(url));
		File export = new File(args[2], appname + "." + ontology.getName() + ".rdf");
		boolean run = shouldRun(args[3], export);
		int timeout = Integer.valueOf(args[4]);

		setURL(url);
		setProcessTimeout(timeout);
		setCSVFile(export);
		setOntologyFile(ontology);
		setShouldOntologyFileExistst(!appname.contains("url"));
		setShouldRun(run);

	}

	private boolean shouldRun(String param, File export) {
		return export.exists() ? param.equals("o") : true;
	}

	public static void run(String[] args, AppRunner runner) {
		App experiment = runner.configureExperiment(args);
		runner.runExperiment(experiment);
	}

	private void setURL(String url) {
		this.url = url;
	}

	public String getURL() {
		return url;
	}

}
