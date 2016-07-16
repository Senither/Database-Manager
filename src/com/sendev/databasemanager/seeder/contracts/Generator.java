package com.sendev.databasemanager.seeder.contracts;

import com.sendev.databasemanager.seeder.services.RandomService;

public abstract class Generator
{
    protected final ResolverContract resolver;
    protected final FakeValuesServiceContract fakeValueService;

    public Generator(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        this.resolver = resolver;
        this.fakeValueService = fakeValueService;
    }

    protected String resolve(String key)
    {
        return fakeValueService.resolve(key, this, resolver);
    }

    protected RandomService random()
    {
        return fakeValueService.getRandomService();
    }

    protected FakeValuesServiceContract service()
    {
        return fakeValueService;
    }
}
