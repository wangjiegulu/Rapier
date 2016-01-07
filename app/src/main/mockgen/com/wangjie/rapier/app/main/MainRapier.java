package com.wangjie.rapier.app.main;

import com.wangjie.rapier.app.main.module.MainModule;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/5/16.
 */
public class MainRapier {
    public void inject(MainModule module, MainActivity injectObj) {
        if (null == module) {
            throw new NullPointerException("Module of " + injectObj + " can not be null!");
        }
        injectObj.presenter = module.pickPresenter();
    }

    public static MainRapier create() {
        return new MainRapier();
    }

}
