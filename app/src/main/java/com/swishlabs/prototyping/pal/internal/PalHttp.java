package com.swishlabs.prototyping.pal.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.swishlabs.prototyping.BuildConfig;
import com.swishlabs.prototyping.entity.InputStreamKnownSizeBody;
import com.swishlabs.prototyping.entity.ProgressMutiPartEntity;
import com.swishlabs.prototyping.http.HttpRequest;
import com.swishlabs.prototyping.pal.IHttp;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class PalHttp implements IHttp {

	private final String TAG = "PAL_HTTP";
	
	static Random mBoundaryRandom = new Random();

	boolean isWap;

	String mUrl;

	String mMethod;

	Map<String, Object> mMultiPartFile;

	List<NameValuePair> mMultiParams= new LinkedList<NameValuePair>();

	List<NameValuePair> mRequestProperty = new LinkedList<NameValuePair>();

	HttpClient mHttpClient;

	HttpResponse mHttpResponse;

	String mPostData;
	
	byte[] mPostRawData;

	private OnPostProgressListener mPostProgressListener;
	
	private long mTotalSize;
	
	public static IHttp createHttp(String url, String method, boolean isWap, HttpClient httpClient) {
		if (httpClient == null) {
			return null;
		}
		
		PalHttp http = new PalHttp();
		http.mHttpClient = httpClient;
		http.isWap = isWap;
		http.open(url);
		http.setRequestMethod(method);
		return http;
	}


	private String encodeUrl(List<NameValuePair> nameVp) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (NameValuePair item : nameVp) {
			if (first)
				first = false;
			else {
				sb.append("&");
			}
			String param = item.getValue();
			if (!TextUtils.isEmpty(param)) {
				sb.append(URLEncoder.encode(item.getName(), HTTP.UTF_8) + "=" + URLEncoder.encode(param, HTTP.UTF_8));
			}
		}
		return sb.toString();
	}
	 		
	public static byte[] compressByGZip(byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream zipots = new GZIPOutputStream(bos);
		zipots.write(data);		
		zipots.flush();
		zipots.finish();
		zipots.close();
		return bos.toByteArray();
	}

	public int execute() throws IOException {
		HttpRequestBase request = null;
		if (mMethod.equals(HttpRequest.METHOD_POST)) {
			request = new HttpPost(mUrl);
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "do HttpReqeust post Url " + mUrl);
			}			
		} else if (mMethod.equals(HttpRequest.METHOD_PUT)) {
			request = new HttpPut(mUrl);
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "do HttpReqeust put Url " + mUrl);
			}			
		} else if (mMethod.equals(HttpRequest.METHOD_DELET)) {
			request = new HttpDelete(mUrl);
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "do HttpReqeust delete Url " + mUrl);
			}			
		} else {
			if (mMultiParams!=null && !mMultiParams.isEmpty()){
				mUrl = mUrl + "&" + encodeUrl(mMultiParams);	
			}
			request = new HttpGet(mUrl);
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "do HttpReqeust get Url " + mUrl);
			}			
		}
				
		HttpEntity entity = null;
		
		if (mMultiPartFile != null && !mMultiPartFile.isEmpty()) {
			String boundary = getBoundary();
			request.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);

			final ProgressMutiPartEntity partEntity = new ProgressMutiPartEntity(HttpMultipartMode.STRICT, boundary,
					Charset.forName("UTF-8"), new ProgressMutiPartEntity.ProgressListener() {

						@Override
						public void transferred(long num) {
							if (mPostProgressListener != null) {
								int progress = (int) (num / (double) mTotalSize * 100);
								mPostProgressListener.onProgress(progress);
							}

						}
					});
			entity = partEntity;

			if (mMultiParams != null) {
				for (NameValuePair valuePair : mMultiParams) {
					String key = valuePair.getName();
					String value = valuePair.getValue();
					partEntity.addPart(key, new StringBody(value, Charset.forName("UTF-8")));
				}
			}
			if (!TextUtils.isEmpty(mPostData)) {
				partEntity.addPart("jsondata", new StringBody(mPostData, Charset.forName("UTF-8")));
			}
			
			Set<Entry<String,Object>> sets= mMultiPartFile.entrySet();
			for(Entry<String, Object> item : sets){
				if (item.getValue() instanceof File) {
					FormBodyPart part = new FormBodyPart(item.getKey(), new FileBody((File) item.getValue()));
					partEntity.addPart(part);
				} else if (item.getValue() instanceof byte[]) {
					ByteArrayBody part = new ByteArrayBody((byte[]) item.getValue(), "image/jpeg", "fileName");
					partEntity.addPart(item.getKey(), part);
				} else if (item.getValue() instanceof InputStream) {
					FormBodyPart part = new FormBodyPart(item.getKey(), new InputStreamKnownSizeBody(
							(InputStream) item.getValue(), "fileName"));
					partEntity.addPart(part);
				}
			}
			mTotalSize = partEntity.getContentLength();

		} else if (mPostRawData != null) {
			mPostRawData = compressByGZip(mPostRawData);
			entity = new ByteArrayEntity(mPostRawData);
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Content-Encoding", "gzip");
		} else if (!TextUtils.isEmpty(mPostData)) {
			entity = new StringEntity(mPostData, HTTP.UTF_8);
			request.setHeader("Content-Type", "application/json");
		} else if (mMultiParams != null) {
			entity = new UrlEncodedFormEntity(mMultiParams, HTTP.UTF_8);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		}

				
		if (entity != null) {
			// we only output short message.
			if (BuildConfig.DEBUG && mMultiPartFile == null) {
				StringBuffer buffer = new StringBuffer();
				InputStream is = entity.getContent();
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8));
					String json = null;
					while ((json = br.readLine()) != null) {
						buffer.append(json);
						if (buffer.length() > 500) {
							break;
						}
					}
					Log.d(TAG, "HttpReqeust body: " + buffer.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}						
			
			if (request instanceof HttpPost) {
				((HttpPost) request).setEntity(entity);
			} else if (request instanceof HttpPut) {
				((HttpPut) request).setEntity(entity);
			}
		}
		
		if (mRequestProperty != null) {
			for (NameValuePair valuePaire : mRequestProperty) {
				String key = valuePaire.getName();
				String value = valuePaire.getValue();
				request.setHeader(key, value);		
			}
		}
		
		
		mHttpResponse = mHttpClient.execute(request);
		if (mPostProgressListener != null) {
			mPostProgressListener.onProgress(100);
		}
		if (mHttpResponse != null && mHttpResponse.getStatusLine() != null) {
			if (BuildConfig.DEBUG){
				Log.w(TAG, "execute ResponseCode:" + mHttpResponse.getStatusLine().getStatusCode());
				for(int i = 0 ; i < mHttpResponse.getAllHeaders().length; i ++){
					Log.w(TAG, "headers : " + mHttpResponse.getAllHeaders()[i]);
				}
			}
			return mHttpResponse.getStatusLine().getStatusCode();
		} else {
			if (BuildConfig.DEBUG){
				Log.d(TAG, "execute mHttpResponse = null");
			}
			
			return -1;
		}
	}

	private String getBoundary() {
		return String.valueOf(System.currentTimeMillis()) + String.valueOf(Math.abs(mBoundaryRandom.nextLong()));
	}

	public String getHeaderField(int n) throws IOException {
		if (mHttpResponse != null) {
			Header head[] = mHttpResponse.getAllHeaders();
			if (head != null && head.length >= n)
				return head[n].getValue();
		}

		return null;
	}

	public String getHeaderField(String name) throws IOException {

		if (mHttpResponse != null) {
			Header head = mHttpResponse.getFirstHeader(name);
			if (head != null)
				return head.getValue();
		}

		return null;
	}

	public String getHeaderFieldKey(int n) throws IOException {
		if (mHttpResponse != null) {
			Header head[] = mHttpResponse.getAllHeaders();
			if (head != null && head.length >= n)
				return head[n].getName();
		}

		return null;
	}

	public int getResponseCode() {
		if (mHttpResponse != null && mHttpResponse.getStatusLine() != null)
			return mHttpResponse.getStatusLine().getStatusCode();

		return -1;
	}

	public void open(String url) {
		mUrl = url;
	}

	public DataInputStream openDataInputStream() throws IOException {
		return null;
	}

	public DataOutputStream openDataOutputStream() throws IOException {
		return null;
	}

	public InputStream openInputStream() throws IOException {
		if (mHttpResponse != null && mHttpResponse.getEntity() != null) {
			return mHttpResponse.getEntity().getContent();
		} else {
			return null;
		}
	}

	public OutputStream openOuputStream() throws IOException {
		return null;
	}

	@Override
	public void setPostData(String data) {
		mPostData = data;
	}

	public void setRequestMethod(String method) {
		mMethod = method;
	}

	public void setRequestProperty(String key, String value) {
		mRequestProperty.add(new BasicNameValuePair(key,value));
	}

	public long getContentLength() {
		if (mHttpResponse != null) {
			HttpEntity entity = mHttpResponse.getEntity();
			if (entity != null)
				return entity.getContentLength();
		}

		return 0;
	}


	static public boolean isWap(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if (nInfo == null || nInfo.getType() != ConnectivityManager.TYPE_MOBILE)
			return false;
		String extraInfo = nInfo.getExtraInfo();
		if (extraInfo == null || extraInfo.length() < 3)
			return false;
		if (extraInfo.toLowerCase(Locale.US).contains("wap"))
			return true;
		return false;
	}

	@Override
	public void close() throws IOException {

		if (mHttpResponse != null && mHttpResponse.getEntity() != null) {
			try {
				InputStream in = mHttpResponse.getEntity().getContent();
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {

			}
		}
	}



	@Override
	public void setMultiPart(Map<String, Object> files) {
		mMultiPartFile = files;
	}


	@Override
	public void addMutliParam(String key, String value) {
		mMultiParams.add(new BasicNameValuePair(key, value));
		
	}


	public byte[] getPostRawData() {
		return mPostRawData;
	}


	@Override
	public void setPostRawData(byte[] postRawData) {
		this.mPostRawData = postRawData;
	}


	@Override
	public void setPostProgressListener(OnPostProgressListener listener) {
		mPostProgressListener = listener;		
	}
	
}
