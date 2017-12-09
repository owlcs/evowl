package owl.cs.analysis.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

}
