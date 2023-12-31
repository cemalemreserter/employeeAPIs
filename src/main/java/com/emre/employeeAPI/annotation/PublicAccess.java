package com.emre.employeeAPI.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicAccess {

    /*
    * Restful methods without this annotation can be used without the authorization token..
    * For other methods you have to send an authorization token in the header..
    * */

}
