package dto;

import enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 222222141950251207L;

    private int requestId;

    private Integer code;

    private String message;

    private Object data;


    public static RpcResponse success(Object data,int requestId){
        return new RpcResponse(requestId,
                RpcResponseCodeEnum.SUCCESS.getCode(),
                RpcResponseCodeEnum.SUCCESS.getMessage(),
                data);
    }

    public static RpcResponse fail(Object data,int requestId){
        return new RpcResponse(requestId,
                RpcResponseCodeEnum.FAIL.getCode(),
                RpcResponseCodeEnum.FAIL.getMessage(),
                data);
    }

}
