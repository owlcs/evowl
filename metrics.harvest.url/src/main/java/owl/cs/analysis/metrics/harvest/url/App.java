package owl.cs.analysis.metrics.harvest.url;

import java.io.File;

import owl.cs.analysis.metrics.utilities.oa5.ExportRDF;
import owl.cs.analysis.utilities.URLAnalysis;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String url = args[0];
		File download = new File(args[1]);
		File export = new File(args[2],"metrics.harvest.url"+download.getName()+".rdf");
		
		URLAnalysis oa = new URLAnalysis(url, download) {

			public boolean exportRDFXML(File f) {
				return ExportRDF.exportMeasurements(getSimpleRecord(), getURL(), f);
			}
			
		};
		oa.exportRDFXML(export);
    }
}
