package dk.erst.cm.xml.ubl21.model.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Field is mandatory in all models
 * 
 * If assigned on a list - at least one element should be present.
 */
@Retention(RUNTIME) @Target({FIELD, METHOD, PARAMETER})
public @interface Mandatory {

}
