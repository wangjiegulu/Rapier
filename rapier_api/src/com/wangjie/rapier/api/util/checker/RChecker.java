package com.wangjie.rapier.api.util.checker;

import com.wangjie.rapier.api.util.func.FuncR;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public class RChecker {
    public static void checker(FuncR<Boolean> checker, String errorMessage) throws RuntimeException{
        if(checker.call()){
            throw new RuntimeException(errorMessage);
        }
    }


    public static void checkerNotNull(final Object obj, String errorMessage){
        checker(new FuncR<Boolean>() {
            @Override
            public Boolean call() {
                return null == obj;
            }
        }, errorMessage);
    }

}
