package io.github.shared.contracts;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.lang.annotation.Annotation;

public interface InstanceNameAware {

    /**
     * Extracts implementors' instance name as a {@link String}.
     * <p>
     * When used with {@code Spring beans} which are declared through {@link Component} inheritors - it tries to
     * extract the name specified within {@link Component#value()} or its inheritors.
     * <p>
     * In case if used outside {@code Spring} environment or if the {@link Component#value()} is not specified - it
     * tries to extract instances' name as if {@link Introspector#decapitalize(String)} is called on instances' {@link Class#getSimpleName()}.
     *
     * @return a Spring-like {@link String} representation of an instance name irrespective whether it is a {@code Spring} bean or an ordinary Java class.
     */
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
