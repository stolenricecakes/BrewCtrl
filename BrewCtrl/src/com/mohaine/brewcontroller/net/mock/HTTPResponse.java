package com.mohaine.brewcontroller.net.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HTTPResponse {

	private final OutputStream outputStream;
	private int statusCode = HttpCodes.OK;
	private String status = "OK";
	private String protocol = "HTTP/1.1";

	private final Map<String, String> headerMap = new HashMap<String, String>();
	private boolean wroteHeaders;

	/**
	 * @param socket
	 * @throws IOException
	 */
	public HTTPResponse(Socket socket) throws IOException {
		super();
		outputStream = socket.getOutputStream();
	}

	public HTTPResponse(OutputStream stream) throws IOException {
		super();
		outputStream = stream;
	}

	public void close() {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public void setContentLength(int length) {
		headerMap.put("Content-Length", Integer.toString(length));
	}

	public void setContentType(String contentType) {
		headerMap.put("Content-Type", contentType);
	}

	public void sendContent(byte[] data) throws IOException {
		setContentLength(data.length);
		writeHeaders();
		outputStream.write(data);
	}

	public void sendContent(String data) throws IOException {
		sendContent(data.getBytes());
	}

	public void send() throws IOException {
		writeHeaders();
	}

	private void writeHeaders() throws IOException {
		if (!this.wroteHeaders) {
			this.wroteHeaders = true;
			writeLineRaw(protocol + " " + statusCode + " " + status);
			Set<String> keySet = headerMap.keySet();
			for (String header : keySet) {
				String value = headerMap.get(header);
				writeLineRaw(header + ": " + value);
			}
			writeLineRaw("");
		}
	}

	private void writeLineRaw(String line) throws IOException {
		outputStream.write(line.getBytes());
		outputStream.write("\r\n".getBytes());
	}

	private void writeRaw(String line) throws IOException {
		outputStream.write(line.getBytes());
	}

	public void writeLine(String line) throws IOException {
		writeHeaders();
		writeLineRaw(line);
	}

	public void write(String line) throws IOException {
		writeHeaders();
		writeRaw(line);
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}