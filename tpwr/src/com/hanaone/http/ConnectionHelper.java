package com.hanaone.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;

import com.hanaone.jni.JNIHanaone;
import com.hanaone.tpwr.Constants;
import com.hanaone.tpwr.db.FileDataSet;

public class ConnectionHelper {
	public static final int HOST_GOOGLE = 0;
	public static final int HOST_DROPBOX = 1;
	public static final int HOST_AMAZON = 2;
	
	private Context mContext;
	
	
	public ConnectionHelper(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public InputStream connect(String remoteFile, int host) throws IOException, SAXException, ParserConfigurationException{
		InputStream is = null;
		switch (host) {
		case HOST_GOOGLE:
			is = gConnect(remoteFile);
			break;
		case HOST_DROPBOX:
			
			break;
		case HOST_AMAZON:
			is = aConnect(remoteFile);
			break;
		default:
			break;
		}
		return is;
	}
	public long getSize(String remoteFile, int host) throws IOException{
		long size = 0;
		switch (host) {
		case HOST_GOOGLE:
			size = gGetSize(remoteFile);
			break;
		case HOST_DROPBOX:
			
			break;
		case HOST_AMAZON:
			size = aGetSize(remoteFile);
			break;
		default:
			break;
		}
		return size;		
	}
	
	private InputStream gConnect(String remoteFile) throws IOException{
		
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			connection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			httpConnection.setDoOutput(true);
//			httpConnection.setDoInput(true);
//			httpConnection.setUseCaches(false);
//			httpConnection.setRequestProperty("Content-Language", "en-US");
            
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			
			int reqCode = httpConnection.getResponseCode();
			
			
			if(reqCode == HttpURLConnection.HTTP_OK){
				InputStream is = httpConnection.getInputStream();
				Map<String, List<String>> map = httpConnection.getHeaderFields();
				List<String> values = map.get("content-type");
				if(values != null && !values.isEmpty()){
					String type = values.get(0);
					
					if(type.contains("text/html")){
						String cookie = httpConnection.getHeaderField("Set-Cookie");
						String temp = Constants.getInternalPath(mContext, Constants.PATH_TEMP) + "/temp.html";
						if(saveGHtmlFile(is, temp)){
							String href = getRealUrl(temp);
							if(href != null){
								return parseUrl(href, cookie);
							}
						}
						
						
					} else if(type.contains("application/json")){
						String temp = Constants.getInternalPath(mContext, Constants.PATH_TEMP) + "/temp.txt";
						if(saveGJsonFile(is, temp)){
							FileDataSet data = JsonReaderHelper.readFileDataset(new File(temp));
							if(data.getPathRemote() != null){
								return parseUrl(data.getPathRemote());
							}
						}
					}
				}
				return is;
			}
		}
		return null;
	}
	

	private long gGetSize(String remoteFile) throws IOException{
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			connection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			httpConnection.setDoOutput(true);			
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			
			int reqCode = httpConnection.getResponseCode();
			if(reqCode == HttpURLConnection.HTTP_OK){
				InputStream is = httpConnection.getInputStream();
				Map<String, List<String>> map = httpConnection.getHeaderFields();
				List<String> values = map.get("content-type");
				if(values != null && !values.isEmpty()){
					
					String type = values.get(0);
					if(type.contains("text/html")){
						String temp = Constants.getInternalPath(mContext, Constants.PATH_TEMP) + "/temp.html";
						if(saveGHtmlFile(is, temp)){
							String href = getRealUrl(temp);
							URL  urlTemp = new URL(href);
							URLConnection connectionTemp = urlTemp.openConnection();							
							map = connectionTemp.getHeaderFields();
							if(map != null){
								values = map.get("content-Length");
								if(values != null && !values.isEmpty()){
									String sLength = values.get(0);
									
									return Long.parseLong(sLength);
								}			
							}
						}						
						
					} else if(type.contains("application/json")){
						String temp = Constants.getInternalPath(mContext, Constants.PATH_TEMP) + "/temp.txt";
						if(saveGJsonFile(is, temp)){
							FileDataSet data = JsonReaderHelper.readFileDataset(new File(temp));
							return data.getSize();
						}
					}
					
				}
	
			}
		}
		return 0;		
	}
    private String GetCookieFromURL(String urlName) {
    	 
        String result = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlName);
            con = (HttpURLConnection) (url.openConnection());
            con.setConnectTimeout(40000);
            con.setReadTimeout(40000);
            con.setRequestMethod("GET");
            //con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
            con.setInstanceFollowRedirects(true);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Language", "en-US");
            result = con.getHeaderField("Set-Cookie");
       } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
            con = null;
        }
        return result;
    }	
	private InputStream aConnect(String remoteFile) throws IOException{
		String cookie = GetCookieFromURL("http://52.27.144.7:8080/webfilesys/servlet?command=login&userid=readuser&password=readuser&logonbutton=Logon");
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestProperty("Content-Language", "en-US");
			httpConnection.setRequestProperty("Cookie", cookie);	
			
			httpConnection.setRequestMethod("GET");
			
			httpConnection.connect();
			
			int resCode = httpConnection.getResponseCode();
			if(resCode == HttpURLConnection.HTTP_OK){				
				InputStream is = httpConnection.getInputStream();
				return is;			
			}
		}		
		return null;
	}	
	
	private InputStream parseUrl(String remoteFile) throws IOException{
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			
			httpConnection.setRequestMethod("GET");
			
			httpConnection.connect();
			
			int resCode = httpConnection.getResponseCode();
			if(resCode == HttpURLConnection.HTTP_OK){				
				InputStream is = httpConnection.getInputStream();
				return is;			
			}
		}		
		return null;
	}		
	private long aGetSize(String remoteFile) throws IOException{
		String cookie = GetCookieFromURL("http://52.27.144.7:8080/webfilesys/servlet?command=login&userid=readuser&password=readuser&logonbutton=Logon");
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestProperty("Content-Language", "en-US");
			httpConnection.setRequestProperty("Cookie", cookie);	
			
			httpConnection.setRequestMethod("GET");
			
			httpConnection.connect();
			
			int resCode = httpConnection.getResponseCode();
			if(resCode == HttpURLConnection.HTTP_OK){				
				Map<String, List<String>> map = httpConnection.getHeaderFields();
				List<String> values = map.get("Content-Length");
				if(values != null && !values.isEmpty()){
					String sLength = values.get(0);
					
					return Long.parseLong(sLength);
				}				
			}
		}	
		return 0;
	}
	private InputStream parseUrl(String remoteFile, String cookie) throws IOException{
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestProperty("Content-Language", "en-US");
			httpConnection.setRequestProperty("Cookie", cookie);	
			
			httpConnection.setRequestMethod("GET");
					
			
			httpConnection.connect();
			
			int resCode = httpConnection.getResponseCode();
			if(resCode == HttpURLConnection.HTTP_OK){				
				InputStream is = httpConnection.getInputStream();
				return is;			
			}
		}		
		return null;
	}	
	private boolean saveGHtmlFile(InputStream is, String localFile) throws IOException{
		if(is == null) return false;
		FileOutputStream os = new FileOutputStream(new File(localFile));		
		
		byte[] buf = new byte[1024];
		int read = 0;
		while((read = is.read(buf)) > 0){
			os.write(buf, 0, read);
		}
		os.close();
		is.close();
		return true;
	}
	private boolean saveGJsonFile(InputStream is, String localFile) throws IOException{
		if(is == null) return false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String txt = null;
		// skip first line
		txt = reader.readLine();
		
		FileOutputStream os = new FileOutputStream(new File(localFile));
		while((txt = reader.readLine()) != null){
			os.write(txt.getBytes());
		}
		os.close();
		reader.close();
		is.close();
		return true;
	}
	private String getRealUrl(String path){
//		try {
//			Document doc = Jsoup.parse(new File(path), "UTF-8", "https://drive.google.com");
//			if(doc != null){
//				Element element = doc.getElementById("uc-download-link");
//				String href = element.attr("href");
//				
//				
//				
//				return "https://drive.google.com" + href;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		return null;
	}
	
}
