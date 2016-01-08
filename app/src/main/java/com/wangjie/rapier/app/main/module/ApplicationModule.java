package com.wangjie.rapier.app.main.module;

import com.wangjie.rapier.app.RapierApplication;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public class ApplicationModule {

    public RapierApplication pickApplication(){
        return RapierApplication.instance;
    }
}
