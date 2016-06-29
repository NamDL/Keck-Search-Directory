import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;







//import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
//404 failed
//500 aborted

public class KeckCrawler extends WebCrawler {	
	int totalfetch;
	int fetchSuccess; 
	int TotalUrl;
	int fetchFailed;
	int fetchAborted;
	
	List<CrawlStat> crawStatList= new ArrayList<CrawlStat>();
	List<CrawlVisitStat> crawVisitStatList= new ArrayList<CrawlVisitStat>();
	List<CrawlURLStat> crawlURLStatList= new ArrayList<CrawlURLStat>();
	List<PageRank> rankList= new ArrayList<PageRank>();
	CrawlStatList myCrawlStatlist;
	Map<String,Integer> URLcount= new HashMap<String,Integer>();
	Map<String,Integer> statusNumbers= new HashMap<String,Integer>();
	Map<String,Integer> downloadSize= new HashMap<String,Integer>();
	Map<String,Integer> contentType= new HashMap<String,Integer>();
	
	public KeckCrawler(){
		myCrawlStatlist=new CrawlStatList();
		totalfetch=0;
		fetchSuccess=0; 
		TotalUrl=0;
		fetchFailed=0;
		fetchAborted=0;
		URLcount.put("urlWithin",0);
		URLcount.put("urlWithinUSC",0);
		URLcount.put("urlOther",0);
		downloadSize.put("< 1KB:", 0);
		downloadSize.put("1KB ~ <10KB:", 0);
		downloadSize.put("10KB ~ <100KB:", 0);
		downloadSize.put("100KB ~ <1MB:", 0);
		downloadSize.put(">= 1MB:", 0);
	}
	
	protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
		logger.warn("Skipping a URL: {} which was bigger ( {} ) than max allowed size", urlStr, pageSize);
	}

	@Override
	public void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		totalfetch++;
		String statusNum=statusCode+" "+statusDescription;
		if(statusNumbers.containsKey(statusNum)){
			statusNumbers.put(statusNum, (statusNumbers.get(statusNum))+1);
		}else if(!statusNumbers.containsKey(statusNum)){ 
			statusNumbers.put(statusNum,1);
		}
		if(statusCode>=200 && statusCode<300){
			fetchSuccess++;
		}else if(statusCode>=300 && statusCode<400){
			fetchFailed++;
		}else{
			fetchAborted++;
		}
		CrawlStat myCrawlStat=new CrawlStat();
		myCrawlStat.setUrl(webUrl.getURL());
		myCrawlStat.setStatus(statusCode+"");
		crawStatList.add(myCrawlStat);
			
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		CrawlURLStat myCrawlURLStat=new CrawlURLStat();
		myCrawlURLStat.setUrl(url.getURL());
		if(url.getURL().contains("keck.usc.edu")){
			URLcount.put("urlWithin", URLcount.get("urlWithin")+1);
			myCrawlURLStat.setUrlLoc("OK");
		}else if(url.getURL().contains("usc.edu")){
			URLcount.put("urlWithinUSC", URLcount.get("urlWithinUSC")+1);
			myCrawlURLStat.setUrlLoc("USC");
		}else{
			URLcount.put("urlOther", URLcount.get("urlOther")+1);
			myCrawlURLStat.setUrlLoc("outUSC");
		}
		crawlURLStatList.add(myCrawlURLStat);	
		String href = url.getURL().toLowerCase();
		return (href.startsWith("http://keck.usc.edu/")||href.startsWith("http://www.keck.usc.edu/"));
		//return href.startsWith("https://sowkweb.usc.edu/");
	}
	
	public boolean checkIfExists(CrawlStat myCrawlStat){
		for(CrawlStat crawl: crawStatList){
			if(crawl.getUrl().equals(myCrawlStat.getUrl())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Object getMyLocalData() {
		myCrawlStatlist.setCrawStatList(crawStatList);
		myCrawlStatlist.setCrawVisitStatList(crawVisitStatList);
		myCrawlStatlist.setCrawlURLStatList(crawlURLStatList);
		myCrawlStatlist.setFetchAborted(fetchAborted);
		myCrawlStatlist.setFetchFailed(fetchFailed);
		myCrawlStatlist.setFetchSuccess(fetchSuccess);
		myCrawlStatlist.setTotalfetch(totalfetch);
		myCrawlStatlist.setTotalUrl(TotalUrl);
		myCrawlStatlist.setUrlOther(URLcount.get("urlOther"));
		myCrawlStatlist.setUrlWithin(URLcount.get("urlWithin"));
		myCrawlStatlist.setUrlWithinUSC(URLcount.get("urlWithinUSC"));
		if(statusNumbers.isEmpty()){
			myCrawlStatlist.setStatusNumbers(null);
		}else{
			myCrawlStatlist.setStatusNumbers(statusNumbers);
		}		
		System.out.println(statusNumbers.get("200 OK"));
		myCrawlStatlist.setDownloadSize(downloadSize);
		myCrawlStatlist.setContentType(contentType);
		myCrawlStatlist.setRankList(rankList);
		return myCrawlStatlist;
	}

	@Override
	 public void visit(Page page) {		
		String contType=page.getContentType().split(";")[0];
		CrawlVisitStat myVisitCrawlStat=new CrawlVisitStat();
		myVisitCrawlStat.setUrl(page.getWebURL().getURL());
		myVisitCrawlStat.setContentType(contType);		
		myVisitCrawlStat.setDownloadSize(page.getContentData().length+"");
		//String hashedName = UUID.randomUUID().toString()+"."+contType.split("/")[1];
		String[] urlsSplit=page.getWebURL().getURL().split("http://",2);
		String hashedName=urlsSplit[1];
		System.out.println(hashedName);
		hashedName=hashedName.replaceAll("/","%22B22%");
		hashedName=hashedName.replaceAll(":","%22col22%");
		hashedName=hashedName.replaceAll("\\*","%22star22%");
		hashedName=hashedName.replaceAll("\\?","%22Q22%");
		hashedName=hashedName.replaceAll("\"","%22Qoute22%");
		hashedName=hashedName.replaceAll("<","%22le22%");
		hashedName=hashedName.replaceAll(">","%22gt22%");
		//hashedName=hashedName.replaceAll("|","%22Da22%");
		//String hashedName=page.getWebURL().getURL();
		int dataSize=(page.getContentData().length)/1024;
		if(dataSize<1){
			downloadSize.put("< 1KB:", downloadSize.get("< 1KB:")+1);
		}else if(dataSize>=1 && dataSize<10){
			downloadSize.put("1KB ~ <10KB:", downloadSize.get("1KB ~ <10KB:")+1);
		}else if(dataSize>=10 && dataSize<100){
			downloadSize.put("10KB ~ <100KB:", downloadSize.get("10KB ~ <100KB:")+1);
		}else if(dataSize>=100 && dataSize<1024){
			downloadSize.put("100KB ~ <1MB:", downloadSize.get("100KB ~ <1MB:")+1);
		}else if(dataSize>=1024){
			downloadSize.put(">= 1MB:", downloadSize.get(">= 1MB:")+1);
		}			
		if(contentType.containsKey(contType)){
			contentType.put(contType, contentType.get(contType)+1);
		}else{
			contentType.put(contType, 1);
		}
		if(page.getParseData() instanceof HtmlParseData){
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			myVisitCrawlStat.setOutLinks(links.size()+"");
			TotalUrl+=links.size();	
			PageRank rank= new PageRank();
			rank.setLinks(links);
			rank.setUrl(hashedName);
			rankList.add(rank);
		}else{
			myVisitCrawlStat.setOutLinks(0+"");
		}
		crawVisitStatList.add(myVisitCrawlStat);
		if(page.getContentType().contains("pdf")||page.getContentType().contains("html")||page.getContentType().contains("htm")||page.getContentType().contains("doc")||page.getContentType().contains("docx")){
				System.out.println(contType);
				hashedName=hashedName+"."+contType.split("/")[1];
				String filename = this.myController.getConfig().getCrawlStorageFolder() + "\\" + hashedName;
			    try {
			    	Files.write(Paths.get(filename), page.getContentData());			    	
			    } catch (IOException iox) {
			    	iox.printStackTrace();
			    }
		}		
	}
}
