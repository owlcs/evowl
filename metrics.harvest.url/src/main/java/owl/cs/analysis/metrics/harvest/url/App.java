package owl.cs.analysis.metrics.harvest.url;

import java.io.File;

import owl.cs.analysis.metrics.utilities.oa5.ExperimentUtilities;
import owl.cs.analysis.metrics.utilities.oa5.ExportRDF;
import owl.cs.analysis.utilities.URLAnalysis;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		String url = args[0];
		File download = new File(args[1]);
		File export = new File(args[2], "metrics.harvest.url" + download.getName() + ".rdf");

		boolean overwrite = args.length > 3 ? args[3].equals("o") : false;
		boolean run = export.exists() && download.exists() ? overwrite : true;
		if (run) {
			System.out.println("Running: "+ExperimentUtilities.getJARName(App.class)+": "+url);
			URLAnalysis oa = new URLAnalysis(url, download) {

				public boolean exportRDFXML(File f) {
					return ExportRDF.exportMeasurements(getSimpleRecord(), getURL(), f,
							ExperimentUtilities.getJARName(this.getClass()));
				}

			};
			oa.exportRDFXML(export);
		}

	}
}
