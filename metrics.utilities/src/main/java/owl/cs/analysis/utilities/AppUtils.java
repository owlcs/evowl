package owl.cs.analysis.utilities;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtils {

	public static String getFilenameFromURL(String url) {
		String s = url.replaceAll("[^A-Za-z0-9]", "");
		s = s.endsWith(".owl") ? s : s+".owl";
		return s;
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return (dateFormat.format(date));
	}

	public static String getJARName(Class MyClass) {
		return new File(MyClass.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
	}

	public static long getSum(List<Long> list) {
		Long sum = 0l;
		for (Long i : list) {
			sum = sum + i;
		}
		return sum;
	}

	public static Map<String, String> getDefaultFailureData(Throwable e) {
		Map<String, String> csvdata = new HashMap<String, String>();
		String stacktrace = e.getMessage();
		if (stacktrace == null) {
			stacktrace = "no message";
		}
		stacktrace = stacktrace.replaceAll("[^a-zA-Z0-9 \\-\\.\\:\\/\\(]", "");
		if (stacktrace.length() > 801) {
			stacktrace = stacktrace.substring(0, 800) + " [...RESTCUTOFF]";
		}
		csvdata.put(MetricLabels.JAVA_EXCEPTION_MESSAGE, stacktrace);
		csvdata.put(MetricLabels.JAVA_EXCEPTION, "" + e.getClass());
		if (e.getCause() != null) {
			csvdata.put(MetricLabels.JAVA_EXCEPTION_CAUSE, "" + e.getCause().getClass());
		}
		return csvdata;
	}

}
