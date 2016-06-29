import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

/*
 * my.usc.edu -->ok
 * downloadsize
 * proper xontent name
 * check for 200- fetches c=succeded
 * contetn type
 * check PDF and other files
 * www.keck.udc.edu
 * Total URL
 * fetch attempterd, succeded, failed
 * */
public class Controller {

	public static void main(String[] args) {
		 String crawlStorageFolder = "D:\\DataCrawl";
		 int numberOfCrawlers = 7;
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 //change to 5
		 config.setMaxDepthOfCrawling(5);
		 //change to 5000
		 config.setMaxPagesToFetch(5000);
		 config.setMaxDownloadSize(4090838);
		 config.setPolitenessDelay(1000);
		 config.setIncludeBinaryContentInCrawling(true);
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller;
		 FileWriter write=null,write2=null,write3=null,crawl=null,write4=null;
		 try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.addSeed("http://www.keck.usc.edu/");			
			//controller.addSeed("https://sowkweb.usc.edu/");
			controller.start(KeckCrawler.class, numberOfCrawlers);			
			List<Object> crawlersLocalData = controller.getCrawlersLocalData();
			write=new FileWriter("D:\\DataCrawl\\fetch.csv",true);
			write2=new FileWriter("D:\\DataCrawl\\visit.csv",true);
			write3=new FileWriter("D:\\DataCrawl\\urls.csv",true);
			crawl=new FileWriter("D:\\DataCrawl\\CrawlReport.txt",true);
			write4=new FileWriter("D:\\DataCrawl\\pagerankdata.csv",true);
			int totalfetch=0;
			int fetchSuccess=0; 
			int fetchFailed=0;
			int fetchAborted=0;
			int TotalUrl=0;
			int urlWithin=0;
			int urlWithinUSC=0;
			int urlOther=0;
			
			Map<String,Integer> statusNumbers= new HashMap<String,Integer>();
			Map<String,Integer> downloadSize= new HashMap<String,Integer>();
			Map<String,Integer> contentType= new HashMap<String,Integer>();
			for (Object localData : crawlersLocalData) {				
				CrawlStatList crawStatList= (CrawlStatList) localData;
				List<CrawlStat> crawStatlist= crawStatList.getCrawStatList();
				List<CrawlVisitStat> crawVisitStatList= crawStatList.getCrawVisitStatList();
				List<CrawlURLStat> crawURLStatList= crawStatList.getCrawlURLStatList();
				List<PageRank> rankList= crawStatList.getRankList();
				Map<String,Integer> statusCollection= crawStatList.getStatusNumbers();
				Map<String,Integer> dataSize= crawStatList.getDownloadSize();
				Map<String,Integer> contType= crawStatList.getContentType();
				
				
				totalfetch+=crawStatList.getTotalfetch();
				fetchSuccess+=crawStatList.getFetchSuccess();
				fetchFailed+=crawStatList.getFetchFailed();
				fetchAborted+=crawStatList.getFetchAborted();
				TotalUrl+=crawStatList.getTotalUrl();
				urlWithin=urlWithin+crawStatList.getUrlWithin();
				urlWithinUSC+=crawStatList.getUrlWithinUSC();
				urlOther+=crawStatList.getUrlOther();
				if(statusCollection !=null){
					for(Map.Entry<String, Integer> entry: statusCollection.entrySet()){					
						if(statusNumbers.containsKey(entry.getKey())){
							statusNumbers.put(entry.getKey(), statusNumbers.get(entry.getKey())+entry.getValue());
						}else {//if(!statusNumbers.containsKey(entry.getKey())){ 
							statusNumbers.put(entry.getKey(),entry.getValue());
						}
					}
				}				
				for(Map.Entry<String, Integer> entry: dataSize.entrySet()){					
					if(downloadSize.containsKey(entry.getKey())){
						downloadSize.put(entry.getKey(), downloadSize.get(entry.getKey())+entry.getValue());
					}else { 
						downloadSize.put(entry.getKey(),entry.getValue());
					}
				}
				
				for(Map.Entry<String, Integer> entry: contType.entrySet()){					
					if(contentType.containsKey(entry.getKey())){
						contentType.put(entry.getKey(), contentType.get(entry.getKey())+entry.getValue());
					}else { 
						contentType.put(entry.getKey(),entry.getValue());
					}
				}
				
				for(CrawlStat stat:crawStatlist){
					String res=stat.getUrl()+","+stat.getStatus()+"\n";
					write.write(res);
				} 
				for(CrawlVisitStat stat:crawVisitStatList){
					String res=stat.getUrl()+","+stat.getDownloadSize()+","+stat.getOutLinks()+","+stat.getContentType()+"\n";
					write2.write(res);
				}
				for(CrawlURLStat stat:crawURLStatList){
					String res=stat.getUrl()+","+stat.getUrlLoc()+"\n";
					write3.write(res);
				}
				
				for(PageRank stat:rankList){
					String res="";
					String url=stat.getUrl()+".html";
					String hashedName="";
					res+=url;
					Set<WebURL> linksURL= stat.getLinks();
					for(WebURL urlLink:linksURL){
						if (!(urlLink.getURL().contains(".png")||urlLink.getURL().contains(".pdf")||urlLink.getURL().contains(".jpg")||urlLink.getURL().contains(".xml")||urlLink.getURL().contains(".css"))){
							System.out.println(urlLink.getURL());
							if(urlLink.getURL().contains("http://")){
								String[] urlsSplit=urlLink.getURL().split("http://",2);
								hashedName=urlsSplit[1];
							}else if(urlLink.getURL().contains("https://")){
								String[] urlsSplit=urlLink.getURL().split("https://",2);
								hashedName=urlsSplit[1];
							}			
							hashedName=hashedName.replaceAll("/","%22B22%");
							hashedName=hashedName.replaceAll(":","%22col22%");
							hashedName=hashedName.replaceAll("\\*","%22star22%");
							hashedName=hashedName.replaceAll("\\?","%22Q22%");
							hashedName=hashedName.replaceAll("\"","%22Qoute22%");
							hashedName=hashedName.replaceAll("<","%22le22%");
							hashedName=hashedName.replaceAll(">","%22gt22%");
							hashedName=hashedName+".html";
							res+=",";
							res+=hashedName;
						}						
					}
					res+="\n";
					write4.write(res);
				}
				
			}			
			String crawtxt="Name: Namratha Lakshminarayana \nUSC ID: 5833999356 \nSchool Crawled: Keck School";
			crawtxt+="\n \n";
			crawtxt+="Fetch Statistics\n";
			crawtxt+="================\n";			
			crawtxt+="# fetches attempted:"+totalfetch+"\n";
			crawtxt+="# fetches succeeded:"+fetchSuccess+"\n";
			crawtxt+="# fetches aborted:"+fetchAborted+"\n";
			crawtxt+="# fetches failed:"+fetchFailed+"\n";
			crawtxt+="\n \n";
			crawtxt+="Outgoing URLs:\n==============\n";
			crawtxt+="Total URLs extracted:"+TotalUrl+"\n";
			crawtxt+="# unique URLs extracted:"+(urlOther+urlWithin+urlWithinUSC)+"\n";
			crawtxt+="# unique URLs within School:"+urlWithin+"\n";
			crawtxt+="# unique USC URLs outside School:"+urlWithinUSC+"\n";
			crawtxt+="# unique URLs outside USC:"+urlOther+"\n";
			crawtxt+="\n \n";
			crawtxt+="File Sizes::\n=============\n";
			for(Map.Entry<String, Integer> entry: downloadSize.entrySet()){
				crawtxt+=entry.getKey()+""+entry.getValue()+"\n";
			}
			crawtxt+="\n \n";
			crawtxt+="Status Codes:\n=============\n";
			for(Map.Entry<String, Integer> entry: statusNumbers.entrySet()){
				crawtxt+=entry.getKey()+":"+entry.getValue()+"\n";
			}
			crawtxt+="\n \n";
			crawtxt+="Content Types:\n=============\n";
			for(Map.Entry<String, Integer> entry: contentType.entrySet()){
				crawtxt+=entry.getKey()+":"+entry.getValue()+"\n";
			}
			crawl.write(crawtxt);			
			System.out.println(crawtxt);
		 } catch (Exception e) {
			e.printStackTrace();
		 }finally{
			 if(write != null){
				 try {
					write.close();
					write2.close();
					write3.close();
					write4.close();
					crawl.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		 }
	}

}
