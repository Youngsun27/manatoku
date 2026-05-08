package com.manatoku.serviceModel;

/* 서비스의 결과값을 보관하기 위한 객체 */
public class ServiceResult<T> { //제네릭 코드로 여러 모델에 대응할 수 있도록 설계

		private String code;
		private boolean success;
		private String message;
		private T data;
		
		private ServiceResult(boolean success, String message, T data) {
			this.success = success;
			this.message = message;
			this.data = data;
		}
		private ServiceResult(String code,boolean success, String message, T data) {
			this.code = code;
			this.success = success;
			this.message = message;
			this.data = data;
		}

		/* 로직 성공시 모델 리턴이 필요 없는 경우 */
		public static <T> ServiceResult<T> success() {
	        return new ServiceResult<>(true, null, null);
	    }
		/* 로직 성공시 모델 리턴이 필요한 경우 */
		public static <T> ServiceResult<T> success(T data) {
	        return new ServiceResult<>(true, null, data);
	    }
		/* 실패시 모델 리턴이 필요한 경우 */
	    public static <T> ServiceResult<T> fail(String message, T data) {  // 실패 메시지를 입력
	        return new ServiceResult<>(false, message, data);
	    }
		/* 실패시 모델 리턴이 필요없는 경우 */
	    public static <T> ServiceResult<T> fail(String message) { // 실패 메세지를 입력
	        return new ServiceResult<>(false, message, null);
	    }

		/* 에러코드를 입력해야 하는 경우 */
		/* 모델 리턴이 필요 없는 경우 */
		public static <T> ServiceResult<T> fail(String code,String message) { // 실패 메세지를 입력
		return new ServiceResult<>(code,false, message, null);
		}
		/* 모델 리턴이 필요한 경우 */
		public static <T> ServiceResult<T> fail(String code,String message,T data) { // 실패 메세지를 입력
			return new ServiceResult<>(code,false, message, data);
		}



	public boolean isSuccess() {
			return success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
}
