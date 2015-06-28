package com.swishlabs.prototyping.pal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface IHttp {

	public final static String MULTIPART_FORM_DATA = "multipart/form-data";
	public final static String POST_FORM_DATA = "application/x-www-form-urlencoded";
	public static final String TRANSFER_ENCODING = "Transfer-Encoding";
	public static final String TRANSFER_ENCODING_CHUNKED = "chunked";

	public void open(String url);

	public void close() throws IOException;

	public void setRequestProperty(String key, String value);

	public void setRequestMethod(String method);

	public void setMultiPart(Map<String, Object> file);

	public void addMutliParam(String key, String value);

	public void setPostData(String data);

	public void setPostRawData(byte[] data);

	public int execute() throws IOException;

	public int getResponseCode();

	public String getHeaderField(String name) throws IOException;

	public String getHeaderField(int n) throws IOException;

	public String getHeaderFieldKey(int n) throws IOException;

	public InputStream openInputStream() throws IOException;

	public DataInputStream openDataInputStream() throws IOException;

	public OutputStream openOuputStream() throws IOException;

	public DataOutputStream openDataOutputStream() throws IOException;

	public long getContentLength() throws IOException;

	public interface OnPostProgressListener {
		void onProgress(int progress);
	}

	public void setPostProgressListener(OnPostProgressListener listener);

}
