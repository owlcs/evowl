package owl.cs.analysis.utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class StringUtilities {

	public static String formatText(String subject, Map<String, String> rec) {
		StringBuilder sb = new StringBuilder();
		sb.append(subject + ": \n");
		for (String key : rec.keySet()) {
			sb.append(key + ": " + rec.get(key) + " \n");
		}
		return sb.toString();
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

}
