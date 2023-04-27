package com.employeedir.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prospect_links")
public class ProspectLinks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "link_id")
	private int id;
	
	@Column(name = "linkedin")
	private String linkedIn;
	
	@Column(name = "instagram")
	private String instagram;
	
	@Column(name = "facebook")
	private String facebook;
	
	@OneToOne
	@JoinColumn(name = "prospect_id")
	private Prospects prospects;
	
	public ProspectLinks() {
		
	}

	public ProspectLinks(String linkedIn, String instagram, String facebook) {
		this.linkedIn = linkedIn;
		this.instagram = instagram;
		this.facebook = facebook;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLinkedIn() {
		return linkedIn;
	}

	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public Prospects getProspects() {
		return prospects;
	}

	public void setProspects(Prospects prospects) {
		this.prospects = prospects;
	}

	@Override
	public String toString() {
		return "ProspectLinks [id=" + id + ", linkedIn=" + linkedIn + ", instagram=" + instagram + ", facebook=" + facebook
				+ ", prospects=" + prospects + "]";
	}
	
	
}
