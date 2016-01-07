package com.wangjie.rapier.app.main.module;

import com.wangjie.rapier.app.main.IMainPresenter;
import com.wangjie.rapier.app.main.MainPresenter;
import com.wangjie.rapier.app.main.MainViewer;
import com.wangjie.rapier.app.model.FooData;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/6/16.
 */
public class MainModule{
    private MainViewer mainViewer;

    public MainModule(MainViewer mainViewer) {
        this.mainViewer = mainViewer;
    }

    public IMainPresenter pickPresenter(){
        return new MainPresenter(mainViewer);
    }

    public FooData pickFooData(){
        return new FooData(112358, "hello foo");
    }
}
