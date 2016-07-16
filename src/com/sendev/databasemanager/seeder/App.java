package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class App extends Generator
{
    public App(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return service().fetchString("app.name");
    }

    public String version()
    {
        return service().numerify(service().fetchString("app.version"));
    }

    public String author()
    {
        return service().resolve("app.name", this, resolver);
    }
}
