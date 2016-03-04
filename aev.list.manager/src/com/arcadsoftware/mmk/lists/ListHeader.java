package com.arcadsoftware.mmk.lists;

import java.util.Date;

public class ListHeader {
	private String description = ""; 
	private String comment = "";	
	
	private Date createdThe = null;
	private String createdBy = null;	
	private Date lastModifiedThe = null;	
	private String lastModifiedBy = null;
	
	
	public ListHeader clone(){
		ListHeader h =new ListHeader();
		h.setComment(comment);
		h.setDescription(description);
		h.setCreatedThe(createdThe);
		h.setCreatedBy(createdBy);
		h.setLastModifiedThe(lastModifiedThe);
		h.setLastModifiedBy(lastModifiedBy);
		return h;		
	}
	
	public void assign(ListHeader h){
		setComment(h.getComment());
		setDescription(h.getDescription());
		setCreatedThe(h.getCreatedThe());
		setCreatedBy(h.getCreatedBy());
		setLastModifiedThe(h.getLastModifiedThe());
		setLastModifiedBy(h.getLastModifiedBy());		
	}	
	
	/**
	 * Renvoit 
	 * @return the comment String : 
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * Renvoit 
	 * @return the createdBy String : 
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * Renvoit 
	 * @return the createdThe Date : 
	 */
	public Date getCreatedThe() {
		return createdThe;
	}
	/**
	 * @param createdThe the createdThe to set
	 */
	public void setCreatedThe(Date createdThe) {
		this.createdThe = createdThe;
	}
	/**
	 * Renvoit 
	 * @return the description String : 
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Renvoit 
	 * @return the lastModifiedBy String : 
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	/**
	 * Renvoit 
	 * @return the lastModifiedThe Date : 
	 */
	public Date getLastModifiedThe() {
		return lastModifiedThe;
	}
	/**
	 * @param lastModifiedThe the lastModifiedThe to set
	 */
	public void setLastModifiedThe(Date lastModifiedThe) {
		this.lastModifiedThe = lastModifiedThe;
	}
}

