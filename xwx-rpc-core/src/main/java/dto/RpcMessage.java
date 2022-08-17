package dto;

import lombok.*;

/**
 * @auther 薛文轩
 * @data 2022/8/17 22:10
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcMessage {
    private byte messageType;

}
