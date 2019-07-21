package com.hit.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.*;
import com.hit.dm.DataModel;

public class Request<T> implements Serializable {
	
	private Map<String,String> headers = new HashMap<String,String>();
	private T body;
	
	public Request(Map<String,String> headers,T body) {
		headers = headers;
		body = body;
	}
	
	public Map<String,String> getHeaders(){
		return headers;
	}
	
	public void setHeaders(Map<String,String> headers) {
		this.headers = headers;
	}
	
	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}
	
	public String toString(){ 
		return "Headers: " + headers.toString() + " Body: "+ body.toString();
	}

}
