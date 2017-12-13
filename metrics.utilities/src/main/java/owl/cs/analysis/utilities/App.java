package owl.cs.analysis.utilities;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class App implements Callable<String> {

	final private String url;
	final private File ontology;
	final private File outfile;
	final private Map<String, String> rec = new HashMap<String, String>();

	public App(File ontology, File outfile, String url) {
		this.url = url;
		this.ontology = ontology;
		this.outfile = outfile;
	}

	@Override
	public String call() throws Exception {
		try {
			this.analyse();
			recordMemoryUsage();
			System.gc();
			exportResults();
		} catch (Throwable e) {
			e.printStackTrace();
			writeFailureData(e);
			throw new RuntimeException(e);
		}
		return "finished";
	}

	protected void writeFailureData(Throwable e) {
		getSimpleRecord().putAll(AppUtils.getDefaultFailureData(e));
	}

	protected File getOutfile() {
		return outfile;
	}

	private void recordMemoryUsage() {
		try {
			List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
			List<Long> mem = new ArrayList<Long>();
			for (MemoryPoolMXBean pool : pools) {
				MemoryUsage peak = pool.getPeakUsage();
				mem.add(peak.getUsed());
			}
			addResult(MetricLabels.PEAK_MEMORY_USAGE, AppUtils.getSum(mem) + "");

		} catch (Throwable t) {
			System.err.println("Exception in agent: " + t);
		}
	}

	protected abstract void analyse();

	protected abstract void exportResults();

	protected void addResult(Object key, Object value) {
		rec.put(key.toString(), value.toString());
	}

	protected File getOntologyFile() {
		return this.ontology;
	}

	protected String getURL() {
		return this.url;
	}

	public Map<String, String> getSimpleRecord() {
		return rec;
	}

}
