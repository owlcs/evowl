package owl.cs.analysis.utilities;

import java.io.File;

public abstract class MetadataHarvestAppRunner extends AppRunner {

	protected void prepareBasic(String[] args, String appname) {

		if (args.length != 2) {
			throw new IllegalArgumentException(
					"You need exactly 2 parameters (Output directory, timeout in milliseconds");
		}

		File export = new File(args[0], appname + ".rdf");
		int timeout = Integer.valueOf(args[1]);

		setProcessTimeout(timeout);
		setCSVFile(export);
		setShouldOntologyFileExistst(false);
	}

	public static void run(String[] args, AppRunner runner) {
		App experiment = runner.configureExperiment(args);
		runner.runExperiment(experiment);
	}
}
