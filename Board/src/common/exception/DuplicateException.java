package common.exception;

// 중복 시 발생처리할 예외
public class DuplicateException extends RuntimeException {
 public DuplicateException(String message) {
	 super(message);
 }
}
