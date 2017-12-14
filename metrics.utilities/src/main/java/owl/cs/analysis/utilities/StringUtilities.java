package owl.cs.analysis.utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class StringUtilities {

	public static String formatText(String subject, Map<String, String> rec) {
		StringBuilder sb = new StringBuilder();
		sb.append(subject + ": \n");
		for (String key : rec.keySet()) {
			sb.append(key + ": " + rec.get(key) + " \n");
		}
		return sb.toString();
	}
	
	public static String stripNonAlphaNumeric(String s) {
		return s.replaceAll("[^A-Za-z0-9]", "");
	}

	public static String formatHTML(String subject, Map<String, String> rec) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head><title>" + subject + "</title></head>");
		sb.append("<body>");
		sb.append("<h1>" + subject + "</h1>");
		sb.append("<table>");
		for (String key : rec.keySet()) {
			sb.append("<tr><td><b>" + key + "</b></td><td>" + rec.get(key) + "</td></tr>");
		}
		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

	public static String formatSimpleXHTMLMessage(String message) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!DOCTYPE html "
				+ "  PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
				+ "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">" + " <head>"
				+ "  <title>Response Message</title>" + " </head>" + " <body>" + "  <p>" + message + "</p>" + " </body>"
				+ "</html>";
	}

	/* https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static String createSpaceSeperatedStringFromMap(Map<String, Integer> map) {
		StringBuilder builder = new StringBuilder();
		Iterator<Entry<String, Integer>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();

			builder.append(pairs.getKey());
			builder.append(":");
			builder.append(pairs.getValue());
			builder.append(" ");
		}
		return builder.toString();
	}
	
	public static String createSpaceSeperatedStringFromOWLClassSet(Set<Object> set) {
		StringBuilder builder = new StringBuilder();
		for (Object s : set) {
			builder.append(s.toString());
			builder.append(" ");
		}
		return builder.toString();
	}

	public static Map<String, String> createPrefixedSet(Set<? extends Object> set, String prefix) {
		Map<String, String> m = new HashMap<>();
		for (Object s :set) {
			m.put(prefix + "_" + s.toString().toLowerCase(), true + "");
		}
		return m;
	}
	
	public static Map<String, String> createPrefixedMap(Map<String, Integer> map, String prefix) {
		Map<String, String> m = new HashMap<>();
		Iterator<Entry<String, Integer>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();
			m.put(prefix + "_" + pairs.getKey().toLowerCase(), pairs.getValue() + "");
		}
		return m;
	}
	
	public static <T> String createSpaceSeperatedStringFromSet(Set<T> set) {
		StringBuilder builder = new StringBuilder();
		for (T s : set) {
			builder.append(s.toString());
			builder.append(" ");
		}
		return builder.toString();
	}

}
