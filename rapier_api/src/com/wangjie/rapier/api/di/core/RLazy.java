package com.wangjie.rapier.api.di.core;

import com.wangjie.rapier.api.util.func.FuncR;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public class RLazy<T> {
    private FuncR<T> creation;
    private T instance;

    private boolean isSync = true;

    public RLazy() {
        this(true);
    }

    public RLazy(boolean isSync) {
        this.isSync = isSync;
    }

//    public RLazy(final FuncR<T> creation) {
//        RChecker.checkerNotNull(creation, "creation can not be null!");
//        this.creation = creation;
//    }

    public void setCreation(FuncR<T> creation) {
        this.creation = creation;
    }

    public T get() {
        return isSync ? getInternalSync() : getInternal();
    }

    private T getInternalSync() {
        T result = instance;
        if (null == result) {
            synchronized (this) {
                result = instance;
                if (null == result) {
                    instance = result = creation.call();
                }
            }
        }
        return result;
    }

    private T getInternal() {
        T result = instance;
        if (null == result) {
            instance = result = creation.call();
        }
        return result;
    }

}
