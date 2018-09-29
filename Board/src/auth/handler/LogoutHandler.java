package auth.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.handler.CommandHandler;

public class LogoutHandler implements CommandHandler {
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 세션을 날리고
		// 기존에 있는 세션만 받도록 한다.
		HttpSession session = req.getSession(false);
		if(session != null) {
			session.invalidate( );
		}
		
		// 리다이렉트로 돌려줌.
		resp.sendRedirect(req.getContextPath( )+"/index.jsp");
		return null;
	}

}
