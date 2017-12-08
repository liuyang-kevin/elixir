package com.echoesnet.crowdfunding.net.subscribers;

/**
 * Created by liukun on 16/3/10.
 */
public interface SubscriberOnErrorListener<T> {
    void onError(T t);
}
