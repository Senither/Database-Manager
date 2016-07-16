package com.sendev.databasemanager.seeder;

import java.math.BigDecimal;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Number extends Generator
{
    public Number(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    /**
     * Returns a random number between 0 and 9
     *
     * @return a random number tween 0 and 9
     */
    public int randomDigit()
    {
        return random().nextInt(9);
    }

    /**
     * Returns a random number between 1 and 9
     *
     * @return a random number tween 1 and 9
     */
    public int randomDigitNotZero()
    {
        return random().nextInt(8) + 1;
    }

    public long numberBetween(int min, long max)
    {
        return numberBetween((long) min, max);
    }

    public int numberBetween(int min, int max)
    {
        return random().nextInt(max - min) + min;
    }

    public long numberBetween(long min, long max)
    {
        return random().nextLong(max - min) + min;
    }

    /**
     * @param numberOfDigits the number of digits the generated value should have
     * @param strict         whether or not the generated value should have exactly <code>numberOfDigits</code>
     *
     * @return a randomly generated number based of the provided rules.
     */
    public long randomNumber(int numberOfDigits, boolean strict)
    {
        long max = (long) Math.pow(10, numberOfDigits);

        if (strict) {
            long min = (long) Math.pow(10, numberOfDigits - 1);

            return random().nextLong(max - min) + min;
        }

        return random().nextLong(max);
    }

    /**
     * @return a random number
     */
    public long randomNumber()
    {
        int numberOfDigits = service().getRandomService().nextInt(8) + 1;

        return randomNumber(numberOfDigits, false);
    }

    /**
     * Returns a random double
     *
     * @param maxNumberOfDecimals maximum number of places
     * @param min                 minimum value
     * @param max                 maximum value
     *
     * @return a random double number based of the provided rules.
     */
    public double randomDouble(int maxNumberOfDecimals, int min, int max)
    {
        double value = min + (max - min) * service().getRandomService().nextDouble();

        return new BigDecimal(value).setScale(maxNumberOfDecimals, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
}
