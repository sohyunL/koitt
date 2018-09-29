package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.ConnectionProvider;

public class WriteArticleService {
	private static WriteArticleService instance = new WriteArticleService( );
	private WriteArticleService( ) { }
	public static WriteArticleService getInstance( ) {
		return instance;
	}
	public int write(WriteRequest wr) {
		ArticleDao articleDao = ArticleDao.getInstance( );
		ArticleContentDao articleContentDao = ArticleContentDao.getInstance( );
		
		// 게시글의 테이블이 두개가 있고, 두개의 articleId는 같아야 무결성을 해치지 않음.
		// 따라서 두개의 articleId는 동기화 되어야하고,
		// article이 삽입 됐을 때 articleId가 생성되기 때문에
		// 삽입 후 select를 하여 articleId를 받아온다.
		// 그리고 그 articleId를 content(내용) 삽입시 사용하여 articleId가 같게 처리함.
		try(Connection conn = ConnectionProvider.getConnection( )){
			try {
				// article -> articleId 을 받을 수 있음. article_content, articleId 가 같아야한다.
				// article이 성공하고 article_content 실패하면 안되니 롤백 해줘야한다.
				conn.setAutoCommit(false);
				Article article = new Article(wr.getWriter( ), wr.getTitle( ));
				
				Article savedArticle = articleDao.insert(conn, article);
				if(savedArticle == null) {
					throw new RuntimeException("article 삽입 실패");
				}
				ArticleContent content = new ArticleContent(savedArticle.getArticleId( ), wr.getContent( ));
				ArticleContent savedContent = articleContentDao.insert(conn, content);
				if(savedContent == null) {
					throw new RuntimeException("content 삽입 실패");
				}
				conn.commit( );
				return savedArticle.getArticleId( );
				
			}catch(SQLException e) {
				conn.rollback( );
				throw new RuntimeException(e);
			}
			catch(RuntimeException e) {
				conn.rollback( );
				throw e;
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
