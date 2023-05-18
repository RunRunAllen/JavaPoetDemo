package com.example.compiler;

import com.example.arouter_annotation.ARouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

//2.配置环境
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.example.arouter_annotation.ARouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 1.继承自 AbStractProcessor

//5.参数
@SupportedOptions("student")
public class JavaPoetProcessor extends AbstractProcessor {


    //3.需要对应的工具类

    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        //6.
        String student = processingEnv.getOptions().get("student");
        messager.printMessage(Diagnostic.Kind.NOTE, "=测试===" + student);

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);


//         package com.example.helloworld;
//
//        public final class HelloWorld {
//
//            public static void main(String[] args) {
//                System.out.println("Hello, JavaPoet!");
//            }
//        }


        for (Element element : elements) {
            // 获取方法
            MethodSpec methodSpec = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(String[].class, "args")
                    .returns(void.class)
                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                    .build();

            //获取类
            TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                    .addMethod(methodSpec)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();


            //获取包
            JavaFile file = JavaFile.builder("com.example.javapoetdemo", typeSpec).build();
            //写入文件
            try {
                file.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.NOTE, "失败了。。。。");
            }


        }


        return false;
    }


}