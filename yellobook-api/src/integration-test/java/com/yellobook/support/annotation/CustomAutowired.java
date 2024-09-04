package com.yellobook.support.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Autowired
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public @interface CustomAutowired {
}