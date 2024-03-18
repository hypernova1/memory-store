package org.sam.store.common.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyRepository<T, U> implements InvocationHandler {
    private final DefaultMemoryRepository<T, U> repository;

    public ProxyRepository() {
        this.repository = new DefaultMemoryRepository<T, U>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Object result = method.invoke(repository, args);
            return result;
        } catch (Exception e) {
            //TODO: 메서드 쿼리 구현
            System.out.println(method.getName());
        }
        return null;
    }
}
