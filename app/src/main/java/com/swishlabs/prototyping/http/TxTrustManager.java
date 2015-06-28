package com.swishlabs.prototyping.http;

import android.util.Log;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TxTrustManager implements X509TrustManager {
	private X509TrustManager standardTrustManager = null;
	
	public TxTrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
		super();
		TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmfactory.init(keystore);
		TrustManager[] tms = tmfactory.getTrustManagers();
		if ( tms.length <= 0 )
			throw new NoSuchAlgorithmException("No Trust Manager found");
		standardTrustManager = (X509TrustManager)tms[0];
	}
	
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		standardTrustManager.checkClientTrusted(chain, authType);
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		for ( X509Certificate cert:chain)
		Log.d("ServerCert", cert.toString());

	}

	public X509Certificate[] getAcceptedIssuers() {
		return standardTrustManager.getAcceptedIssuers();
	}
}
