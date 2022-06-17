package com.gbicc.aicr.system.flowable.exception;

/**
 * 流程运行异常类
 * 
 * @author FC
 * @version v1.0 2019年7月9日
 */
public class FlowableRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FlowableRuntimeException() {
        super();
    }

    public FlowableRuntimeException(String message) {
        super(message);
    }

}
