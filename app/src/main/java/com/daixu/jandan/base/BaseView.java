package com.daixu.jandan.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseView<T> {

    boolean isActive();

    <T> LifecycleTransformer<T> bindToLife();
}
