package com.wangjie.rapier.compiler.base;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/6/16.
 */
public abstract class BaseAbstractProcessor extends AbstractProcessor {
    protected Elements elementUtils;
    protected Types typeUtils;
    protected Filer filer;

    private static SimpleDateFormat LOGGER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-DD HH:mm:sss");

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected void logger(String str) {
        String loggerDate = System.identityHashCode(this) + "...[" + LOGGER_DATE_FORMAT.format(new Date()) + "]";
        String log = loggerDate + ": " + str;
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, log);
        writeToDisk(log);
    }

    private void writeToDisk(String log) {
        try {
            File logFile = new File("/Users/wangjie/Desktop/za/test/processor_log.txt");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter fw = new FileWriter(logFile, true);
            fw.write(log + "\n\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
