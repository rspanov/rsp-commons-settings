package org.rspanov.commons.settings.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Primary setting annotation. All storable fields must be annotated with this
 * one
 *
 * @author rspanov
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Setting {

    /**
     * Setting description
     *
     * @return
     */
    String description();

    /**
     * Define if field can be null in settings storage
     *
     * @return
     */
    boolean nullable() default true;

}
