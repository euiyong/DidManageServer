package kr.re.etri.did.idmngserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class HttpUtil  {
	private HttpURLConnection m_conn = null;
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
	public void init() throws IOException
	{
		URL url = new URL(m_strURL);
		m_conn = (HttpURLConnection)url.openConnection();
		m_conn.setDoInput(true);
		m_conn.setDoOutput(true);
		m_conn.setConnectTimeout(m_connTimeout);
		m_conn.setReadTimeout(m_readTimeout);
		m_conn.setRequestMethod("POST");
		m_conn.setRequestProperty("Content-Type", "application/json");
		m_conn.setRequestProperty("Accept-Charset", "UTF-8");
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
				} catch (IOException e1) {}
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
				} catch (IOException e1) {}
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
