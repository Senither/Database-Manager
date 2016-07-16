package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Phone extends Generator
{
    public Phone(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String cellPhone()
    {
        return service().numerify(service().fetchString("cell_phone.formats"));
    }

    public String phoneNumber()
    {
        return service().numerify(service().fetchString("phone_number.formats"));
    }
}
