package owl.cs.analysis.utilities;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public abstract class URLAnalysis implements ExportToRDFXML {

	final private String url;
	final private File download;
	final private Map<String, String> rec = new HashMap<String, String>();

	public URLAnalysis(String url, File download) {
		this.url = url;
		this.download = download;
		analyse();
	}

	protected void analyse() {
		linkHealth();
		download();
	}

	private void download() {
		URL url;
		try {
			url = new URL(getFinalURL(this.url));
			try {
				FileUtils.copyURLToFile(url, download, 5000, 5000);
				addResult(MetricLabels.URL_DOWNLOADABLE, true);
			} catch (IOException e) {
				addResult(MetricLabels.URL_DOWNLOADABLE, false);
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			addResult(MetricLabels.URL_DOWNLOADABLE, false);
			e.printStackTrace();
		} catch (IOException e) {
			addResult(MetricLabels.URL_DOWNLOADABLE, false);
			e.printStackTrace();
		}
	}

	protected String getURL() {
		return this.url;
	}

	protected void addResult(Object key, Object value) {
		rec.put(key.toString(), value.toString());
	}

	private boolean linkHealth() {
		try {
			System.out.println(getURL());
			URL url = new URL(getURL());
			addResult(MetricLabels.URL_VALID, true);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			addResult(MetricLabels.URL_HTTP_CODE, code);

			if (code == 200) {
				addResult(MetricLabels.URL_HEALTHY, true);
				return true;
			} else {
				addResult(MetricLabels.URL_HEALTHY, false);
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			addResult(MetricLabels.URL_VALID, false);
		} catch (ProtocolException e) {
			e.printStackTrace();
			addResult(MetricLabels.URL_PROTOCOL_EXCEPTION_MESSAGE, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			addResult(MetricLabels.URL_ACCESS_EXCEPTION_MESSAGE, e.getMessage());
		}
		return false;
	}

	public Map<String, String> getSimpleRecord() {
		return rec;
	}
	
	private static String getFinalURL(String url) throws IOException {
		URLConnection conu = new URL(url).openConnection();
		if (conu instanceof HttpURLConnection) {
			HttpURLConnection con = (HttpURLConnection) conu;
			con.setInstanceFollowRedirects(false);
			con.connect();
			con.getInputStream();

			if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
					|| con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				String redirectUrl = con.getHeaderField("Location");
				return getFinalURL(redirectUrl);
			}
		}
		return url;
	}
}
