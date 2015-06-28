package com.swishlabs.prototyping.http;


import com.swishlabs.prototyping.pal.IHttp;

import java.io.InputStream;

public interface HttpCallBack
{
	
	public void onError(int requestId, int errCode, byte[] errStr, IHttp http);
	
	public void onReceived(int requestId, byte[] data, IHttp http);
	
	public void onReceived(int requestId, InputStream stream, long contentLength, IHttp http);
}
