package sg.nus.iss.com.Leaveapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 3709531800279000172L;

    private Integer code;

    private String msg;

    private T data;


//    public static <T> ResultBuilder<T> builder(){
//    	return new ResultBuilder<T>();
//    }

	public static <Void> Result<Void> success() {
        return Result.<Void>builder()
                .code(200)
                .msg("success！")
                .data(null)
                .build();
    }


    public static Result<Void> success(String msg) {
        return Result.<Void>builder()
                .code(200)
                .msg(msg)
                .data(null)
                .build();
    }



	public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(200)
                .msg("success！")
                .data(data)
                .build();
    }


    public static <T> Result<T> success(String msg, T data) {
        return Result.<T>builder()
                .code(200)
                .msg(msg)
                .data(data)
                .build();
    }


    public static <Void> Result<Void> error() {
        return Result.<Void>builder()
                .code(500)
                .msg("success！")
                .data(null)
                .build();
    }


    public static <Void> Result<Void> error(String msg) {
        return Result.<Void>builder()
                .code(500)
                .msg(msg)
                .data(null)
                .build();
    }


    public static <Void> Result<Void> error(Integer code, String msg) {
        return Result.<Void>builder()
                .code(code)
                .msg(msg)
                .data(null)
                .build();
    }


//	public Integer getCode() {
//		return code;
//	}
//
//
//	public void setCode(Integer code) {
//		this.code = code;
//	}
//
//
//	public String getMsg() {
//		return msg;
//	}
//
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
//
//	public T getData() {
//		return data;
//	}
//
//
//	public void setData(T data) {
//		this.data = data;
//	}
    
//	static class ResultBuilder<S> {
//		
//		private Result<S> result;
//		
//		ResultBuilder() {
//			this.result = new Result<S>();
//		}
//		
//		public ResultBuilder<S> code(int code) {
//			result.setCode(code);
//			return this;
//		}
//		
//		public ResultBuilder<S> msg(String msg) {
//			result.setMsg(msg);
//			return this;
//		}
//		
//		public ResultBuilder<S> data(S data) {
//			result.setData(data);
//			return this;
//		}
//		
//		public Result build() {
//			return result;
//		}
//	}     
}
