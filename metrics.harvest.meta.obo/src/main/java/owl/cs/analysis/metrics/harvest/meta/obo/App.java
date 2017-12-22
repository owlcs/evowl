package owl.cs.analysis.metrics.harvest.meta.obo;

import owl.cs.analysis.utilities.MetadataHarvestAppRunner;

/**
 * Hello world!
 *
 */
public class App extends MetadataHarvestAppRunner {
	
	public static void main(String[] args) {
		run(args, new App());
	}

	@Override
	protected owl.cs.analysis.utilities.App prepare(String[] args) {
		prepareBasic(args, this.getClass().getName());
		return new OBOHarvester(getCSVFile());
	}
}
