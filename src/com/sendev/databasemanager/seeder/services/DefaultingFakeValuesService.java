package com.sendev.databasemanager.seeder.services;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;

public class DefaultingFakeValuesService implements InvocationHandler
{

    private final FakeValuesServiceContract service;
    private final FakeValuesServiceContract defaultService;

    public DefaultingFakeValuesService(FakeValuesServiceContract service, FakeValuesServiceContract defaultService)
    {
        this.service = service;
        this.defaultService = defaultService;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        try {
            Object value = method.invoke(service, args);
            if (value == null || isEmptyString(value)) {
                return defaultValue(method, args);
            } else {
                return value;
            }
        } catch (Exception e) {
            return defaultValue(method, args);
        }
    }

    private static boolean isEmptyString(Object value)
    {
        return (value instanceof String) && ((String) value).isEmpty();
    }

    private Object defaultValue(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException
    {
        return method.invoke(defaultService, args);
    }
}
