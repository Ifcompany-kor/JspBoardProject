package com.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.board.model.ArticleVO;
import com.board.service.BoardService;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	BoardService boardService;
	ArticleVO articleVO;
	
	@Override
	public void init() throws ServletException {
		boardService = new BoardService();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doHandle(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doHandle(req,resp);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo();
		System.out.println("action:" + action);
		
		try {
			List<ArticleVO> arList = new ArrayList<ArticleVO>();
			if(action==null || action.equals("/listArticles.do"))
			{
				arList = boardService.listArticles();
				request.setAttribute("articlesList", arList);
				nextPage = "/boardView/listArticles.jsp";
			}
			else if(action.equals("/articleForm.do"))
			{
				nextPage = "/boardView/articleForm.jsp";
			}
			else if(action.equals("/addArticle.do"))
			{
				Map<String, String> articleMap = upload(request,response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				articleVO = new ArticleVO();
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				
				boardService.addArticle(articleVO);
				nextPage = "/board/listArticles.do";
			}
			else if(action.equals("/viewArticle.do")){
				String articleNO = request.getParameter("no");
				articleVO=boardService.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article",articleVO);
				nextPage = "/boardView/viewArticle.jsp";
			
			}
			else if (action.equals("/modArticle.do")) {
				Map<String, String> articleMap = upload(request,response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				articleVO = new ArticleVO();
				
				articleVO.setNo(Integer.parseInt(articleMap.get("articleNO")));
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				
				boardService.modArticle(articleVO);
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('글을 수정했습니다.');" + " location.href='" + request.getContextPath()
						+ "/board/viewArticle.do?no=" + articleMap.get("articleNO") + "';" + "</script>");
				return;
			}else if (action.equals("/removeArticle.do")) {
				int articleNO = Integer.parseInt(request.getParameter("articleNO"));
				
				boardService.removeArticle(articleNO);
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('글을 삭제했습니다.');" + " location.href='" + request.getContextPath()
						+ "/board/listArticles.do';" + "</script>");
				return;
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else {
					System.out.println("파라미터명:" + fileItem.getFieldName());
					//System.out.println("파일명:" + fileItem.getName());
					System.out.println("파일크기:" + fileItem.getSize() + "bytes");
					//articleMap.put(fileItem.getFieldName(), fileItem.getName());
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}

						String fileName = fileItem.getName().substring(idx + 1);
						System.out.println("파일명:" + fileName);
						articleMap.put(fileItem.getFieldName(), fileName);  //익스플로러에서 업로드 파일의 경로 제거 후 map에 파일명 저장
						
						int no = boardService.selectNewArticleNO();
						File folder = new File(currentDirPath+"\\"+no);
						if(!folder.exists()) {
							folder.mkdir();
						}
						File uploadFile = new File(currentDirPath + "\\" + boardService.selectNewArticleNO() + "\\" +fileName);
						fileItem.write(uploadFile);

					} // end if
				} // end if
			} // end for
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleMap;
	}
}
