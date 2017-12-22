package owl.cs.analysis.utilities;

import java.net.MalformedURLException;
import java.net.URL;

public class WebUtils {

	public static boolean isValidURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String encodeURL(String s) {
		// TODO encode properly (URLEncoder.encode(q, "UTF-8"))
		return s.replace("#", "%23");
	}
	
}
