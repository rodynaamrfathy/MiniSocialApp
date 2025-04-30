package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Post {
	
	@Temporal(TemporalType.DATE)
	protected Date publishDate;
	
	
	protected String imageUrl;
	
	@Column(length = 50)
	protected String content;
	
	private int likesCount;

}
