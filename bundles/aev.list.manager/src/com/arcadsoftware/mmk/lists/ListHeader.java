package com.arcadsoftware.mmk.lists;

import java.util.Date;

public class ListHeader {
	private String comment = "";
	private String createdBy = null;

	private Date createdThe = null;
	private String description = "";
	private String lastModifiedBy = null;
	private Date lastModifiedThe = null;

	public void assign(final ListHeader h) {
		setComment(h.getComment());
		setDescription(h.getDescription());
		setCreatedThe(h.getCreatedThe());
		setCreatedBy(h.getCreatedBy());
		setLastModifiedThe(h.getLastModifiedThe());
		setLastModifiedBy(h.getLastModifiedBy());
	}

	@Override
	public ListHeader clone() {
		final ListHeader h = new ListHeader();
		h.setComment(comment);
		h.setDescription(description);
		h.setCreatedThe(createdThe);
		h.setCreatedBy(createdBy);
		h.setLastModifiedThe(lastModifiedThe);
		h.setLastModifiedBy(lastModifiedBy);
		return h;
	}

	/**
	 * Renvoit
	 * 
	 * @return the comment String :
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Renvoit
	 * 
	 * @return the createdBy String :
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Renvoit
	 * 
	 * @return the createdThe Date :
	 */
	public Date getCreatedThe() {
		return createdThe;
	}

	/**
	 * Renvoit
	 * 
	 * @return the description String :
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Renvoit
	 * 
	 * @return the lastModifiedBy String :
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * Renvoit
	 * 
	 * @return the lastModifiedThe Date :
	 */
	public Date getLastModifiedThe() {
		return lastModifiedThe;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @param createdThe
	 *            the createdThe to set
	 */
	public void setCreatedThe(final Date createdThe) {
		this.createdThe = createdThe;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(final String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @param lastModifiedThe
	 *            the lastModifiedThe to set
	 */
	public void setLastModifiedThe(final Date lastModifiedThe) {
		this.lastModifiedThe = lastModifiedThe;
	}
}
