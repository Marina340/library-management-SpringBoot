package com.example.mendix.CustomAnnotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableAction {
    String action();
    String entity();
}