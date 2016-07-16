package com.sendev.databasemanager.seeder.services;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

public class RandomService
{
    private final Random random;

    public RandomService(Random random)
    {
        this.random = random != null ? random : new SecureRandom(ByteBuffer.allocate(4).putInt(12345).array());
    }

    public int nextInt()
    {
        return random.nextInt();
    }

    public int nextInt(int n)
    {
        return random.nextInt(n);
    }

    public long nextLong()
    {
        return random.nextLong();
    }

    public long nextLong(long n)
    {
        if (n <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }

        long bits, val;

        do {
            long randomLong = random.nextLong();
            bits = (randomLong << 1) >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);

        return val;
    }

    public double nextDouble()
    {
        return random.nextDouble();
    }

    public Boolean nextBoolean()
    {
        return random.nextBoolean();
    }
}
