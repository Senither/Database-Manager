package com.sendev.databasemanager.utils;

import com.sendev.databasemanager.exceptions.InvalidFormatException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public final class Carbon
{

    public enum Day
    {
        MONDAY("Monday", Calendar.MONDAY),
        TUESDAY("Tuesday", Calendar.TUESDAY),
        WEDNESDAY("Wednesday", Calendar.WEDNESDAY),
        THURSDAY("Thursday", Calendar.THURSDAY),
        FRIDAY("Friday", Calendar.FRIDAY),
        SATURDAY("Saturday", Calendar.SATURDAY),
        SUNDAY("Sunday", Calendar.SUNDAY);

        private final String name;
        private final int id;

        private Day(String name, int id)
        {
            this.name = name;
            this.id = id;
        }

        /**
         * Gets the day name.
         *
         * @return the day name
         */
        public String getName()
        {
            return name;
        }

        /**
         * Gets the day ID.
         *
         * @return the day id
         */
        public int getId()
        {
            return id;
        }

        /**
         * Gets the day before the current day.
         *
         * @return the day before the current day
         */
        public Day getYesterday()
        {
            int day = getId() - 1;

            return day <= 0 ? SATURDAY : fromId(day);
        }

        /**
         * Gets the day after the current day.
         *
         * @return the day after the current day
         */
        public Day getTomorrow()
        {
            int day = getId() + 1;

            return day > 7 ? SUNDAY : fromId(id);
        }

        /**
         * Gets the day from the id, if an invalid id was
         * given, <code>NULL</code> will returned instead.
         *
         * @param id the id to match with the day
         *
         * @return the day that match the provided id
         */
        public static Day fromId(int id)
        {
            for (Day day : values()) {
                if (day.getId() == id) {
                    return day;
                }
            }

            return null;
        }

    }

    private enum Time
    {

        YEARS_PER_CENTURY(100),
        YEARS_PER_DECADE(10),
        MONTHS_PER_YEAR(12),
        WEEKS_PER_YEAR(52),
        DAYS_PER_WEEK(7),
        HOURS_PER_DAY(24),
        MINUTES_PER_HOUR(60),
        SECONDS_PER_MINUTE(60);

        private final int time;

        private Time(int time)
        {
            this.time = time;
        }

        /**
         * Gets the time.
         *
         * @return the integer time value
         */
        public int getTime()
        {
            return time;
        }
    }

    public enum SupportedFormat
    {
        DATE_TIME("yyyy-MM-dd HH:mm:ss"),
        DATE("yyyy-MM-dd"),
        FORMATTED_DATE("MMMMM dd, yyyy"),
        TIME("HH:mm:ss"),
        TIME_OFFSET("HH:mm:ssXXX"),
        DAY_DATE_TIME("EEE, MMM dd, yyyy h:mm aaa"),
        COOKIE("EEEEEEEE, dd-MMM-yyyy HH:mm:ss z"),
        RFC_822("EEE, dd MMM yyyy HH:mm:ss Z"),
        RFC_850("EEEEEEEE, dd-MMM-yyyy HH:mm:ss z"),
        RFC_1036("EEE, dd MMM yyyy HH:mm:ssXXX"),
        RSS("EEE, dd MMM yyyy HH:mm:ss Z");

        private final String string;

        private SupportedFormat(String string)
        {
            this.string = string;
        }

        public String getFormat()
        {
            return string;
        }

        public SimpleDateFormat make()
        {
            return new SimpleDateFormat(string);
        }

        public Date parse(String time) throws ParseException
        {
            return make().parse(time);
        }

        @Override
        public String toString()
        {
            return string;
        }
    }

    private static final Day GLOBAL_WEEK_START_AT = Day.MONDAY;
    private static final Day GLOBAL_WEEK_END_AT = Day.SUNDAY;
    private static final List<Day> WEEKEND_DAYS = Arrays.asList(Day.SATURDAY, Day.SUNDAY);
    private static String toStringFormat = SupportedFormat.DATE_TIME.getFormat();

    private final Calendar time;
    private Day WEEK_START_AT;
    private Day WEEK_END_AT;

    private TimeZone timezone;

    /**
     * Attempts to create a new Carbon instance with the current date and time.
     *
     * @see java.text.SimpleDateFormat
     */
    public Carbon()
    {
        this.time = Calendar.getInstance();

        this.WEEK_START_AT = GLOBAL_WEEK_START_AT;
        this.WEEK_END_AT = GLOBAL_WEEK_END_AT;

        this.timezone = time.getTimeZone();

        this.time.setFirstDayOfWeek(WEEK_START_AT.getId());
        this.time.setTime(Calendar.getInstance().getTime());
    }

    /**
     * Attempts to create a new Carbon instance from the given time string,
     * the string must be a valid date to be parsed correctly.
     * <p>
     * The string will be parsed through the <code>SimpleDateFormat</code>'s parser.
     *
     * @see java.text.SimpleDateFormat
     *
     * @param time The date string to parse.
     *
     * @throws InvalidFormatException if the date string given doesn't match any of the supported formats
     */
    public Carbon(String time) throws InvalidFormatException
    {
        this.time = Calendar.getInstance();

        this.WEEK_START_AT = GLOBAL_WEEK_START_AT;
        this.WEEK_END_AT = GLOBAL_WEEK_END_AT;

        this.timezone = this.time.getTimeZone();

        this.time.setFirstDayOfWeek(WEEK_START_AT.getId());

        for (SupportedFormat supportedFormat : SupportedFormat.values()) {
            try {
                this.time.setTime(supportedFormat.parse(time));

                return;
            } catch (ParseException ex) {
            }
        }

        throw new InvalidFormatException("'%s' does not follow any of the supported time formats, failed to creae Carbon instance.", time);
    }

    /**
     * Attempts to create a new Carbon instance from the given time string,
     * the string must be a valid date to be parsed correctly.
     * <p>
     * The string will be parsed through the <code>SimpleDateFormat</code>'s parser.
     *
     * @see java.text.SimpleDateFormat
     *
     * @param time     The date string to parse.
     * @param timezone The timezone to base the time output off
     *
     * @throws InvalidFormatException if the date string given doesn't match any of the supported formats
     */
    public Carbon(String time, String timezone) throws InvalidFormatException
    {
        this.time = Calendar.getInstance();

        this.WEEK_START_AT = GLOBAL_WEEK_START_AT;
        this.WEEK_END_AT = GLOBAL_WEEK_END_AT;

        this.timezone = TimeZone.getTimeZone(timezone);

        this.time.setFirstDayOfWeek(WEEK_START_AT.getId());

        for (SupportedFormat supportedFormat : SupportedFormat.values()) {
            try {
                this.time.setTime(supportedFormat.parse(time));

                return;
            } catch (ParseException ex) {
            }
        }

        throw new InvalidFormatException("'%s' does not follow any of the supported time formats, failed to creae Carbon instance.", time);
    }

    /**
     * Creates a new copy of the provided carbon instance.
     *
     * @param instance the carbon instance to copy
     */
    public Carbon(Carbon instance)
    {
        this.time = Calendar.getInstance();

        this.WEEK_START_AT = instance.WEEK_START_AT;
        this.WEEK_END_AT = instance.WEEK_END_AT;

        this.timezone = instance.getTimezone();

        this.time.setFirstDayOfWeek(WEEK_START_AT.getId());

        this.time.setTime((Date) instance.getTime().getTime().clone());
    }

    /**
     * Creates an new carbon instance from an internal method call from a date.
     *
     * @param date The date to use to create the carbon instance
     */
    private Carbon(Date date)
    {
        this.time = Calendar.getInstance();

        this.WEEK_START_AT = GLOBAL_WEEK_START_AT;
        this.WEEK_END_AT = GLOBAL_WEEK_END_AT;

        this.timezone = time.getTimeZone();

        this.time.setFirstDayOfWeek(WEEK_START_AT.getId());

        this.time.setTime(date);
    }

    /**
     * Creates a new Carbon instance with the current date and time.
     *
     * @return a Carbon instance with the current date and time.
     */
    public static Carbon now()
    {
        return new Carbon();
    }

    /**
     * Creates a new Carbon instance with the current date and time.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the current date and time.
     */
    public static Carbon now(String timezone)
    {
        Carbon carbon = now().startOfDay();

        carbon.setTimezone(timezone);

        return carbon;
    }

    /**
     * Creates a new Carbon instance with the current date and time.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the current date and time.
     */
    public static Carbon now(TimeZone timezone)
    {
        Carbon carbon = now().startOfDay();

        carbon.setTimezone(timezone);

        return carbon;
    }

    /**
     * Create a new Carbon instance with the date of today at the start of the day.
     *
     * @return a Carbon instance with the date of today at the start of the day
     */
    public static Carbon today()
    {
        return now().startOfDay();
    }

    /**
     * Create a new Carbon instance with the date of today at the start of the day.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the date of today at the start of the day
     */
    public static Carbon today(String timezone)
    {
        return now().startOfDay().setTimezone(timezone);
    }

    /**
     * Create a new Carbon instance with the date of today at the start of the day.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the date of today at the start of the day
     */
    public static Carbon today(TimeZone timezone)
    {
        return now().startOfDay().setTimezone(timezone);
    }

    /**
     * Creates a new Carbon instance with the date and time set to tomorrow.
     *
     * @return a Carbon instance with the date and time set to tomorrows date.
     */
    public static Carbon tomorrow()
    {
        return now().addDay().startOfDay();
    }

    /**
     * Creates a new Carbon instance with the date and time set to tomorrow.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the date and time set to tomorrows date.
     */
    public static Carbon tomorrow(String timezone)
    {
        return now().addDay().startOfDay().setTimezone(timezone);
    }

    /**
     * Creates a new Carbon instance with the date and time set to tomorrow.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the date and time set to tomorrows date.
     */
    public static Carbon tomorrow(TimeZone timezone)
    {
        return now().addDay().startOfDay().setTimezone(timezone);
    }

    /**
     * Creates a new Carbon instance with the date and time set to yesterday.
     *
     * @return a Carbon instance with the date and time set to yesterdays date.
     */
    public static Carbon yesterday()
    {
        return now().subDay().startOfDay();
    }

    /**
     * Creates a new Carbon instance with the date and time set to yesterday.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the date and time set to yesterdays date.
     */
    public static Carbon yesterday(String timezone)
    {
        return now().subDay().startOfDay().setTimezone(timezone);
    }

    /**
     * Creates a new Carbon instance with the date and time set to yesterday.
     *
     * @param timezone The timezone to base the date output off
     *
     * @return a Carbon instance with the date and time set to yesterdays date.
     */
    public static Carbon yesterday(TimeZone timezone)
    {
        return now().subDay().startOfDay().setTimezone(timezone);
    }

    public static Carbon createFromDate(Object... params)
    {
        Carbon carbon = new Carbon();

        if (params.length >= 1 && params[0] != null && parseObj(params[0]) > 0) {
            carbon.setYear(parseObj(params[0]));
        }

        if (params.length >= 2 && params[1] != null && parseObj(params[1]) > 0) {
            carbon.setMonth(parseObj(params[1]));
        }

        if (params.length >= 3 && params[2] != null && parseObj(params[2]) > 0) {
            carbon.setDay(parseObj(params[2]));
        }

        if (params.length >= 4 && params[3] != null) {
            if (params[3] instanceof TimeZone) {
                carbon.setTimezone((TimeZone) params[3]);

                return carbon;
            }

            if (params[3] instanceof String) {
                carbon.setTimezone((String) params[3]);

                return carbon;
            }
        }

        return carbon;
    }

    public static Carbon createFromTime(Object... params)
    {
        Carbon carbon = new Carbon();

        if (params.length >= 1 && params[0] != null && parseObj(params[0]) >= 0) {
            carbon.setHour(parseObj(params[0]));
        }

        if (params.length >= 2 && params[1] != null && parseObj(params[1]) >= 0) {
            carbon.setMinute(parseObj(params[1]));
        }

        if (params.length >= 3 && params[2] != null && parseObj(params[2]) >= 0) {
            carbon.setSecond(parseObj(params[2]));
        }

        if (params.length >= 4 && params[3] != null) {
            if (params[3] instanceof TimeZone) {
                carbon.setTimezone((TimeZone) params[3]);

                return carbon;
            }

            if (params[3] instanceof String) {
                carbon.setTimezone((String) params[3]);

                return carbon;
            }
        }

        return carbon;
    }

    public static Carbon create(Object... params)
    {
        Carbon carbon = new Carbon();

        if (params.length >= 1 && params[0] != null && parseObj(params[0]) > 0) {
            carbon.setYear(parseObj(params[0]));
        }

        if (params.length >= 2 && params[1] != null && parseObj(params[1]) > 0) {
            carbon.setMonth(parseObj(params[1]));
        }

        if (params.length >= 3 && params[2] != null && parseObj(params[2]) > 0) {
            carbon.setDay(parseObj(params[2]));
        }

        if (params.length >= 4 && params[3] != null && parseObj(params[3]) >= 0) {
            carbon.setHour(parseObj(params[3]));
        }

        if (params.length >= 5 && params[4] != null && parseObj(params[4]) >= 0) {
            carbon.setMinute(parseObj(params[4]));
        }

        if (params.length >= 6 && params[5] != null && parseObj(params[5]) >= 0) {
            carbon.setSecond(parseObj(params[5]));
        }

        if (params.length >= 7 && params[6] != null) {
            if (params[6] instanceof TimeZone) {
                carbon.setTimezone((TimeZone) params[6]);

                return carbon;
            }

            if (params[6] instanceof String) {
                carbon.setTimezone((String) params[6]);

                return carbon;
            }
        }

        return carbon;
    }

    private static int parseObj(Object obj)
    {
        if (obj == null) {
            return 0;
        }

        try {
            return (int) obj;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public static Carbon createFromFormat(String format, String time) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date date = sdf.parse(time);

        return new Carbon(date);
    }

    public static Carbon createFromFormat(String format, String time, String timezone) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date date = sdf.parse(time);

        return new Carbon(date).setTimezone(timezone);
    }

    public static Carbon createFromFormat(String format, String time, TimeZone timezone) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date date = sdf.parse(time);

        return new Carbon(date).setTimezone(timezone);
    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////////// GETTERS AND SETTERS /////////////////////
    ///////////////////////////////////////////////////////////////////
    public Carbon set(int field, int value)
    {
        time.set(field, value);

        return this;
    }

    public int get(int field)
    {
        return time.get(field);
    }

    /**
     * Sets the second to the carbon instance.
     *
     * @param second the seconds to set
     *
     * @return the Carbon instance
     */
    public Carbon setSecond(int second)
    {
        return set(Calendar.SECOND, second);
    }

    /**
     * Gets the second from the carbon instance.
     *
     * @return the second of the carbon instance
     */
    public int getSecond()
    {
        return get(Calendar.SECOND);
    }

    /**
     * Sets the minute to the carbon instance.
     *
     * @param minute the minute to set
     *
     * @return the Carbon instance
     */
    public Carbon setMinute(int minute)
    {
        return set(Calendar.MINUTE, minute);
    }

    /**
     * Gets the minute from the carbon instance.
     *
     * @return the minute of the carbon instance
     */
    public int getMinute()
    {
        return get(Calendar.MINUTE);
    }

    /**
     * Sets the hour to the carbon instance.
     *
     * @param hour the hour to set
     *
     * @return the Carbon instance
     */
    public Carbon setHour(int hour)
    {
        return set(Calendar.HOUR_OF_DAY, hour);
    }

    /**
     * Gets the hour from the carbon instance.
     *
     * @return the hour of the carbon instance
     */
    public int getHour()
    {
        return get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Sets the day of the month to the carbon instance.
     *
     * @param day the day to set
     *
     * @return the Carbon instance
     */
    public Carbon setDay(int day)
    {
        return set(Calendar.DAY_OF_MONTH, day);
    }

    /**
     * Gets the day of the month from the carbon instance.
     *
     * @return the day of the carbon instance
     */
    public int getDay()
    {
        return get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Sets the day of the week, this is a calendar-specific value.
     * The days count from 1 to 7, where 1 is a {@link Day#SUNDAY}
     *
     * @param day The day to set
     *
     * @return the Carbon instance
     */
    public Carbon setDayOfWeek(int day)
    {
        return set(Calendar.DAY_OF_WEEK, day);
    }

    /**
     * Sets the day of the week, this is a calendar-specific value.
     * The days count from 1 to 7, where 1 is a {@link Day#SUNDAY}
     *
     * @param day The day to set
     *
     * @return the Carbon instance
     */
    public Carbon setDayOfWeek(Day day)
    {
        return set(Calendar.DAY_OF_WEEK, day.getId());
    }

    /**
     * Gets the day of the week.
     *
     * @see Day
     *
     * @return
     */
    public Day getDayOfWeek()
    {
        return Day.fromId(get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Sets the week to the carbon instance.
     * <p>
     * This is a calendar-specific value. The week starts on a <code>MONDAY</code> and ends on a <code>SUNDAY</code>
     *
     * @see #WEEK_START_AT
     * @see #WEEK_END_AT
     *
     * @param week the week to set
     *
     * @return the Carbon instance
     */
    public Carbon setWeek(int week)
    {
        return set(Calendar.WEEK_OF_MONTH, week);
    }

    /**
     * Gets the week from the carbon instance.
     *
     * @return the week of the carbon instance
     */
    public int getWeek()
    {
        return get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Sets the month to the carbon instance.
     * <p>
     * This is a calendar-specific value. The first month of the year in the
     * Gregorian and Julian calendars is <code>JANUARY</code> which is 0;
     * the last depends on the number of months in a year.
     *
     * @see java.util.Calendar#JANUARY
     * @see java.util.Calendar#FEBRUARY
     * @see java.util.Calendar#MARCH
     * @see java.util.Calendar#APRIL
     * @see java.util.Calendar#MAY
     * @see java.util.Calendar#JUNE
     * @see java.util.Calendar#JULY
     * @see java.util.Calendar#AUGUST
     * @see java.util.Calendar#SEPTEMBER
     * @see java.util.Calendar#OCTOBER
     * @see java.util.Calendar#NOVEMBER
     * @see java.util.Calendar#DECEMBER
     * @see java.util.Calendar#UNDECIMBER
     *
     * @param month the month to set
     *
     * @return the Carbon instance
     */
    public Carbon setMonth(int month)
    {
        return set(Calendar.MONTH, month - 1);
    }

    /**
     * Gets the month from the carbon instance.
     *
     * @return the month of the carbon instance
     */
    public int getMonth()
    {
        return get(Calendar.MONTH) + 1;
    }

    /**
     * Sets the year to the carbon instance.
     *
     * @param year the year to set
     *
     * @return the Carbon instance
     */
    public Carbon setYear(int year)
    {
        return set(Calendar.YEAR, year);
    }

    /**
     * Gets the year from the carbon instance.
     *
     * @return the year of the carbon instance
     */
    public int getYear()
    {
        return get(Calendar.YEAR);
    }

    public int getDayOfYear()
    {
        return get(Calendar.DAY_OF_YEAR);
    }

    public int getWeekOfMonth()
    {
        return get(Calendar.WEEK_OF_MONTH);
    }

    public int getWeekOfYear()
    {
        return get(Calendar.WEEK_OF_YEAR);
    }

    public int getDaysInMonth()
    {
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public Carbon setTimestamp(long timestamp)
    {
        time.setTime(new Date(timestamp * 1000));

        return this;
    }

    public long getTimestamp()
    {
        return time.getTimeInMillis() / 1000;
    }

    public int getQuarter()
    {
        return (get(Calendar.MONTH) / 3) + 1;
    }

    public int getAge()
    {
        Carbon carbon = new Carbon();

        return getPositive(getYear() - (carbon.setTimezone(timezone)).getYear());
    }

    /**
     * Gets the calendar object used by carbon.
     *
     * @return the calendar object used by carbon
     */
    public Calendar getTime()
    {
        return time;
    }

    public Carbon setTimezone(TimeZone timezone)
    {
        if (timezone != null) {
            this.timezone = timezone;
        }

        return this;
    }

    public Carbon setTimezone(String timezone)
    {
        return setTimezone(TimeZone.getTimeZone(timezone));
    }

    public TimeZone getTimezone()
    {
        return timezone;
    }

    public Carbon setFirstDayOfWeek(Day day)
    {
        WEEK_START_AT = day;
        WEEK_END_AT = day.getYesterday();

        return this;
    }

    public Carbon setLastDayOfWeek(Day day)
    {
        WEEK_END_AT = day;
        WEEK_START_AT = day.getTomorrow();

        return this;
    }

    public Carbon setDate(int year, int month, int day)
    {
        return setYear(year).setMonth(month).setDay(day);
    }

    public Carbon setTime(int hour, int minute, int second)
    {
        return setHour(hour).setMinute(minute).setSecond(second);
    }

    public Carbon setDateTime(int year, int month, int day, int hour, int minute, int second)
    {
        return setDate(year, month, day).setTime(hour, minute, second);
    }

    public static void setToStringFormat(String format)
    {
        toStringFormat = format;
    }

    public static void resetToStringFormat()
    {
        toStringFormat = SupportedFormat.DATE_TIME.getFormat();
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// ADDITIONS AND SUBTRACTIONS ////////////////////
    ///////////////////////////////////////////////////////////////////
    public Carbon add(int field, int value)
    {
//        System.out.println("getPositive: " + getPositive(value) + ":" + value);
        time.add(field, getPositive(value));

        return this;
    }

    public Carbon sub(int field, int value)
    {
//        System.out.println("getNegative: " + getNegative(value) + ":" + value);
        time.add(field, getNegative(value));

        return this;
    }

    /**
     * Adds one second to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addSecond()
    {
        return addSeconds(1);
    }

    /**
     * Adds the given amount of seconds to the carbon instance.
     *
     * @param seconds the amount of seconds to add
     *
     * @return the Carbon instance
     */
    public Carbon addSeconds(int seconds)
    {
        return add(Calendar.SECOND, seconds);
    }

    /**
     * Subtracts one second from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subSecond()
    {
        return subSeconds(1);
    }

    /**
     * Subtracts the given amount of seconds from the carbon instance.
     *
     * @param seconds the amount of seconds to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subSeconds(int seconds)
    {
        return sub(Calendar.SECOND, seconds);
    }

    /**
     * Adds one minute to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addMinute()
    {
        return addMinutes(1);
    }

    /**
     * Adds the given amount of minutes to the carbon instance.
     *
     * @param minutes the amount of minutes to add
     *
     * @return the Carbon instance
     */
    public Carbon addMinutes(int minutes)
    {
        return add(Calendar.MINUTE, minutes);
    }

    /**
     * Subtracts one minute from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subMinute()
    {
        return subMinutes(1);
    }

    /**
     * Subtracts the given amount of minutes from the carbon instance.
     *
     * @param minutes the amount of minutes to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subMinutes(int minutes)
    {
        return sub(Calendar.MINUTE, minutes);
    }

    /**
     * Adds one hour to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addHour()
    {
        return addHours(1);
    }

    /**
     * Adds the given amount of hours to the carbon instance.
     *
     * @param hours the amount of hours to add
     *
     * @return the Carbon instance
     */
    public Carbon addHours(int hours)
    {
        return add(Calendar.HOUR, hours);
    }

    /**
     * Subtracts one hour from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subHour()
    {
        return subHours(1);
    }

    /**
     * Subtracts the given amount of hours from the carbon instance.
     *
     * @param hours the amount of hours to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subHours(int hours)
    {
        return sub(Calendar.HOUR, hours);
    }

    /**
     * Adds one day to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addDay()
    {
        return addDays(1);
    }

    /**
     * Adds the given amount of days to the carbon instance.
     *
     * @param days the amount of days to add
     *
     * @return the Carbon instance
     */
    public Carbon addDays(int days)
    {
        return add(Calendar.DAY_OF_MONTH, days);
    }

    /**
     * Subtracts one day from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subDay()
    {
        return subDays(1);
    }

    /**
     * Subtracts the given amount of days from the carbon instance.
     *
     * @param days the amount of days to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subDays(int days)
    {
        return sub(Calendar.DAY_OF_MONTH, days);
    }

    /**
     * Adds one week to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addWeek()
    {
        return addWeeks(1);
    }

    /**
     * Adds the given amount of weeks to the carbon instance.
     *
     * @param weeks the amount of weeks to add
     *
     * @return the Carbon instance
     */
    public Carbon addWeeks(int weeks)
    {
        return add(Calendar.WEEK_OF_MONTH, weeks);
    }

    /**
     * Subtracts one week from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subWeek()
    {
        return subWeeks(1);
    }

    /**
     * Subtracts the given amount of weeks from the carbon instance.
     *
     * @param weeks the amount of weeks to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subWeeks(int weeks)
    {
        return sub(Calendar.WEEK_OF_MONTH, weeks);
    }

    /**
     * Adds one month to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addMonth()
    {
        return addMonths(1);
    }

    /**
     * Adds the given amount of months to the carbon instance.
     *
     * @param months the amount of months to add
     *
     * @return the Carbon instance
     */
    public Carbon addMonths(int months)
    {
        return add(Calendar.MONTH, months);
    }

    /**
     * Subtracts one month from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subMonth()
    {
        return subMonths(1);
    }

    /**
     * Subtracts the given amount of months from the carbon instance.
     *
     * @param months the amount of months to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subMonths(int months)
    {
        return sub(Calendar.MONTH, months);
    }

    /**
     * Adds one year to the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon addYear()
    {
        return addYears(1);
    }

    /**
     * Adds the given amount of years to the carbon instance.
     *
     * @param years the amount of years to add
     *
     * @return the Carbon instance
     */
    public Carbon addYears(int years)
    {
        return add(Calendar.YEAR, years);
    }

    /**
     * Subtracts one year from the carbon instance.
     *
     * @return the Carbon instance
     */
    public Carbon subYear()
    {
        return subYears(1);
    }

    /**
     * Subtracts the given amount of years from the carbon instance.
     *
     * @param years the amount of years to subtract
     *
     * @return the Carbon instance
     */
    public Carbon subYears(int years)
    {
        return sub(Calendar.YEAR, years);
    }

    private int getPositive(int x)
    {
        if (x >= 0) {
            return x;
        }

        return x * -1;
    }

    private int getNegative(int x)
    {
        if (x < 0) {
            return x;
        }

        return x * -1;
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////////////// COMPARISON ////////////////////////////
    ///////////////////////////////////////////////////////////////////
    public boolean eq(Carbon value)
    {
        return getTimestamp() == value.getTimestamp();
    }

    public boolean ne(Carbon value)
    {
        return !eq(value);
    }

    public boolean gt(Carbon value)
    {
        return time.after(value.getTime());
    }

    public boolean gte(Carbon value)
    {
        return gt(value) || eq(value);
    }

    public boolean lt(Carbon value)
    {
        return time.before(value.getTime());
    }

    public boolean lte(Carbon value)
    {
        return lt(value) || eq(value);
    }

    public boolean between(Carbon first, Carbon second)
    {
        return between(first, second, true);
    }

    public boolean between(Carbon first, Carbon second, boolean matchEqual)
    {
        if (matchEqual && (eq(first) || eq(second))) {
            return true;
        }

        return (time.before(first.getTime()) && time.after(second.getTime())) || (time.before(second.getTime()) && time.after(first.getTime()));
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////////////// DIFFERENCES ///////////////////////////
    ///////////////////////////////////////////////////////////////////
    public boolean isYesterday()
    {
        Carbon carbon = new Carbon().subDay();

        return carbon.getYear() == getYear() && carbon.getMonth() == getMonth() && carbon.getDay() == getDay();
    }

    public boolean isToday()
    {
        Carbon carbon = new Carbon();

        return carbon.getYear() == getYear() && carbon.getMonth() == getMonth() && carbon.getDay() == getDay();
    }

    public boolean isTomorrow()
    {
        Carbon carbon = new Carbon().addDay();

        return carbon.getYear() == getYear() && carbon.getMonth() == getMonth() && carbon.getDay() == getDay();
    }

    /**
     * Checks to see if the Carbon instance is currently set in the past.
     *
     * @return true if the carbon date is set in the past.
     */
    public boolean isPast()
    {
        return Calendar.getInstance().getTimeInMillis() < time.getTimeInMillis();
    }

    /**
     * Checks to see if the Carbon instance is currently set in the future.
     *
     * @return true if the carbon date is set in the future.
     */
    public boolean isFuture()
    {
        return !isPast();
    }

    /**
     * Checks to see if the Carbon instance date is currently set to a weekday.
     *
     * @return true if the carbon date is set to a weekday
     */
    public boolean isWeekday()
    {
        return !isWeekend();
    }

    /**
     * Checks to see if the Carbon instance date is currently set to a weekend.
     *
     * @return true if the carbon date is set to a weekend
     */
    public boolean isWeekend()
    {
        return WEEKEND_DAYS.stream().anyMatch(( day ) -> (day.getId() == getDay()));
    }

    public boolean isLeapYear()
    {
        return time.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public boolean isSameDay(Carbon other)
    {
        return getYear() == other.getYear() && getMonth() == other.getMonth() && getDay() == other.getDay();
    }

    public boolean isBirthday(Carbon other)
    {
        return getMonth() == other.getMonth() && getDay() == other.getDay();
    }

    /**
     * Gets the difference between now and the carbon instance in seconds.
     *
     * @return the difference between now and the carbon instance in seconds
     */
    public long diff()
    {
        long current = System.currentTimeMillis();
        long unixTime = time.getTimeInMillis();

        long value = (current - unixTime) / 1000;

        return value >= 0 ? value : value * -1;
    }

    /**
     * Get the difference between now and the carbon instance time in a human readable string.
     *
     * @return the difference between now and the carbon instance in a human readable string
     */
    public String diffForHumans()
    {
        return diffForHumans(false);
    }

    /**
     * Get the difference between now and the carbon instance time in a human readable string.
     *
     * @param removeModifiers Determines if the modifiers "from now" and "ago" should be removed
     *
     * @return the difference between now and the carbon instance in a human readable string
     */
    public String diffForHumans(boolean removeModifiers)
    {
        long unix = diff();

        if (unix == 0) {
            return "now";
        }

        StringBuilder builder = parseDiffForHumans(unix);

        if (!removeModifiers) {
            if (isPast()) {
                builder.append(" from now");
            } else {
                builder.append(" ago");
            }
        }

        return builder.toString().trim();
    }

    /**
     * Get the difference between the provided Carbon instance and the carbon instance time in a human readable string.
     *
     * @param other The carbon instance to compare with
     *
     * @return the difference between the provided Carbon instance and the carbon instance in a human readable string
     */
    public String diffForHumans(Carbon other)
    {
        return diffForHumans(other, false);
    }

    /**
     * Get the difference between the provided Carbon instance and the carbon instance time in a human readable string.
     *
     * @param other           The carbon instance to compare with
     * @param removeModifiers Determines if the modifiers "from now" and "ago" should be removed
     *
     * @return the difference between the provided Carbon instance and the carbon instance in a human readable string
     */
    public String diffForHumans(Carbon other, boolean removeModifiers)
    {
        long value = getTimestamp() - other.getTimestamp();

        long unix = value >= 0 ? value : value * -1;

        if (unix == 0) {
            return "now";
        }

        StringBuilder builder = parseDiffForHumans(unix);

        if (!removeModifiers) {
            if (other.time.getTimeInMillis() < time.getTimeInMillis()) {
                builder.append(" after");
            } else {
                builder.append(" before");
            }
        }

        return builder.toString().trim();
    }

    private StringBuilder parseDiffForHumans(long unix)
    {
        StringBuilder sb = new StringBuilder();

        long sec = (unix >= 60 ? unix % 60 : unix);
        long min = (unix = (unix / 60)) >= 60 ? unix % 60 : unix;
        long hrs = (unix = (unix / 60)) >= 24 ? unix % 24 : unix;
        long days = (unix = (unix / 24)) >= 30 ? unix % 30 : unix;
        long months = (unix = (unix / 30)) >= 12 ? unix % 12 : unix;
        long years = (unix / 12);

//        System.out.println(String.format("parseDiffForHumans: [sec:%s, min:%s, hrs:%s, days:%s, months:%s, years:%s]", sec, min, hrs, days, months, years));
        if (years > 0) {
            if (years == 1) {
                sb.append("a year");
            } else {
                sb.append(years).append(" years");
            }

            if (years <= 6 && months > 0) {
                if (months == 1) {
                    sb.append(" and a month");
                } else {
                    sb.append(" and ").append(months).append(" months");
                }
            }
        } else if (months > 0) {
            if (months == 1) {
                sb.append("a month");
            } else {
                sb.append(months).append(" months");
            }

            if (months <= 6 && days > 0) {
                if (days == 1) {
                    sb.append(" and a day");
                } else {
                    sb.append(" and ").append(days).append(" days");
                }
            }
        } else if (days > 0) {
            if (days == 1) {
                sb.append("a day");
            } else {
                sb.append(days).append(" days");
            }

            if (days <= 3 && hrs > 0) {
                if (hrs == 1) {
                    sb.append(" and an hour");
                } else {
                    sb.append(" and ").append(hrs).append(" hours");
                }
            }
        } else if (hrs > 0) {
            if (hrs == 1) {
                sb.append("an hour");
            } else {
                sb.append(hrs).append(" hours");
            }

            if (min > 1) {
                sb.append(" and ").append(min).append(" minutes");
            }
        } else if (min > 0) {
            if (min == 1) {
                sb.append("a minute");
            } else {
                sb.append(min).append(" minutes");
            }

            if (sec > 1) {
                sb.append(" and ").append(sec).append(" seconds");
            }
        } else if (sec <= 1) {
            sb.append("about a second");
        } else {
            sb.append(sec).append(" seconds");
        }

        return sb;
    }

    ///////////////////////////////////////////////////////////////////
    //////////////////////////// MODIFIERS ////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /**
     * Sets the carbon time to the start of the day.
     *
     * @return the Carbon instance
     */
    public Carbon startOfDay()
    {
        return setHour(0).setMinute(0).setSecond(0);
    }

    /**
     * Sets the carbon time to the end of the day.
     *
     * @return the Carbon instance
     */
    public Carbon endOfDay()
    {
        return setHour(23).setMinute(59).setSecond(59);
    }

    /**
     * Sets the carbon time to the start of the week.
     *
     * @return the Carbon instance
     */
    public Carbon startOfWeek()
    {
        return setDayOfWeek(WEEK_START_AT.getId()).startOfDay();
    }

    /**
     * Sets the carbon time to the end of the week.
     *
     * @return the Carbon instance
     */
    public Carbon endOfWeek()
    {
        return setDay(WEEK_END_AT.getId()).endOfDay();
    }

    /**
     * Sets the carbon time to the start of the month.
     *
     * @return the Carbon instance
     */
    public Carbon startOfMonth()
    {
        return startOfDay().setDay(1);
    }

    /**
     * Sets the carbon time to the end of the month.
     *
     * @return the Carbon instance
     */
    public Carbon endOfMonth()
    {
        Calendar cal = new GregorianCalendar(getYear(), getMonth(), getDay());

        return setDay(cal.getActualMaximum(Calendar.DAY_OF_MONTH)).endOfDay();
    }

    /**
     * Sets the carbon time to the start of the year.
     *
     * @return the Carbon instance
     */
    public Carbon startOfYear()
    {
        return setMonth(1).startOfMonth();
    }

    /**
     * Sets the carbon time to the end of the year.
     *
     * @return the Carbon instance
     */
    public Carbon endOfYear()
    {
        return setMonth(Time.MONTHS_PER_YEAR.getTime()).endOfMonth();
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////////////// OUTPUT FORMAT //////////////////////////
    ///////////////////////////////////////////////////////////////////
    @Override
    public String toString()
    {
        return format(toStringFormat);
    }

    /**
     * Generates a date string, example:
     *
     * 1975-12-25
     *
     * @return The generated date string
     */
    public String toDateString()
    {
        return format(SupportedFormat.DATE);
    }

    /**
     * Generates a formatted date string, example:
     * <p>
     * Dec 25, 1975
     *
     * @return The generated formatted date string
     */
    public String toFormattedDateString()
    {
        return format(SupportedFormat.FORMATTED_DATE);
    }

    /**
     * Generates a time string, example:
     *
     * 14:15:16
     *
     * @return The generated time string
     */
    public String toTimeString()
    {
        return format(SupportedFormat.TIME);
    }

    /**
     * Generates a time offset time string, example:
     * <p>
     * 14:15:16-05:00
     *
     * @return The generated time offset time string
     */
    public String toTimeOffsetString()
    {
        return format(SupportedFormat.TIME_OFFSET);
    }

    /**
     * Generates a date time string, example:
     *
     * 1975-12-25 14:15:16
     *
     * @return The generated date time string
     */
    public String toDateTimeString()
    {
        return format(SupportedFormat.DATE_TIME);
    }

    /**
     * Generates a day date time string, example:
     * <p>
     * Thu, Dec 25, 1975 2:15 PM
     *
     * @return The generated day date time string
     */
    public String toDayDateTimeString()
    {
        return format(SupportedFormat.DAY_DATE_TIME);
    }

    /**
     * Generates an atomic time string, example:
     * <p>
     * 1975-12-25T14:15:16-05:00
     *
     * @return The generated atomic time string
     */
    public String toAtomicString()
    {
        return String.format("%sT%s", toDateString(), toTimeOffsetString());
    }

    /**
     * Generates a cookie time string, example:
     * <p>
     * Thursday, 25-Dec-1975 14:15:16 EST
     *
     * @return The generated cookie time string
     */
    public String toCookieString()
    {
        return format(SupportedFormat.COOKIE);
    }

    /**
     * Generates a ISO 8601 time string, example:
     * <p>
     * 1975-12-25T14:15:16-0500
     *
     * @return The generated ISO 8601 time string
     */
    public String toIso8601String()
    {
        return String.format("%sT%s", toDateString(), format("HH:mm:ssZ"));
    }

    /**
     * Generates a RFC 822 time string, example:
     * <p>
     * Thu, 25 Dec 1975 14:15:16 -0500
     *
     * @return The generated RFC 822 time string
     */
    public String toRfc822String()
    {
        return format(SupportedFormat.RFC_822);
    }

    /**
     * Generates a RFC 850 time string, example:
     * <p>
     * Thursday, 25-Dec-1975 14:15:16 EST
     *
     * @return The generated RFC 850 time string
     */
    public String toRfc850String()
    {
        return format(SupportedFormat.RFC_850);
    }

    /**
     * Generates a RFC 1036 time string, example:
     * <p>
     * 1975-12-25T14:15:16-05:00
     *
     * @return The generated RFC 1036 time string
     */
    public String toRfc1036String()
    {
        return format(SupportedFormat.RFC_1036);
    }

    /**
     * Generates a RFC 850 time string, example:
     * <p>
     * Thu, 25 Dec 1975 14:15:16 -0500
     *
     * @return The generated RFC 850 time string
     */
    public String toRfc3339String()
    {
        return String.format("%sT%s", toDateString(), format("HH:mm:ssZ"));
    }

    /**
     * Generates a RSS time string, example:
     * <p>
     * Thu, 25 Dec 1975 14:15:16 -0500
     *
     * @return The generated RSS time string
     */
    public String toRssString()
    {
        return format(SupportedFormat.RSS);
    }

    /**
     * Generates a W3C time string, example:
     * <p>
     * 1975-12-25T14:15:16-05:00
     *
     * @return The generated W3C time string
     */
    public String toW3cString()
    {
        return String.format("%sT%s", format("yyyy-mm-dd"), toTimeOffsetString());
    }

    /**
     * Formats the carbon instance and prints out the formatted time string.
     *
     * @param format the string to use to generate the time string
     *
     * @return the formatted datetime string
     */
    public String format(String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        if (timezone != null) {
            sdf.setTimeZone(timezone);
        }

        return sdf.format(time.getTime());
    }

    /**
     * Formats the carbon instance and prints out the formatted time string.
     *
     * @param format the string to use to generate the time string
     *
     * @return the formatted datetime string
     */
    public String format(SupportedFormat format)
    {
        return format(format.getFormat());
    }

    public Carbon copy()
    {
        return new Carbon(this);
    }
}
