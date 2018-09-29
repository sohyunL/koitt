package jdbc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

// DB Connection Pool을 준비하기 위한 Context Listener 클래스
public class DBCPInitListener implements ServletContextListener {

	
	// 시작할 때 우리의 DB 커넥션 풀을 셋팅
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		// Servletcontext 생성
			// 서블릿 컨텍스트를 받음
		ServletContext sc = sce.getServletContext();
		
		// web.xml에 등록된 Parameter를 받아옴
			// 서블릿 컨텍스트를 통해서 Init 파라미터(DB 정보가 있는 properties 파일)를 받음
		// String poolConfigFile = sc.getInitParameter("poolConfigFile");
		
		// 파일 주소로 파일을 읽어야 함 (web.xml에 있는 주소로는 부족함. 시스템 주소가 필요함)
		String poolConfigFile = sc.getRealPath(sc.getInitParameter("poolConfigFile")); 
		
		// 파일 주소로 프로퍼티스 객체에 파일에 있는 데이터를 넣을 것임
		Properties prop = new Properties();	// 데이터를 담을 Properties 객체 생성
		try {
			
			prop.load(new FileReader(poolConfigFile));
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Not Found poolConfigFile", e);	// 오류를 직접처리를 하도록 오류를 날려줌
		} catch (IOException e) {
			throw new RuntimeException("Fail to Read poolConfigFile", e);
		}
		
		loadJDBCDriver(prop);	// JDBC Driver 로드
		initConnectionPool(prop);	// 커넥션 풀 초기화

	}

	private void initConnectionPool(Properties prop) {
		
		try {
			
			// 1. 커넥션 풀이 새로운 커넥션을 생성할 때 사용할 커넥션 팩토리 생성 (DB 접속 정보를 인자로 넣고 커넥션을 만들어 주는 팩토리 객체를 생성)
			ConnectionFactory connFactory = new DriverManagerConnectionFactory(prop.getProperty("jdbcURI"), prop.getProperty("dbUser"), prop.getProperty("dbPwd"));
			
			// 2. 커넥션을 보관할 Pool을 생성하는 PoolablerConnection을 생성하는 팩토리 생성 (Pool에서 쓸 수 있는 커넥션을 만들어주는 팩토리에 커넥션 팩토리를 넣고 생성함)
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connFactory, null);
			
			// 3. 커넥션들이 유효한지 확인하기 위한 작업 (커넥션이 유효한지 체크하기 위한 쿼리를 지정)
			poolableConnectionFactory.setValidationQuery(prop.getProperty("validationQuery", "select 1"));	// prop.getProperty(A, B); -> A : 파일에 정의되어 있는 값, B: 없을 시 기본값으로 해줄 값 (default)
			
			// 4. 커넥션 풀의 설정 정보를 담을 변수 생성 (커넥션 풀의 설정 정보를 다루는 객체를 생성하고 설정 정보 셋팅)
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
			
				// 4-1. 커넥션 풀의 설정정보 설정 (검사주기 셋팅 : 어느정도 기간 동안 커넥션을 유요하게 둘 것인지를 설정하는 것)
				poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);
				
				// 4-2. 커넥션 풀의 설정정보 설정 (풀에 보관중인 커넥션이 유효한지 검사 여부 : 유효한지 테스트를 할건지 말건지 결정하는 것)
				poolConfig.setTestWhileIdle(true);
				
				// 4-3. 커넥션 풀의 설정정보 설정 (커넥션 최소 갯수 : 커넥션 풀 안에서의 최소로 유지하려는 커넥션의 갯수)
				poolConfig.setMinIdle(Integer.parseInt(prop.getProperty("minIdle", "5")));	// 프로퍼티는 String 형태의 값만을 가지므로 String형태로 표현 후 Integer.parseInt()를 통해 숫자 형태로 변환해줌
				
				// 4-4. 커넥션 풀의 설정정보 설정 (커넥션 최대 갯수 : 커넥션 자체의 총 갯수, 풀 안에 있든 사용 중이든 상관없이 총 갯수)
				poolConfig.setMaxTotal(Integer.parseInt(prop.getProperty("maxTotal", "50")));
				
			// 5. 커넥션 풀 생성 시 팩토리와 커넥션 설정을 받음 (커넥션 풀 객체 생성)
			GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, poolConfig);
			
			// 6. poolableConnectionFactory에 생성한 커넥션 풀을 연결
			poolableConnectionFactory.setPool(connectionPool);

			// 7. 커넥션 풀을 제공하는 JDBC 드라이버 등록 (풀링 드라이버 로드)
			Class.forName(prop.getProperty("poolingDriver"));
			
			// 8. 커넥션 풀 드라이버에 생성한 커넥션 풀을 등록 (생성한 커넥션 풀을 커넥션 풀 드라이버에 등록)
			PoolingDriver driver = (PoolingDriver)DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool(prop.getProperty("poolName"), connectionPool);

		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	// JDBC 드라이버를 로드하는 메소드를 정의
	private void loadJDBCDriver(Properties prop) {
		
		try {
			Class.forName(prop.getProperty("jdbcDriver"));	// 프로퍼티즈 파일에서 설정한 드라이버 주소(prop.getProperty("jdbcDriver"))로 클래스 로드를 하는 것
		}catch(ClassNotFoundException e) {
			throw new RuntimeException("Fail to load JDBC Driver", e);
		}
		
	}

}
