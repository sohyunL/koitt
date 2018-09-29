package article.service;

import article.model.Article;
import article.model.ArticleContent;

// 게시글 보기에 필요한 객체
public class ArticleData {
	private Article article; // 아티클 테이블 내용
	private ArticleContent content; // 아티클 컨텐트 테이블 내용
	
	public ArticleData(Article article, ArticleContent content) {
		this.article = article;
		this.content = content;
	}
	
	public Article getArticle() {
		return article;
	}

	public String getContent() {
		return content.getContent( );
	}
	
}
