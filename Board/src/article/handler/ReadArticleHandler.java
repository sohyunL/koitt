package article.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ReadArticleService;
import common.exception.ArticleContentNotFoundException;
import common.exception.ArticleNotFoundException;
import common.handler.CommandHandler;

public class ReadArticleHandler implements CommandHandler {
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 사용자에게 어떤 요청을 받고
		// 서비스를 이용해서 화면에 보여줄 데이터를 생성하고
		int articleId = Integer.parseInt(req.getParameter("no"));
		ReadArticleService articleService = ReadArticleService.getInstance( );
		// 화면으로 리턴
		try {
			ArticleData articleData = articleService.getArticle(articleId, true);
			/*articleData.getContent( ).replace(oldChar, newChar);*/ // 치환
			req.setAttribute("articleData", articleData);
			return "/WEB-INF/view/readArticle.jsp";
			// 없는 데이터라면
		}catch(ArticleNotFoundException | ArticleContentNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

}
