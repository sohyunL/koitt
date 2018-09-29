package article.service;

import java.util.List;

import article.model.Article;

// 한 페이지에서 보여줄 게시글 정보와 페이지 관련 정보를 담는 클래스
// 페이지(게시글 목록의 한 화면)에 여기서 담겨있는 내용을 출력할 예정.
public class ArticlePage {
	
		// 게시글id, 제목, 작성자, 작성자id, 작성일, 수정일, 조회수
		private List<Article> artList; // 게시글 정보을 담고있는 객체들의 목록
		private int currentPage; // 사용자가 요청한 페이지 번호
		private int totalPages; // 전체 페이지 수 한페이지에 10개씩 보여주는 11개 일때는 2개 페이지가 됨.
		private int total; // 게시글의 전체 개수
		private int startPage; // 화면 하단에 보여줄 페이지 링크의 시작번호
		private int endPage; // 화면 하단에 보여줄 페이지 링크의 끝 번호
		
		public ArticlePage(List<Article> artList, int currentPage, int total, int size, int blockSize) {

			// size는 한 페이지에 보여줄 게시글의 개수
			// blockSize는 한 페이지에서 보여줄 하단 페이지 링크 블럭 개수
			this.artList = artList;
			this.currentPage = currentPage;
			this.total = total;
			if(total == 0) {
				totalPages = 0;
				startPage = 0;
				endPage = 0;
			}else {
				totalPages = total/size;
				if(total % size > 0) { // 51/10 이면 나머지 1이 있어서
					totalPages++; // 페이지를 하나더 추가
				}
				// [ ] [ ] [ ] [ ] [ ]
				startPage = currentPage / blockSize * blockSize + 1;
				// 페이지 번호와 블럭사이즈가 같아 페이지가 넘어가는것을 막기 위함.
				if((currentPage % blockSize) == 0){ 
					startPage -= blockSize;
				}
				// [1] [2] [3]
				endPage = startPage + (blockSize - 1);
				if(endPage > totalPages) {
					endPage = totalPages;
				}
			}
		}
		
		public boolean hasNoArticles( ) {
			return total > 0;
		}
		public List<Article> getArtList() {
			return artList;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public int getTotal() {
			return total;
		}

		public int getStartPage() {
			return startPage;
		}

		public int getEndPage() {
			return endPage;
		}
		

		

}
