package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class University extends Generator
{
    public University(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return service().resolve("university.name", this, resolver);
    }

    public String prefix()
    {
        return service().fetchString("university.prefix");
    }

    public String suffix()
    {
        return service().fetchString("university.suffix");
    }
}
