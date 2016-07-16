package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;
import com.sendev.databasemanager.seeder.contracts.Generator;

import org.apache.commons.lang.StringUtils;

public class Name extends Generator
{
    public Name(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return fakeValueService.resolve("name.name", this, resolver);
    }

    public String nameWithMiddle()
    {
        return fakeValueService.resolve("name.name_with_middle", this, resolver);
    }

    public String fullName()
    {
        return name();
    }

    public String firstName()
    {
        return fakeValueService.fetchString("name.first_name");
    }

    public String lastName()
    {
        return fakeValueService.fetchString("name.last_name");
    }

    public String prefix()
    {
        return fakeValueService.fetchString("name.prefix");
    }

    public String suffix()
    {
        return fakeValueService.fetchString("name.suffix");
    }

    public String title()
    {
        return StringUtils.join(new String[]{fakeValueService.fetchString("name.title.descriptor"),
            fakeValueService.fetchString("name.title.level"), fakeValueService.fetchString("name.title.job")}, " ");
    }

    public String username()
    {
        return StringUtils.join(new String[]{
            firstName().replaceAll("'", "").toLowerCase(), ".", lastName().replaceAll("'", "").toLowerCase()}
        );
    }
}
