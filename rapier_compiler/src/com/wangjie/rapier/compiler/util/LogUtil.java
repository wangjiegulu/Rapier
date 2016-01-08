package com.wangjie.rapier.compiler.util;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public class LogUtil {
    public static StringBuilder transformStackTrace(StackTraceElement[] elements){
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement element : elements){
            sb.append(element.toString()).append("\r\n");
        }
        return sb;
    }
    public static String transformStackTrace(Throwable throwable){
        StringBuilder sb = new StringBuilder(throwable.getMessage()).append("\n");
        for(StackTraceElement element : throwable.getStackTrace()){
            sb.append(element.toString()).append("\r\n");
        }
        return sb.toString();
    }
}
