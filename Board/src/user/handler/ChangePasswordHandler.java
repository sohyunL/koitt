package user.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.AuthUser;
import common.exception.InvalidPasswordException;
import common.exception.UserNotFoundException;
import common.handler.CommandHandler;
import user.service.ChangePasswordService;

public class ChangePasswordHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/changePwdForm.jsp";
	
	// 입력받은 데이타(비밀번호)가 문제가 있는지 무결성 체크 후
	// 비밀번호 화면으로 돌려보내거나, 정보 수정 후 성공화면으로 보냄.
	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if(req.getMethod( ).equalsIgnoreCase("GET")) {
			return processFrom(req, resp);
		}else if(req.getMethod( ).equalsIgnoreCase("POST")){
				return porcessSubmit(req, resp);
		}else {
			resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processFrom(HttpServletRequest req, HttpServletResponse resp) {
		return FORM_VIEW;
	}
	
	private String porcessSubmit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// form_view 로 부터 입력받은 비밀번호의 무결성 검증을 해야하고
		// 세션에 있는 auth 객체로부터 로그인ID 정보를 받아오고
		// 서비스를 이용해서 비번 변경 수행
		AuthUser authUser = (AuthUser)req.getSession( ).getAttribute("authUser");
		
		Map<String, Boolean> errors = new HashMap<String, Boolean>( );
		req.setAttribute("errors", errors);
		
		String oldPwd = req.getParameter("oldPwd");
		String newPwd = req.getParameter("newPwd");
		
		if(oldPwd == null || oldPwd.isEmpty( )) {
			errors.put("oldPwd", true);
		}
		if(newPwd == null || newPwd.isEmpty( )) {
			errors.put("newPwd", true);
		}
		
		if(oldPwd.equals(newPwd)) {
			errors.put("same", true);
		}
		
		if(!errors.isEmpty( )) {
			return FORM_VIEW;
		}
		
		try {
			ChangePasswordService changePasswrodService = ChangePasswordService.getInstance( );
			changePasswrodService.changePassword(authUser.getLoginId( ), oldPwd, newPwd);
			return "/WEB-INF/view/changePwdSuccess.jsp";
		
		}catch(InvalidPasswordException e){
			errors.put("wrongOldPwd", true);
			return FORM_VIEW;
		}catch(UserNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}
}
