package com.echoesnet.crowdfunding.net.subscribers;

/**
 * Created by liukun on 16/3/10.
 */
public interface InterceptorOnErrorListener<T> {
    /**
     * <b color="#ff0000">T 完全打断，F 继续执行</b>
     */
    boolean onError(T t);
}
