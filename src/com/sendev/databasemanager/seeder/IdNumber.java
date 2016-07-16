package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class IdNumber extends Generator
{
    private static final String[] invalidSSNPatterns = {
        "0{3}-\\\\d{2}-\\\\d{4}",
        "\\d{3}-0{2}-\\d{4}",
        "\\d{3}-\\d{2}-0{4}",
        "666-\\d{2}-\\d{4}",
        "9\\d{2}-\\d{2}-\\d{4}"};

    public IdNumber(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String valid()
    {
        return service().resolve("id_number.valid", this, resolver);
    }

    public String invalid()
    {
        return service().numerify(service().fetchString("id_number.invalid"));
    }

    public String ssnValid()
    {
        String ssn = service().regexify("[0-8]\\d{2}-\\d{2}-\\d{4}");

        boolean isValid = true;

        for (int i = 0; i < invalidSSNPatterns.length; i++) {
            if (ssn.matches(invalidSSNPatterns[i])) {
                isValid = false;
                break;
            }
        }

        if (!isValid) {
            ssn = ssnValid();
        }

        return ssn;
    }
}
