
public class CrawlVisitStat {
	private String url;
	private String downloadSize;
	private String outLinks;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDownloadSize() {
		return downloadSize;
	}
	public void setDownloadSize(String downloadSize) {
		this.downloadSize = downloadSize;
	}
	public String getOutLinks() {
		return outLinks;
	}
	public void setOutLinks(String outLinks) {
		this.outLinks = outLinks;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	private String contentType;

}
