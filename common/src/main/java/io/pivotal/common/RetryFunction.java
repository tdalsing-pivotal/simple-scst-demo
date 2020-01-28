package io.pivotal.common;

@FunctionalInterface
public interface RetryFunction<T, R> {

    R apply(T t) throws Exception;
}
