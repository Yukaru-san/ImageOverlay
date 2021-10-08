import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import org.jsoup.*;

class GoogleImagesScraper {
	// Data
	String searchUrl;
	String searchHtml;

	// Max number of images can check (only so many loaded in page)
	int MaxNumImages = 25;

	// Scraper
	private String urlRegex = "\\[\"https://[^encrypted].+?(?=\")";

	// Set the search results for the google images search
	public void setSearch(String inSearchKeywords, int inNumTries) {
		try {
			// Get page url and html
			searchUrl = "https://www.google.com/search?tbm=isch&tbs=isz:lt,islt:vga,iar:s,ift:jpg&q="
					+ convertToSearchUrl(inSearchKeywords);
			searchHtml = Jsoup.connect(searchUrl).get().html();

		} catch (IOException e) {
			// IF num of tries is > 0, try again, otherwise recurse back up
			if (inNumTries > 0) {
				setSearch(inSearchKeywords, inNumTries - 1);
			}
		}
	}

	// Get the n'th image's url
	public String getImageUrl(int inImageNum) {
		Pattern p = Pattern.compile(urlRegex);
		Matcher m = p.matcher(searchHtml);

		String url = "";
		for (int i = 0; i < inImageNum; i++) {
			m.find();
			url = m.group().substring(2);
		}

		return url;
	}

	// Get the page's html
	public String getHtml() {
		return searchHtml;
	}

	// Change certain chars in given string to make suitable for URL search
	private String convertToSearchUrl(String inSearch) {
		String outSearch = inSearch;

		outSearch = outSearch.replace("&", "%26");
		outSearch = outSearch.replace(",", "%2C");
		outSearch = outSearch.replace("\\+", "%2B");
		outSearch = outSearch.replace(' ', '+');

		return outSearch;
	}

	// Change unsupported chars in image url
	private String fixUnsupportedChars(String inString) {
		String outString = inString;

		outString = outString.replace("\\u003d", "=");
		outString = outString.replace("\\u0026", "&");

		return outString;
	}
}