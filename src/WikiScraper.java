import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/*
* AUTHOR: Nate Brill
* FILE: WikiScraper.java
* PURPOSE: To retrieve a wiki webpage and get the wiki links on that page.
*/
public class WikiScraper {
	
	public static Set<String> findWikiLinks(String link) {
		/*
         * PURPOSE: Converts a wiki page to a string and then creates a set of
         * wiki links that were found on that page.
         * 
         * @param link, the page that has the links.
         * 
         * @return links, a set of wiki links on the wiki page.
         */
		String html = fetchHTML(link);
		Set<String> links = scrapeHTML(html);
		return links;
	}
	
	private static String fetchHTML(String link) {
		/*
         * PURPOSE: To fetch a wiki page using a link from the internet and
         * convert the page to a string.
         * 
         * @param link, the page that has the links.
         * 
         * @return String, the HTML page as a string.
         */
		StringBuffer buffer = null;
		try {
			// Creates the URL so it can be looked up.
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			// Adds each line of the HTML page to buffer.
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) { // The page does not exist or was invalid.
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	private static String getURL(String link) {
		/*
         * PURPOSE: Converts a string to an actual URL that can be accessed
         * from the Internet.
         * 
         * @param link, the page that has the links.
         * 
         * @return String, the HTML URL for the wiki page.
         */
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	private static Set<String> scrapeHTML(String html) {
		/*
         * PURPOSE: To find all valid links using the HTML string that was
         * acquired off the webpage.
         * 
         * @param html, the HTML page as a string.
         * 
         * @return links, a set of valid wiki links.
         */
		Set<String> links = new HashSet<String>();
		String line = html;
		// Iterates through all links in the webpage.
		while (line.contains("href=\"/wiki/")) {
			line = line.substring(line.indexOf("\"/wiki/") + 7);
			String link = line.substring(0, line.indexOf("\""));
			if (!link.contains(":") && !link.contains("#")) {
					links.add(link);
			}
			line = line.substring(line.indexOf("\""));
		}
		return links;
	}
	
}
