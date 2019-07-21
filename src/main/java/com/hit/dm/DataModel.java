package com.hit.dm;

import java.io.Serializable;
import java.util.Objects;

public class DataModel<T> implements Serializable {
	private Long dataModelId;
	private T content;
	
	public DataModel(Long id,T content) {
		dataModelId = id;
		this.content = content;
	}
	
	public int hashCode() {
		return Objects.hash(dataModelId,content);
	}
	
	public boolean equals(java.lang.Object obj) {
		DataModel<?> t = (DataModel<?>)obj;
		return (this.dataModelId == t.dataModelId && this.content.equals(t.content));
	}
	
	public String toString(){
		if(content == null) {
			return dataModelId.toString().concat(" ").concat("null");
		}
		return dataModelId.toString().concat(" ").concat(content.toString());
	}
	
	public Long getDataModelId(){
		return dataModelId;
	}
	
	public void setDataModelId(java.lang.Long id) {
		this.dataModelId = id;
	}
	
	public T getContent() {
		return content;
	}
	
	public void setContent(T content) {
		this.content= content;
	}
	

}
