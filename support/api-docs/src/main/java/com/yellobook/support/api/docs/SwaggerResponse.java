package com.yellobook.support.api.docs;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(SwaggerResponses.class)
public @interface SwaggerResponse {
    String description() default "";

    String responseCode() default "default";

    Header[] headers() default {};

    Link[] links() default {};

    Content[] content() default {};

    Extension[] extensions() default {};

    String ref() default "";

    boolean useReturnTypeSchema() default false;
}

