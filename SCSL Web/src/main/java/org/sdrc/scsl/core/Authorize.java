package org.sdrc.scsl.core;
/**
 * @author Sarita(sarita@sdrc.co.in)
 * This is an annotation used to authorize features and permission across user roles.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
	
	public String feature() default "default" ;
	public String permission() default "default" ;

}
