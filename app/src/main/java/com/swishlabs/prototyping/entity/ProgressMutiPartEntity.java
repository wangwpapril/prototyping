package com.swishlabs.prototyping.entity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ProgressMutiPartEntity extends MultipartEntity {

	private final ProgressListener listener;

	public ProgressMutiPartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public ProgressMutiPartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public ProgressMutiPartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	@Override
	public InputStream getContent() throws IOException, UnsupportedOperationException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		writeTo(bao);		
		ByteArrayInputStream bio = new ByteArrayInputStream(bao.toByteArray());
		return bio;
	}
	
	public  class CountingOutputStream extends FilterOutputStream {
		
		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out,
				final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;									
			this.listener.transferred(this.transferred);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;			
			this.listener.transferred(this.transferred);
		}
	}

}
