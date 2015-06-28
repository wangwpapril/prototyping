package com.swishlabs.prototyping.http;

import android.content.Context;
import android.util.Log;


import com.swishlabs.prototyping.BuildConfig;
import com.swishlabs.prototyping.pal.IHttp;
import com.swishlabs.prototyping.pal.internal.PalHttp;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;

public class HttpEngine implements Runnable {

	public static final String TAG = "HttpEngine";

	public static final int NET_CODE = 6000;

	public static final int ERR_NETWORK_SECURITY = NET_CODE + 1;

	public static final int ERR_NETWORK_DONT_CONNECT = NET_CODE + 2;

	public static final int ERR_NETWORK_OTHER = NET_CODE + 3;

	public static final int ERR_NETWORK_CANCEL = NET_CODE + 4;

	public static final int ERR_NETWORK_TIMEOUT = NET_CODE + 5;

	private static final int MAX_RETRY_COUNT = 1;
	
	private static final int NET_BUFFER = 1 * 1024;
	
	private static final int NET_CONNECT_TIMEOUT = 5 * 1000;

	private static final int NET_READ_TIMEOUT = 25 * 1000;
	
	LinkedBlockingQueue<HttpRequest> mReqeustQueue;
	
	Thread mThread;
	boolean mStop = false;
	static HttpEngine mEngine;
	static int mReqeustNum = 0;

	Context mContext;

	HttpClient mHttpClient;

	public HttpEngine(Context context) {
		mReqeustQueue = new LinkedBlockingQueue<HttpRequest>();
		mContext = context;
	}

	/**
	 * Do a http-request as a asynchronized way.
	 * @param request
	 */
	public void addRequest(HttpRequest request) {	
		try {
			mReqeustQueue.put(request);
			if (mThread == null) {
				mThread = new Thread(this);
				mThread.start();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelPendingTask() {
		mReqeustQueue.clear();
	}
		
	/**
	 * Do a http-reqeust ,this can block calling thread. 
	 * @param request
	 */
	public void doRequest(HttpRequest request) {
		try {
			doHttpReqeust(request);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			notifyError(request, ERR_NETWORK_TIMEOUT, null, null);
		} catch (IOException e) {
			e.printStackTrace();
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "Http Engine Catch IOException : " + e.toString());
			}
			notifyError(request, ERR_NETWORK_DONT_CONNECT, null, null);
		} catch (SecurityException e) {
			e.printStackTrace();
			notifyError(request, ERR_NETWORK_SECURITY, null, null);

		} catch (Exception e) {
			e.printStackTrace();
			notifyError(request, ERR_NETWORK_OTHER, null, null);
		}
	}

	public void shutdown() {
		if (mThread != null) {
			mStop = true;

			if (mHttpClient != null) {
				mHttpClient.getConnectionManager().shutdown();
				mHttpClient = null;
			}
			mThread.interrupt();
			mThread = null;
		}
	}
		

	@Override
	public void run() {
		if (BuildConfig.DEBUG){
			Log.d(TAG, "Http Engine start");
		}		 

		while (!mStop) {
			HttpRequest request = null;
			try {
				request = mReqeustQueue.take();
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}

			if (request != null) {
				int times = 0;

				while (!mStop && times < MAX_RETRY_COUNT && !request.isCancel()) {

					try {
						int code = doHttpReqeust(request);
						if (code != -1) {
							break;
						}
					} catch (ConnectTimeoutException e) {
						e.printStackTrace();											
						notifyError(request, ERR_NETWORK_TIMEOUT, null, null);
						break;
					} catch (IOException e) {
						e.printStackTrace();
						if (BuildConfig.DEBUG){
							Log.d(TAG, "Http Engine Catch IOException : " + e.toString());
						}
						notifyError(request, ERR_NETWORK_DONT_CONNECT, null, null);
						break;
					} catch (SecurityException e) {
						e.printStackTrace();						
						notifyError(request, ERR_NETWORK_SECURITY, null, null);
						break;

					} catch (Exception e) {
						e.printStackTrace();						
						notifyError(request, ERR_NETWORK_OTHER, null, null);
						break;
					}
					times++;
				}

				if (isCancel(request)) {
					notifyError(request, ERR_NETWORK_CANCEL, null, null);
				} else if (times >= MAX_RETRY_COUNT) {
					notifyError(request, ERR_NETWORK_DONT_CONNECT, null, null);
				}
			}
		}
		if (BuildConfig.DEBUG){
			Log.d(TAG, "Http Engine End ");
		}
	
	}

	
	private static byte[] extractByGZip(byte[] data) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPInputStream zis = new GZIPInputStream(bis);
		byte[] buffer = new byte[NET_BUFFER];
		int index = -1;
		while ((index = zis.read(buffer)) != -1) {
			bos.write(buffer, 0, index);
		}
		return bos.toByteArray();
	}
	
	private int doHttpReqeust(HttpRequest request) throws IOException {
		IHttp runningHttp = null;
		try {
			runningHttp = trySend(request);
			int reponseCode = runningHttp.getResponseCode();
			if (BuildConfig.DEBUG){
				Log.d(TAG, "doHttpReqeust RSPCODE: " + reponseCode + ", isCancel: " + isCancel(request));
			}
			
			if (isCancel(request)) {
				return reponseCode;

			} else {
				String type = runningHttp.getHeaderField("Content-Type");
				if (BuildConfig.DEBUG){
					Log.d(TAG, "doHttpReqeust Content-Type: " + type);
				}								
				
				if (type != null && type.indexOf("vnd.wap.wml") >= 0) {									
					runningHttp.close();
					runningHttp = null;
					return -1;
				} else {
					if (reponseCode == 200) {
						if (request.isStreamCallBack()) {
							InputStream in = null;
							try {
								in = runningHttp.openInputStream();
								notifyReceived(request, in, runningHttp.getContentLength(), runningHttp);
							} finally {
								if (in != null) {
									in.close();
									in = null;
								}
							}
						} else {
							String encodeType = runningHttp.getHeaderField("Content-Encoding");
							
							byte[] data = readData(runningHttp, request);
							if ("gzip".equals(encodeType)){
								Log.d(TAG, "This content is extract from gzip formater");
								data = extractByGZip(data);
							}
							notifyReceived(request, data, runningHttp);
						}
					} else {						
						byte[] data = readData(runningHttp, request);
						notifyError(request, reponseCode, data, runningHttp);
					}

				}
			}

			return reponseCode;
		} finally {
			if (runningHttp != null) {
				runningHttp.close();
			}
		}
	}

	private IHttp trySend(HttpRequest request) throws IOException {

		if (mHttpClient == null) {
			mHttpClient = createHttpClient();
		}

		String url = request.getUrl();
		if (!url.startsWith("https://") && !url.startsWith("http://")) {
			url = "http://" + url;
		}
		boolean isWap = PalHttp.isWap(mContext);
		IHttp http = PalHttp.createHttp(url, request.getMethod(), isWap, mHttpClient);

		http.setPostProgressListener(request.getPostProgressListener());
		
		Map<String, String> header = request.getHeaderField();
		if (header != null && header.size() > 0) {
			Set<Entry<String,String>> sets = header.entrySet();
			for (Map.Entry<String,String> item: sets) {
				http.setRequestProperty(item.getKey(), item.getValue());
			}
		}

		if (request.getPostData() != null) {
			http.setPostData(request.getPostData());
		} else {
			if (request.getPostRawtData() != null) {
				http.setPostRawData(request.getPostRawtData());
			}
		}
		if (request.getMultiPartFile() != null) {
			http.setMultiPart(request.getMultiPartFile());
		}

		if (request.getMultiPartParams() != null) {
			Set<Entry<String, String>> sets = request.getMultiPartParams().entrySet();
			for (Entry<String, String> item : sets) {
				http.addMutliParam(item.getKey(), item.getValue());
			}
		}
/*		if(LogUtil.isLog){
			LogUtil.d("PAL_HTTP", "Request Method : " + request.getMethod());
			if(LogUtil.isLog){
				for (Map.Entry<String, String> entry : header.entrySet()) {
					LogUtil.d("PAL_HTTP", "Header || " + entry.getKey() + " : " + entry.getValue());
				}
			}
			
			LogUtil.d("PAL_HTTP", "body : " + request.getPostData());
		}*/
		http.execute();

		return http;
	}

	private HttpClient createHttpClient() {

		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		ConnManagerParams.setTimeout(params, 1000);

		HttpConnectionParams.setConnectionTimeout(params, NET_CONNECT_TIMEOUT);

		HttpConnectionParams.setSoTimeout(params, NET_READ_TIMEOUT);
		
		HttpConnectionParams.setSocketBufferSize(params, 100 * 1024);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams.setContentCharset(params, "UTF8");
		SchemeRegistry schReg = new SchemeRegistry();

		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		schReg.register(new Scheme("https", new TxSSLSocketFactory(), 443));

		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		HttpClient client = new DefaultHttpClient(conMgr, params);
		client.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		return client;
	}


	HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

	private byte[] readData(IHttp http, HttpRequest request) throws IOException {
		InputStream inStream = null;
		try {
			inStream = http.openInputStream();

			ByteArrayOutputStream buff = new ByteArrayOutputStream();

			if (http.getHeaderField("Content-Length") != null) {

				int len = (int) http.getContentLength();
				readData(request, inStream, buff, len);
				return buff.toByteArray();

			} else {
				int numread = 0;
				long timeout = System.currentTimeMillis();

				byte[] tmp = new byte[NET_BUFFER];

				while (!isCancel(request)) {
					numread = inStream.read(tmp, 0, tmp.length);
					long t = System.currentTimeMillis();
					if (numread < 0) {
						break;
					} else if (numread == 0) {
						if (t - timeout > 5000) {
							System.out.println("time out  > 5000");
							break;
						}
						sleep(50);
					} else {
						buff.write(tmp, 0, numread);
						timeout = t;
					}
				}
				return buff.toByteArray();
			}

		} finally {
			if (inStream != null) {
				inStream.close();
				inStream = null;
			}
		}
	}

	private void readData(HttpRequest request, InputStream in, OutputStream out, int preferLength) throws IOException {
		int numread = 0;
		int count = 0;
		byte[] data = new byte[1024];
		while (!isCancel(request) && count < preferLength) {
			numread = in.read(data, 0, Math.min(data.length, preferLength - count));

			if (numread == -1) {
				throw new IOException();
			} else {
				out.write(data, 0, numread);
				count += numread;
			}
			sleep(10);
		}
	}

	private boolean isCancel(HttpRequest request) {
		return mStop || request.isCancel();
	}

	private void notifyReceived(HttpRequest request, byte[] data, IHttp http) {
		if (request.getHttpCallBack() != null) {
			request.getHttpCallBack().onReceived(request.getRequestID(), data, http);
		}
	}

	private void notifyReceived(HttpRequest request, InputStream stream, long contentLength, IHttp http) {
		if (request.getHttpCallBack() != null) {
			request.getHttpCallBack().onReceived(request.getRequestID(), stream, contentLength, http);
		}
	}

	private void notifyError(HttpRequest request, int errCode, byte[] msg, IHttp http) {
		if (request.getHttpCallBack() != null) {
			request.getHttpCallBack().onError(request.getRequestID(), errCode, msg, http);
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

	public static synchronized int getNextRequestID() {
		if (mReqeustNum >= Integer.MAX_VALUE) {
			mReqeustNum = 0;
			return mReqeustNum;
		} else {
			return mReqeustNum++;
		}

	}

}
