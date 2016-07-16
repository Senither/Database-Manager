package com.sendev.databasemanager.seeder;

import org.apache.commons.lang.StringUtils;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Name extends Generator
{
    public Name(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return service().resolve("name.name", this, resolver);
    }

    public String nameWithMiddle()
    {
        return service().resolve("name.name_with_middle", this, resolver);
    }

    public String fullName()
    {
        return name();
    }

    public String firstName()
    {
        return service().fetchString("name.first_name");
    }

    public String lastName()
    {
        return service().fetchString("name.last_name");
    }

    public String prefix()
    {
        return service().fetchString("name.prefix");
    }

    public String suffix()
    {
        return service().fetchString("name.suffix");
    }

    public String title()
    {
        return StringUtils.join(new String[]{service().fetchString("name.title.descriptor"),
            service().fetchString("name.title.level"), service().fetchString("name.title.job")}, " ");
    }

    public String username()
    {
        return StringUtils.join(new String[]{
            firstName().replaceAll("'", "").toLowerCase(), ".", lastName().replaceAll("'", "").toLowerCase()}
        );
    }
}
