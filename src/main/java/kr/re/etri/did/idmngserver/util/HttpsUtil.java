package kr.re.etri.did.idmngserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsUtil  {
	private HttpsURLConnection m_conn = null;
	private HashMap m_hashMap = new HashMap();
	private String m_strURL = null;
	private int m_connTimeout = 10000;
	private int m_readTimeout = 15000;
	
	public void setURL(String strURL)
	{
		m_strURL = strURL;
	}
	
	public void setConnTimeout(int ms)
	{
		m_connTimeout = ms;
	}
	
	public void setReadTimeout(int ms)
	{
		m_readTimeout = ms;
	}
	public void init() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
			
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {				
			}
		}};
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		URL url = new URL(m_strURL);
		m_conn = (HttpsURLConnection)url.openConnection();
		m_conn.setDoInput(true);
		m_conn.setDoOutput(true);
		m_conn.setConnectTimeout(m_connTimeout);
		m_conn.setReadTimeout(m_readTimeout);
		m_conn.setRequestMethod("POST");
		m_conn.setRequestProperty("Content-Type", "application/json");
		m_conn.setRequestProperty("Accept-Charset", "UTF-8");
		m_conn.setHostnameVerifier(new HostnameVerifier() {			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}
	
	public void addHeader(String key, String value)
	{
		m_hashMap.put(key, value);
	}
	
	public void clearHeaders()
	{
		m_hashMap.clear();
	}
	
	
	public void send(byte[] data) throws IOException 
	{
		if(m_hashMap.size() > 0)
		{
			Set keySet = m_hashMap.keySet();
			Iterator it = keySet.iterator();
			while(it.hasNext())
			{
				String key = (String)it.next();
				String value = (String)m_hashMap.get(key);
				m_conn.setRequestProperty(key, value);
			}
		}
		OutputStream os = null;
		try {
			os = m_conn.getOutputStream();
			if(data != null && data.length > 0)
			{
				os.write(data);
				os.flush();
			}
		} finally {
			if(os != null)
			{
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public byte[] receive() throws IOException
	{
		InputStream is = null;
		byte[] baRes = null;
		try {
			is = m_conn.getInputStream();
			baRes = readAll(is);
		} finally {
			if(is != null)
			{
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return baRes;
	}
	
	public String getHeader(String key)
	{
		String value = m_conn.getHeaderField(key);
		return value;
	}
	
	public void close()
	{
		if(m_conn != null)
		{
			m_conn.disconnect();
			m_conn = null;
		}
	}
	
	public byte[] readAll(InputStream is) throws IOException
	 {
		 byte[] data = null;
		
		 int nRead = 0;
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 byte[] temp = new byte[2048];
		while( (nRead = is.read(temp)) > 0 )
		{
			baos.write(temp, 0, nRead);
		}
		data = baos.toByteArray();
		return data;
	 }
}
