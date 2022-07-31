package com.board.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;

public class ArticleVO {
	private int no;
	private String title;
	private String content;
	private String imageFileName;
	private String id;
	private Date writeData;
	
	public ArticleVO() {
		
	}




	public ArticleVO(int no, String title, String content, String imageFileName, String id, Date writeDate) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.imageFileName = imageFileName;
		this.id = id;
		this.writeData = writeDate;
	}




	public int getNo() {
		return no;
	}




	public void setNo(int no) {
		this.no = no;
	}




	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}
	
	public String getImageFileName() {
		try {
			if (imageFileName != null && imageFileName.length() != 0) {
				imageFileName = URLDecoder.decode(imageFileName, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		try {
			if(imageFileName!=null && imageFileName.length()!=0) {
				this.imageFileName = URLEncoder.encode(imageFileName, "UTF-8");  //파일이름에 특수문자가 있을 경우 인코딩합니다.
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}




	public Date getWriteData() {
		return writeData;
	}




	public void setWriteData(Date writeData) {
		this.writeData = writeData;
	}


	
	
	
	
	

}
