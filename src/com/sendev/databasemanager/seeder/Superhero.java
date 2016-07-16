package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Superhero extends Generator
{
    public Superhero(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return service().resolve("superhero.name", this, resolver);
    }

    public String prefix()
    {
        return service().fetchString("superhero.prefix");
    }

    public String suffix()
    {
        return service().fetchString("superhero.suffix");
    }

    public String power()
    {
        return service().fetchString("superhero.power");
    }

    public String descriptor()
    {
        return service().fetchString("superhero.descriptor");
    }
}
