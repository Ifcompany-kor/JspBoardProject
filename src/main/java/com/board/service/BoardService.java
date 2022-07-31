package com.board.service;

import java.util.List;

import com.board.dao.BoardDAO;
import com.board.model.ArticleVO;

public class BoardService {
	BoardDAO boardDAO;

	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles() {
		List<ArticleVO> artiList = boardDAO.selectAllArticles();
		return artiList;
	}

	public void addArticle(ArticleVO article) {
		boardDAO.insertNewArticle(article);	
	}

	public ArticleVO viewArticle(int articleNO) {
		ArticleVO article = null;
		article = boardDAO.selectArticle(articleNO);
		return article;
	}

	public void modArticle(ArticleVO articleVO) {
		boardDAO.updateArticle(articleVO);
	}

	public void removeArticle(int articleNO) {
		boardDAO.deleteArticle(articleNO);
	}

}
