package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Team extends Generator
{
    public Team(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return service().resolve("team.name", this, resolver);
    }

    public String creature()
    {
        return service().fetchString("team.creature");
    }

    public String state()
    {
        return service().fetchString("address.state");
    }

    public String sport()
    {
        return service().fetchString("team.sport");
    }
}
