package io.github.shared.contracts;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.lang.annotation.Annotation;

public interface InstanceNameAware {

    default String getInstanceName() {

        Class<?> clazz = this.getClass();

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotation instanceof Component || AnnotationUtils.findAnnotation(annotationType, Component.class) != null) {
                Object value = AnnotationUtils.getValue(annotation);
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    return ((String) value).trim();
                }
            }
        }

        return Introspector.decapitalize(clazz.getSimpleName().trim());
    }
}
