package owl.cs.analysis.utilities;

import java.io.File;

public abstract class MetricsServerAppRunner extends AppRunner {

	String url = null;

	protected void prepareBasic(String[] args, String appname) {

		if (args.length != 6) {
			throw new IllegalArgumentException(
					"You need exactly 5 parameters (Ontology IRI, Ontology directory, Group, Output directory, overwrite condition, timeout in milliseconds");
		}

		String url = args[0];
		File ontology = new File(args[1], AppUtils.getFilenameFromURL(url));
		String group = args[2];
		File export = new File(args[3], appname + "." + ontology.getName() + ".rdf");
		boolean run = shouldRun(args[4], export);
		int timeout = Integer.valueOf(args[5]);

		setURL(url);
		setProcessTimeout(timeout);
		setCSVFile(export);
		setOntologyFile(ontology);
		setShouldOntologyFileExistst(!appname.contains("url"));
		setShouldRun(run);
		setGroup(group);

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
