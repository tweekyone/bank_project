package com.epam.bank.operatorinterface.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = AdminMockSecurityContextFactory.class)
public @interface WithMockAdmin {
    String role() default "ADMIN";

    String email() default "admin@admin.com";

    String password() default "123456";

    boolean isEnabled() default true;

    @AliasFor(annotation = WithSecurityContext.class)
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}
