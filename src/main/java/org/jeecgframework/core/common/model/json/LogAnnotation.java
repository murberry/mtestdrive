package org.jeecgframework.core.common.model.json;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pengjin
 * @version 2.1
 * @since 2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAnnotation {
	String operateModelNm();
	String operateFuncNm();
    String operateDescribe();
}