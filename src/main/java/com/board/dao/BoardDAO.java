package com.board.dao;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.board.model.ArticleVO;

public class BoardDAO {
	private DataSource dataFactory;
	Connection conn;
	PreparedStatement pstmt;

	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/mysql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List selectAllArticles() {
		List articlesList = new ArrayList();
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT * FROM t_board";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date writeData = rs.getDate("writeData");
				ArticleVO article = new ArticleVO();
				article.setNo(no);
				article.setTitle(title);
				article.setContent(content);
				article.setWriteData(writeData);
				articlesList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articlesList;
	}

	public void insertNewArticle(ArticleVO article) {
		try {
			conn = dataFactory.getConnection();
			String title = article.getTitle();
			String content = article.getContent();
			String imageFileName = article.getImageFileName();
			String query = "INSERT INTO t_board (title, content, imageFileName)"
					+ " VALUES (?, ? ,?)";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setString(3, imageFileName);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArticleVO selectArticle(int articleNO){
		ArticleVO article=new ArticleVO();
		try{
			conn = dataFactory.getConnection();
			String query ="select no,title,content,  IFNULL(imageFileName, 'null') as imageFileName, writeData"
				                     +" from t_board" 
				                     +" where no=?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs =pstmt.executeQuery();
			rs.next();
			int no =rs.getInt("no");
			String title = rs.getString("title");
			String content =rs.getString("content");
			String imageFileName = URLEncoder.encode(rs.getString("imageFileName"), "UTF-8"); //파일이름에 특수문자가 있을 경우 인코딩합니다.
			if(imageFileName.equals("null")) {
				imageFileName = null;
			}
			Date writeDate = rs.getDate("writeData");
	
			article.setNo(no);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setWriteData(writeDate);
			rs.close();
			pstmt.close();
			conn.close();
			}catch(Exception e){
			e.printStackTrace();	
			}
		return article;
		}

	public void updateArticle(ArticleVO article) {
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName = article.getImageFileName();
		try {
			conn = dataFactory.getConnection();
			String query = "update t_board  set title=?,content=?";
			if (imageFileName != null && imageFileName.length() != 0) {
				query += ",imageFileName=?";
			}
			query += " where no=?";

			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if (imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, article.getNo());
			} else {
				pstmt.setInt(3, article.getNo());
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteArticle(int articleNO) {
		try {
			conn = dataFactory.getConnection();
			String query = "DELETE FROM t_board ";
			query += " WHERE no = ?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int selectNewArticleNO() {
		int no = 0;
		try {
			conn = dataFactory.getConnection();
			String query = "select ifnull(max(no)+1,1) no from t_board";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				no = rs.getInt("no");
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return no;
	}
	
}
