package xwx.dto;

import enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * the message which the server send to the client
 * @auther 薛文轩
 * @data 2022/8/18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 222222141950251207L;

    private String requestId;

    private Integer code;

    private String message;

    private Object data;


    public static RpcResponse success(Object data,String requestId){
        return new RpcResponse(requestId,
                RpcResponseCodeEnum.SUCCESS.getCode(),
                RpcResponseCodeEnum.SUCCESS.getMessage(),
                data);
    }

    public static RpcResponse fail(Object data,String requestId){
        return new RpcResponse(requestId,
                RpcResponseCodeEnum.FAIL.getCode(),
                RpcResponseCodeEnum.FAIL.getMessage(),
                data);
    }

}
