package com.sendev.databasemanager.seeder;

import java.math.BigDecimal;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;
import com.sendev.databasemanager.seeder.contracts.Generator;

public class Number extends Generator
{
    public Number(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    /**
     * Returns a random number between 0 and 9
     */
    public int randomDigit()
    {
        return fakeValueService.getRandomService().nextInt(9);
    }

    /**
     * Returns a random number between 1 and 9
     */
    public int randomDigitNotZero()
    {
        return randomDigit() + 1;
    }

    public long numberBetween(int min, long max)
    {
        return numberBetween((long) min, max);
    }

    public int numberBetween(int min, int max)
    {
        return fakeValueService.getRandomService().nextInt(max - min) + min;
    }

    public long numberBetween(long min, long max)
    {
        return fakeValueService.getRandomService().nextLong(max - min) + min;
    }

    /**
     *
     * @param numberOfDigits the number of digits the generated value should have
     * @param strict         whether or not the generated value should have exactly <code>numberOfDigits</code>
     */
    public long randomNumber(int numberOfDigits, boolean strict)
    {
        long max = (long) Math.pow(10, numberOfDigits);
        if (strict) {
            long min = (long) Math.pow(10, numberOfDigits - 1);
            return fakeValueService.getRandomService().nextLong(max - min) + min;
        }

        return fakeValueService.getRandomService().nextLong(max);
    }

    /**
     * Returns a ranbom number
     */
    public long randomNumber()
    {
        int numberOfDigits = fakeValueService.getRandomService().nextInt(8) + 1;

        return randomNumber(numberOfDigits, false);
    }

    /**
     * Returns a random double
     *
     * @param maxNumberOfDecimals maximum number of places
     * @param min                 minimum value
     * @param max                 maximum value
     */
    public double randomDouble(int maxNumberOfDecimals, int min, int max)
    {
        double value = min + (max - min) * fakeValueService.getRandomService().nextDouble();

        return new BigDecimal(value).setScale(maxNumberOfDecimals, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
}
