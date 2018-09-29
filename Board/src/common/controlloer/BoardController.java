package common.controlloer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.handler.CommandHandler;
import common.handler.NullHandler;

public class BoardController extends HttpServlet {
	
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

	@Override
	public void init() throws ServletException {
		
		String handlerConfigFile = getInitParameter("handlerConfigFile");
		String handlerConfigFilePath = getServletContext().getRealPath(handlerConfigFile);
		
		Properties prop = new Properties();

		try(FileReader fR = new FileReader(handlerConfigFilePath);){
			prop.load(fR);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Properties 객체에 담긴 정보들을 commandHandlerMap에 객체로 만들어서 담아냄
		for(Object key : prop.keySet()) {
			String command = (String)key;
			String handlerClassName = prop.getProperty(command);
			
			try {
				Class handlerClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance = (CommandHandler)handlerClass.getDeclaredConstructor().newInstance();
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String command = req.getRequestURI();
		
		// 앞의 Context 부분을 잘라내고 주소 뒤의 명령부분만 남김
		if(command.indexOf(req.getContextPath()) == 0) {
			command = command.substring(req.getContextPath().length());
		}
		
		CommandHandler handler = null;
		if(command == null) {	// null일 때 null 핸들러로 빈 페이지를 반환함
			handler = new NullHandler();
		}else {
			handler = commandHandlerMap.get(command);	// command 명령어를 이용하여 Handler 객체를 받음
		}
		
		// Handler 객체로부터 결과 페이지 정보를 받고
		String viewPage = null;
		try {
			viewPage = handler.process(req, resp);	// Handler객체를 통해 view 페이지의 페이지 정보를 받음
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 결과 페이지로 전환시킴
		if(viewPage != null) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(viewPage);
			dispatcher.forward(req, resp);
		}

	}
	
	

}
