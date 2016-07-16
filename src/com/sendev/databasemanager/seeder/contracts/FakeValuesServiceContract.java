package com.sendev.databasemanager.seeder.contracts;

import com.sendev.databasemanager.seeder.services.RandomService;

public interface FakeValuesServiceContract
{
    public Object fetch(String key);

    public String fetchString(String key);

    public String safeFetch(String key);

    public Object fetchObject(String key);

    public String numerify(String numberString);

    public String bothify(String string);

    public String letterify(String letterString);

    public String regexify(String regex);

    public String resolve(String key, Object current, ResolverContract resolver);

    public RandomService getRandomService();
}
