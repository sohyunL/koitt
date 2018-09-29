package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

// Encoding을 해주는 Filter
public class CharacterEncodingFilter implements Filter {

	private String encoding;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("encoding filter start");
		
		request.setCharacterEncoding(encoding);	// 설정에서 가져오는 경우
		
		chain.doFilter(request, response);
		
		System.out.println("encoding filter end");

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		// web.xml의 설정 값으로 encoding을 설정하는데, 없다면 기본으로 utf-8을 하겠다는 의미
		
		encoding = config.getInitParameter("encoding");
		
		if(encoding == null) {
			encoding = "utf-8";	// encoding이 설정되어 있지 않다면 utf-8로 설정
		}

	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
