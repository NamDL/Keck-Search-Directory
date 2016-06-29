import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CrawlStatList {
	
	List<CrawlStat> crawStatList= new ArrayList<CrawlStat>();
	List<CrawlVisitStat> crawVisitStatList= new ArrayList<CrawlVisitStat>();
	List<CrawlURLStat> crawlURLStatList= new ArrayList<CrawlURLStat>();
	List<PageRank> rankList= new ArrayList<PageRank>();
	Map<String,Integer> statusNumbers= new HashMap<String,Integer>();
	Map<String,Integer> downloadSize= new HashMap<String,Integer>();
	Map<String,Integer> contentType= new HashMap<String,Integer>();
	int Totalfetch=0;
	int fetchSuccess=0; 
	int fetchFailed=0;
	int fetchAborted=0;
	int TotalUrl=0;
	int urlWithin=0;
	int urlWithinUSC=0;
	int urlOther=0;

	public List<PageRank> getRankList() {
		return rankList;
	}

	public void setRankList(List<PageRank> rankList) {
		this.rankList = rankList;
	}

	public Map<String, Integer> getContentType() {
		return contentType;
	}

	public void setContentType(Map<String, Integer> contentType) {
		this.contentType = contentType;
	}

	public Map<String, Integer> getDownloadSize() {
		return downloadSize;
	}

	public void setDownloadSize(Map<String, Integer> downloadSize) {
		this.downloadSize = downloadSize;
	}

	public Map<String, Integer> getStatusNumbers() {
		return statusNumbers;
	}

	public void setStatusNumbers(Map<String, Integer> statusNumbers) {
		this.statusNumbers = statusNumbers;
	}
	
	public int getTotalUrl() {
		return TotalUrl;
	}

	public void setTotalUrl(int totalUrl) {
		TotalUrl = totalUrl;
	}

	public int getUrlWithin() {
		return urlWithin;
	}

	public void setUrlWithin(int urlWithin) {
		this.urlWithin = urlWithin;
	}

	public int getUrlWithinUSC() {
		return urlWithinUSC;
	}

	public void setUrlWithinUSC(int urlWithinUSC) {
		this.urlWithinUSC = urlWithinUSC;
	}

	public int getUrlOther() {
		return urlOther;
	}

	public void setUrlOther(int urlOther) {
		this.urlOther = urlOther;
	}

	public List<CrawlURLStat> getCrawlURLStatList() {
		return crawlURLStatList;
	}

	public int getTotalfetch() {
		return Totalfetch;
	}

	public void setTotalfetch(int totalfetch) {
		Totalfetch = totalfetch;
	}

	public int getFetchSuccess() {
		return fetchSuccess;
	}

	public void setFetchSuccess(int fetchSuccess) {
		this.fetchSuccess = fetchSuccess;
	}

	public int getFetchFailed() {
		return fetchFailed;
	}

	public void setFetchFailed(int fetchFailed) {
		this.fetchFailed = fetchFailed;
	}

	public int getFetchAborted() {
		return fetchAborted;
	}

	public void setFetchAborted(int fetchAborted) {
		this.fetchAborted = fetchAborted;
	}

	public void setCrawlURLStatList(List<CrawlURLStat> crawlURLStatList) {
		this.crawlURLStatList = crawlURLStatList;
	}

		public List<CrawlStat> getCrawStatList() {
		return crawStatList;
	}

	public List<CrawlVisitStat> getCrawVisitStatList() {
		return crawVisitStatList;
	}

	public void setCrawVisitStatList(List<CrawlVisitStat> crawVisitStatList) {
		this.crawVisitStatList = crawVisitStatList;
	}

	public void setCrawStatList(List<CrawlStat> crawStatList) {
		this.crawStatList = crawStatList;
	}
	
	

}
