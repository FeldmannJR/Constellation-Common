package dev.feldmann.constellation.common.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {

    /**
     * Allow search for service in another providers instead of searching only in the owner provider
     **/
    boolean external() default false;
    boolean required() default true;
}
