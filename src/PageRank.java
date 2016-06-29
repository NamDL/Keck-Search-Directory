import java.util.Set;

import edu.uci.ics.crawler4j.url.WebURL;


public class PageRank {
	
	String url;
	Set<WebURL> links;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Set<WebURL> getLinks() {
		return links;
	}
	
	public void setLinks(Set<WebURL> links) {
		this.links = links;
	}
	

}
