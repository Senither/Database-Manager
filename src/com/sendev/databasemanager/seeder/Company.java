package com.sendev.databasemanager.seeder;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.StringUtils.join;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Company extends Generator
{
    public Company(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String name()
    {
        return service().resolve("company.name", this, resolver);
    }

    public String suffix()
    {
        return service().safeFetch("company.suffix");
    }

    public String industry()
    {
        return service().safeFetch("company.industry");
    }

    public String profession()
    {
        return service().safeFetch("company.profession");
    }

    public String buzzword()
    {
        @SuppressWarnings("unchecked")
        List<List<String>> buzzwordLists = (List<List<String>>) service().fetchObject("company.buzzwords");

        List<String> buzzwords = new ArrayList<String>();
        for (List<String> buzzwordList : buzzwordLists) {
            buzzwords.addAll(buzzwordList);
        }

        return buzzwords.get(random().nextInt(buzzwords.size()));
    }

    /**
     * Generate a buzzword-laden catch phrase.
     *
     * @return a buzzword-laden catch phrase.
     */
    public String catchPhrase()
    {
        @SuppressWarnings("unchecked")
        List<List<String>> catchPhraseLists = (List<List<String>>) service().fetchObject("company.buzzwords");

        return joinSampleOfEachList(catchPhraseLists, " ");
    }

    /**
     * When a straight answer won't do, BS to the rescue!
     *
     * @return some bullshit.
     */
    public String bs()
    {
        @SuppressWarnings("unchecked")
        List<List<String>> buzzwordLists = (List<List<String>>) service().fetchObject("company.bs");

        return joinSampleOfEachList(buzzwordLists, " ");
    }

    /**
     * Generate a random company logo url in PNG format.
     *
     * @return a random company logo url in PNG format.
     */
    public String logo()
    {
        int number = random().nextInt(13) + 1;

        return "https://pigment.github.io/fake-logos/logos/medium/color/" + number + ".png";
    }

    public String url()
    {
        return join(new Object[]{
            "www",
            ".",
            IDN.toASCII(domainName()),
            ".",
            domainSuffix()
        });
    }

    private String domainName()
    {
        return StringUtils.deleteWhitespace(name().toLowerCase().replaceAll(",", "").replaceAll("'", ""));
    }

    private String domainSuffix()
    {
        return service().fetchString("internet.domain_suffix");
    }

    private String joinSampleOfEachList(List<List<String>> listOfLists, String separator)
    {
        List<String> words = new ArrayList<String>();

        for (List<String> list : listOfLists) {
            words.add(list.get(random().nextInt(list.size())));
        }

        return StringUtils.join(words, separator);
    }
}
