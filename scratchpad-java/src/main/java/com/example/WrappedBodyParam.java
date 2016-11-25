package com.example;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Wrap a parameter in the request */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface WrappedBodyParam {
  String value();
}
