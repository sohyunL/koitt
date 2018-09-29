package user.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.exception.DuplicateException;
import common.handler.CommandHandler;
import user.service.JoinRequest;
import user.service.JoinService;

// 사용자의 요청을 받아서 폼 화면을 보여줄지, 데이타로 회원가입을 시킬지 구분하여 처리
public class JoinHandler implements CommandHandler{

	// 회원가입 페이지 주소를 상수로 만듬
	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	
	// 사용자는 url board/join -> 
	// form에서 전송할 action 역시 board/join
	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if(req.getMethod( ).equalsIgnoreCase("GET")) {
			// GET 방식으로 요청이 오면 폼을 보여주는 뷰로 리턴을 하고(url로 쳐서 옴)
			return processForm(req, resp);
		}else if(req.getMethod( ).equalsIgnoreCase("POST")) {
			// POST 방식으로 요청이 오면 회원가입을 처리하고 결과를 보여주는 뷰로 리턴(입력받아 옴)
			return processSubmit(req, resp);
		}else {
			resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse resp) {
		return FORM_VIEW;
	}
	
	// 사용자로부터 회원가입 데이터를 입력받아
	// submit 버튼을 클릭해서 데이타가 넘어왔을 때 실행하는 메소드
	private String processSubmit(HttpServletRequest req, HttpServletResponse resp) {
		// 파라미터를 통해서 입력받은 데이터를 joinRequest 객체에 담음.
		JoinRequest joinRequest = new JoinRequest( );
		joinRequest.setLoginId(req.getParameter("loginId"));
		joinRequest.setName(req.getParameter("name"));
		joinRequest.setPassword(req.getParameter("password"));
		joinRequest.setConfirmPassword(req.getParameter("confirmPassword"));
		
		// joinRequest를 통해 입력받은 데이터가 제대로 입력되어 있는지,
		// 잘못된 정보는 errors 라는 맵에 넣어 놓기 위해 errors라는 맵을 생성
		Map<String, Boolean> errors = new HashMap<String, Boolean>( );
		
		// errors는 view에 표출해주기 위해 request 속성값으로 넣어줌.
		req.setAttribute("errors", errors);
		
		// 데이터 검증! , 무결성 체크
		// 또는 패스워드와 패스워드 확인 두개가 서로 같은지 확인
		joinRequest.validate(errors);
		// validate 메소드가 지나오면, errors 맵에는 잘못된 데이터필드명(키)과 함께 true value 값이
		// 추가되어 있음.
		
		// 잘못 들어왔으면 다시 폼화면으로 반환
		if(!errors.isEmpty( )) {
			return FORM_VIEW;
		}
		
		// 그래서 잘 입력되면 joinService를 통해서 회원가입 로직 수행
		JoinService joinService = JoinService.getInstance();
		try {
		// join인 로직은 아이디가 중복일 때 예외를 여기로 던져줌.
		joinService.join(joinRequest);
		// 성공화면으로 반환
		return "/WEB-INF/view/joinSuccess.jsp";
		
		}catch(DuplicateException e) {
			// 아이디가 중복일 때 service에서 발생시킨 예외를 받아서 처리해줌.
			errors.put("duplicateId", true);
			return FORM_VIEW;
		}
	}
	
	
}
