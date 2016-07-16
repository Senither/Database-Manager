package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Business extends Generator
{
    public Business(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String creditCardNumber()
    {
        return service().fetchString("business.credit_card_numbers");
    }

    public String creditCardType()
    {
        return service().fetchString("business.credit_card_types");
    }

    public String creditCardExpiry()
    {
        return service().fetchString("business.credit_card_expiry_dates");
    }
}
