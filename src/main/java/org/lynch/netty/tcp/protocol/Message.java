package org.lynch.netty.tcp.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 协议包
 */
@Data
@AllArgsConstructor
public class Message {
    private long length;
    private byte[] content;

}
