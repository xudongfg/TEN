package com.ten.beans;

//Fields common for all the classes in ten ontology
public class TenCommonAnnoationsBean{
	String modified;
	String rightsHolder;
	
	String isPartOf;
	String hasPart;
	
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getRightsHolder() {
		return rightsHolder;
	}
	public void setRightsHolder(String rightsHolder) {
		this.rightsHolder = rightsHolder;
	}
	public String getIsPartOf() {
		return isPartOf;
	}
	public void setIsPartOf(String isPartOf) {
		this.isPartOf = isPartOf;
	}
	public String getHasPart() {
		return hasPart;
	}
	public void setHasPart(String hasPart) {
		this.hasPart = hasPart;
	}
	
	public TenCommonAnnoationsBean(){
		super();
		modified = "";
		rightsHolder = "";
		
		isPartOf = "";
		hasPart = "";
	}
}
