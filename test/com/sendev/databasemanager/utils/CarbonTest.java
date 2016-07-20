package com.sendev.databasemanager.utils;

import java.text.ParseException;

import org.junit.Test;

import com.sendev.databasemanager.utils.Carbon.Day;
import com.sendev.test.TestCase;

import static org.junit.Assert.assertEquals;

public class CarbonTest extends TestCase
{
    @Test
    public void testcreatingInstanceWithStaticcreateFromFormat() throws ParseException
    {
        Carbon instance = Carbon.createFromFormat("EEE, dd MMM yyyy HH:mm:ssXXX", "Sat, 27 Feb 2016 09:52:34+01:00");

        assertEquals(instance.getYear(), 2016);
        assertEquals(instance.getMonth(), 2);
        assertEquals(instance.getDay(), 27);
        assertEquals(instance.getHour(), 9);
        assertEquals(instance.getMinute(), 52);
        assertEquals(instance.getSecond(), 34);
        assertEquals(instance.toDateTimeString(), "2016-02-27 09:52:34");

        assertEquals(instance.getDayOfWeek(), Day.SATURDAY);
    }

    @Test
    public void testCreatingInstanceWithStaticCreate()
    {
        Carbon instance = Carbon.create(2016, 2, 27, 9, 52, 34);

        assertEquals(instance.getYear(), 2016);
        assertEquals(instance.getMonth(), 2);
        assertEquals(instance.getDay(), 27);
        assertEquals(instance.getHour(), 9);
        assertEquals(instance.getMinute(), 52);
        assertEquals(instance.getSecond(), 34);
        assertEquals(instance.toDateTimeString(), "2016-02-27 09:52:34");

        assertEquals(instance.getDayOfWeek(), Day.SATURDAY);
    }

    @Test
    public void testCreatingInstanceWithStaticCreateFromDate()
    {
        Carbon instance = Carbon.createFromDate(2016, 2, 27);

        assertEquals(instance.getYear(), 2016);
        assertEquals(instance.getMonth(), 2);
        assertEquals(instance.getDay(), 27);
        assertEquals(instance.toDateString(), "2016-02-27");
    }

    @Test
    public void testCreatingInstanceWithStaticCreateFromTime()
    {
        Carbon instance = Carbon.createFromTime(9, 52, 34);

        assertEquals(instance.getHour(), 9);
        assertEquals(instance.getMinute(), 52);
        assertEquals(instance.getSecond(), 34);
        assertEquals(instance.toTimeString(), "09:52:34");
    }

    @Test
    public void testCreatingInstanceWithStaticYesterday()
    {
        assertEquals(Carbon.yesterday().toString(), (new Carbon()).subDay().startOfDay().toString());
    }

    @Test
    public void testCreatingInstanceWithStaticNow()
    {
        assertEquals(Carbon.now().toString(), (new Carbon()).toString());
    }

    @Test
    public void testCreatingInstanceWithStaticToday()
    {
        assertEquals(Carbon.today().toString(), (new Carbon()).startOfDay().toString());
    }

    @Test
    public void testCreatingInstanceWithStaticTomorrow()
    {
        assertEquals(Carbon.tomorrow().toString(), (new Carbon()).addDay().startOfDay().toString());
    }

    @Test
    public void testSetTimestamp()
    {
        Carbon instance = Carbon.now().setTimestamp(1456563154);

        assertEquals(instance.toDateTimeString(), "2016-02-27 09:52:34");
    }

    @Test
    public void testGetTimestamp()
    {
        assertEquals(Carbon.create(2016, 2, 27, 9, 52, 34).getTimestamp(), 1456563154);
    }

    @Test
    public void testDiffForHumansComparisons()
    {
        Carbon future = Carbon.now().addHour().addSeconds(30);
        Carbon past = Carbon.now().subHour().subSeconds(30);

        assertEquals(future.diffForHumans(), "an hour from now");
        assertEquals(past.diffForHumans(), "an hour ago");
        assertEquals(future.diffForHumans(past), "2 hours after");
        assertEquals(past.diffForHumans(future), "2 hours before");
    }
}
