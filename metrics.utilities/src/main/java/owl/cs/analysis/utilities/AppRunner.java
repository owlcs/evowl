package owl.cs.analysis.utilities;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class AppRunner {
	File ontologyfile;
	File outfile;
	long processtimeout = 21600000;
	boolean run = true;
	boolean ontologyFileShouldExist = false;
	boolean shouldRun = true;
	String group = "default";

	/**
	 * @param args
	 */

	public App configureExperiment(String[] args) {
		App experiment = prepare(args);
		verifyOntologyFile();
		verifyOutFile();
		return experiment;
	}

	private void verifyOntologyFile() {
		if (ontologyfile == null) {
			throw new IllegalArgumentException("The ontology file is not set!");
		} else if (ontologyFileShouldExist&&!ontologyfile.exists()) {
			throw new IllegalArgumentException(ontologyfile + " is not a valid file!");
		}
	}

	private void verifyOutFile() {
		if (outfile == null) {
			throw new IllegalArgumentException("The CSV file is not set!");
		} else if (!outfile.getParentFile().isDirectory()) {
			throw new IllegalArgumentException("The CSV file is not put in a valid directory!");
		}
	}

	protected abstract App prepare(String[] args);

	/**
	 * @param args
	 */
	public void runExperiment(App experiment) {
		if (shouldRun) {
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<String> future = executor.submit(experiment);

			try {
				Thread thread = new TerribleTimeoutThread((long) (getProcessTimeout() * 1.2));
				thread.start();
				System.out.println(future.get(getProcessTimeout(), TimeUnit.MILLISECONDS));
				thread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
				executor.shutdownNow();
				System.exit(9);
				// throw new RuntimeException("Uncomment System Exit.");
			}

			executor.shutdownNow();
			// System.err.println("Uncomment System Exit.");
			System.exit(0);
		} else {
			System.out.println("Warning: Settings precluding running of process.");
		}
	}

	protected long getProcessTimeout() {
		return processtimeout;
	}

	public void setProcessTimeout(long timeout) {
		processtimeout = timeout;
	}

	public File getOntologyFile() {
		return ontologyfile;
	}

	public File getCSVFile() {
		return outfile;
	}
	
	public String getGroup() {
		return group;
	}

	protected void setCSVFile(File csv) {
		outfile = csv;
	}

	protected void setOntologyFile(File ontology) {
		ontologyfile = ontology;
	}
	
	protected void setGroup(String group) {
		this.group = group;
	}

	protected void setShouldOntologyFileExistst(boolean ontologyFileShouldExist) {
		this.ontologyFileShouldExist = ontologyFileShouldExist;
	}

	protected void setShouldRun(boolean shouldRun) {
		this.shouldRun = shouldRun;
	}
}
