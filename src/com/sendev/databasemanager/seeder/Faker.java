package com.sendev.databasemanager.seeder;

import java.lang.reflect.Proxy;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;
import com.sendev.databasemanager.seeder.services.DefaultingFakeValuesService;
import com.sendev.databasemanager.seeder.services.FakeValuesService;
import com.sendev.databasemanager.seeder.services.RandomService;

public class Faker implements ResolverContract
{
    private final RandomService randomService;
    private final FakeValuesService fakeValuesService;

    private final Address address;
    private final Name name;
    private final Number number;
    private final Finance finance;
    private final Company company;
    private final University university;
    private final Lorem lorem;
    private final Internet internet;
    private final Phone phone;
    private final Business business;
    private final Color color;
    private final Commerce commerce;
    private final Team team;
    private final App app;
    private final IdNumber idnumber;
    private final Superhero superhero;
    private final ChuckNorris chuckNorris;

    private final Crypto crypto;

    public Faker()
    {
        this(Locale.ENGLISH);
    }

    public Faker(Locale locale)
    {
        this(locale, null);
    }

    public Faker(Random random)
    {
        this(Locale.ENGLISH, random);
    }

    public Faker(Locale locale, Random random)
    {
        this.randomService = new RandomService(random);
        this.fakeValuesService = new FakeValuesService(locale, randomService);

        FakeValuesService defaultEnglishFakeValuesService = new FakeValuesService(Locale.ENGLISH, randomService);
        FakeValuesServiceContract proxiedFakeValueService = createProxiedFakeValuesService(fakeValuesService,
        defaultEnglishFakeValuesService);

        this.name = new Name(this, proxiedFakeValueService);
        this.number = new Number(this, proxiedFakeValueService);
        this.finance = new Finance(this, proxiedFakeValueService);
        this.company = new Company(this, proxiedFakeValueService);
        this.university = new University(this, proxiedFakeValueService);
        this.lorem = new Lorem(this, proxiedFakeValueService);
        this.phone = new Phone(this, proxiedFakeValueService);
        this.business = new Business(this, proxiedFakeValueService);
        this.color = new Color(this, proxiedFakeValueService);
        this.commerce = new Commerce(this, proxiedFakeValueService);
        this.team = new Team(this, proxiedFakeValueService);
        this.app = new App(this, proxiedFakeValueService);
        this.idnumber = new IdNumber(this, proxiedFakeValueService);
        this.superhero = new Superhero(this, proxiedFakeValueService);
        this.chuckNorris = new ChuckNorris(this, proxiedFakeValueService);

        this.address = new Address(this, name, proxiedFakeValueService);
        this.internet = new Internet(this, name, lorem, proxiedFakeValueService);

        this.crypto = new Crypto(null, lorem, null);
    }

    private static FakeValuesServiceContract createProxiedFakeValuesService(FakeValuesServiceContract service, FakeValuesServiceContract defaultService)
    {
        return (FakeValuesServiceContract) Proxy.newProxyInstance(Faker.class.getClassLoader(),
        new Class[]{FakeValuesServiceContract.class},
        new DefaultingFakeValuesService(service, defaultService));
    }

    public Address address()
    {
        return address;
    }

    public Company company()
    {
        return company;
    }

    public Name name()
    {
        return name;
    }

    public Number number()
    {
        return number;
    }

    public Finance finance()
    {
        return finance;
    }

    public University university()
    {
        return university;
    }

    public Lorem lorem()
    {
        return lorem;
    }

    public Internet internet()
    {
        return internet;
    }

    public Phone phone()
    {
        return phone;
    }

    public Business business()
    {
        return business;
    }

    public Color color()
    {
        return color;
    }

    public Commerce commerce()
    {
        return commerce;
    }

    public Team team()
    {
        return team;
    }

    public App app()
    {
        return app;
    }

    public IdNumber idNumber()
    {
        return idnumber;
    }

    public Superhero superhero()
    {
        return superhero;
    }

    public ChuckNorris chuckNorris()
    {
        return chuckNorris;
    }

    public Crypto crypto()
    {
        return crypto;
    }

    @Override
    public String resolve(String key)
    {
        String[] keySplit = key.split("\\.", 2);

        String object = WordUtils.uncapitalize(keySplit[0]);
        String methodName = keySplit[1];

        char[] METHOD_NAME_REPLACEMENT = {'_'};

        methodName = WordUtils.capitalizeFully(methodName, METHOD_NAME_REPLACEMENT).replaceAll("_", "");
        methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

        try {
            Object objectWithMethodToInvoke = MethodUtils.invokeMethod(this, object, null);
            return (String) MethodUtils.invokeMethod(objectWithMethodToInvoke, methodName, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
