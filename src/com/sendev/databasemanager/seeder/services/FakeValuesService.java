package com.sendev.databasemanager.seeder.services;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;

import com.mifmif.common.regex.Generex;
import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;
import com.sendev.databasemanager.seeder.exceptions.LocaleDoesNotExistException;

public class FakeValuesService implements FakeValuesServiceContract
{
    private static final char[] METHOD_NAME_DELIMITERS = {'_'};
    private final Map<String, Object> fakeValues;
    private final RandomService randomService;

    public FakeValuesService(Locale locale, RandomService randomService)
    {
        this.randomService = randomService;

        String language = locale.getLanguage();

        InputStream stream = findStream(language);
        if (stream == null) {
            language = language + "-" + locale.getCountry();
            stream = findStream(language);
        }

        if (stream == null) {
            throw new LocaleDoesNotExistException(String.format("%s could not be found, does not have a corresponding yaml file", locale));
        }

        Map valuesMap = (Map) new Yaml().load(stream);
        valuesMap = (Map) valuesMap.get(language);

        fakeValues = (Map<String, Object>) valuesMap.get("faker");
    }

    private InputStream findStream(String filename)
    {
        String filenameWithExtension = "/resources/faker/" + filename + ".yml";
        InputStream streamOnClass = getClass().getResourceAsStream(filenameWithExtension);

        if (streamOnClass != null) {
            return streamOnClass;
        }

        return getClass().getClassLoader().getResourceAsStream(filenameWithExtension);
    }

    public Object fetch(String key)
    {
        List valuesArray = (List) fetchObject(key);

        return valuesArray.get(nextInt(valuesArray.size()));
    }

    public String fetchString(String key)
    {
        return (String) fetch(key);
    }

    public String safeFetch(String key)
    {
        Object o = fetchObject(key);

        if (o == null) {
            return "";
        }

        if (o instanceof List) {
            List<String> values = (List<String>) o;

            return values.get(randomService.nextInt(values.size()));
        }

        return (String) o;
    }

    public Object fetchObject(String key)
    {
        String[] path = key.split("\\.");

        Object currentValue = fakeValues;

        for (String pathSection : path) {
            currentValue = ((Map<String, Object>) currentValue).get(pathSection);
        }

        return currentValue;
    }

    public String numerify(String numberString)
    {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < numberString.length(); i++) {
            if (numberString.charAt(i) == '#') {
                sb.append(nextInt(10));

                continue;
            }

            sb.append(numberString.charAt(i));
        }

        return sb.toString();
    }

    public String bothify(String string)
    {
        return letterify(numerify(string));
    }

    public String letterify(String letterString)
    {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < letterString.length(); i++) {
            if (letterString.charAt(i) == '?') {
                sb.append((char) (97 + nextInt(26))); // a-z

                continue;
            }

            sb.append(letterString.charAt(i));
        }

        return sb.toString();
    }

    public String regexify(String regex)
    {
        Generex generex = new Generex(regex);

        generex.setSeed(randomService.nextLong());

        return generex.random();
    }

    private int nextInt(int n)
    {
        return randomService.nextInt(n);
    }

    public String resolve(String key, Object current, ResolverContract resolver)
    {
        String unresolvedString = safeFetch(key);
        String regex = "#\\{[A-Za-z_.]+\\}";

        Matcher matcher = Pattern.compile(regex).matcher(unresolvedString);

        while (matcher.find()) {
            String matched = matcher.group();
            String strippedMatched = matched.replace('#', ' ').replace('{', ' ').replace('}', ' ').trim();

            boolean isFirstLetterCapital = Character.isUpperCase(strippedMatched.charAt(0));

            String objectWithMethodToResolve = isFirstLetterCapital ? strippedMatched : current.getClass().getSimpleName() + "." + strippedMatched;
            String resolvedValue = resolver.resolve(objectWithMethodToResolve);

            unresolvedString = unresolvedString.replace(matched, resolvedValue);
        }

        return unresolvedString;
    }

    public RandomService getRandomService()
    {
        return randomService;
    }
}
