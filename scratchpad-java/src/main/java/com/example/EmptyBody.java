package com.example;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Make a body-less request. */
@Target(METHOD)
@Retention(RUNTIME)
public @interface EmptyBody { }
