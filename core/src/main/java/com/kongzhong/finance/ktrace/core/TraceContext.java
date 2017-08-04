package com.kongzhong.finance.ktrace.core;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IFT8 on 17/4/3.
 */
@Data
@NoArgsConstructor
class TraceContext {
    private String traceId;
    private String spanId = "1";
    private String parentSpanId;

    private long sendTime;
    private long recvTime;
}
