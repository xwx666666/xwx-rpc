package dto;

import lombok.*;

/**
 *
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
    /**
     * message type,such as request message,response message,heartbeat message
     */
    private byte messageType;

    /**
     * serialization type
     */
    private byte codec;

    /**
     * compress type
     */
    private byte compress;


    /**
     * mark one request to facilitate the processing of the server
     */
    private byte requestId;

    /**
     * useful data,such the content of one request
     */
    private Object data;



}
