package com.group1.taskmanagement.interfaces;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@PostAuthorize("@userService.hasUserRights(returnObject.userId)")
public @interface HasResourceRights {
}