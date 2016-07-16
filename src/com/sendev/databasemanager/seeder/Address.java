package com.sendev.databasemanager.seeder;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Address extends Generator
{
    private final Name name;

    public Address(ResolverContract resolver, Name name, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);

        this.name = name;
    }

    public String streetName()
    {
        return resolve("address.street_name");
    }

    public String streetAddressNumber()
    {
        return String.valueOf(fakeValueService.getRandomService().nextInt(1000));
    }

    public String streetAddress()
    {
        return resolve("address.street_address");
    }

    public String streetAddress(boolean includeSecondary)
    {
        String streetAddress = resolve("address.street_address");
        if (includeSecondary) {
            streetAddress = streetAddress + " " + secondaryAddress();
        }
        return streetAddress;
    }

    public String secondaryAddress()
    {
        return fakeValueService.numerify(fakeValueService.fetchString("address.secondary_address"));
    }

    public String zipCode()
    {
        return fakeValueService.bothify(fakeValueService.fetchString("address.postcode"));
    }

    public String streetSuffix()
    {
        return fakeValueService.fetchString("address.street_suffix");
    }

    public String citySuffix()
    {
        return fakeValueService.safeFetch("address.city_suffix");
    }

    public String cityPrefix()
    {
        return fakeValueService.safeFetch("address.city_prefix");
    }

    public String city()
    {
        return resolve("address.city");
    }

    public String cityName()
    {
        return resolve("address.city_name");
    }

    public String state()
    {
        return fakeValueService.fetchString("address.state");
    }

    public String stateAbbr()
    {
        return fakeValueService.fetchString("address.state_abbr");
    }

    public String firstName()
    {
        return name.firstName();
    }

    public String lastName()
    {
        return name.lastName();
    }

    public String latitude()
    {
        return String.format("%.8g", (fakeValueService.getRandomService().nextDouble() * 180) - 90);
    }

    public String longitude()
    {
        return String.format("%.8g", (fakeValueService.getRandomService().nextDouble() * 360) - 180);
    }

    public String timeZone()
    {
        return fakeValueService.fetchString("address.time_zone");
    }

    public String country()
    {
        return fakeValueService.fetchString("address.country");
    }

    public String countryCode()
    {
        return fakeValueService.fetchString("address.country_code");
    }

    public String buildingNumber()
    {
        return fakeValueService.numerify(fakeValueService.fetchString("address.building_number"));
    }
}
