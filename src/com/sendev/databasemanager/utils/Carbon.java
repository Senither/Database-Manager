package com.sendev.databasemanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Carbon implements Cloneable
{

    private enum Day
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

        private static final Map<Integer, Day> days = new HashMap<>();

        static {
            for (Day day : values()) {
                days.put(day.getId(), day);
            }
        }

        private Day(String name, int id)
        {
            this.name = name;
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public int getId()
        {
            return id;
        }

        public static Day fromId(int id)
        {
            if (days.containsKey(id)) {
                return days.get(id);
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

        public int getTime()
        {
            return time;
        }
    }

    private static final String DEFAULT_TO_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Day weekStartsAt = Day.MONDAY;
    private static final Day weekEndsAt = Day.SUNDAY;
    private static final List<Day> weekendDays = Arrays.asList(Day.SATURDAY, Day.SUNDAY);

    private static final SimpleDateFormat format = new SimpleDateFormat(DEFAULT_TO_STRING_FORMAT);

    private final Calendar time;

    /**
     * Attempts to create a new Carbon instance with the current date and time.
     *
     * @see java.text.SimpleDateFormat
     */
    public Carbon()
    {
        this.time = Calendar.getInstance();

        this.time.setFirstDayOfWeek(weekStartsAt.getId());
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
     * @throws ParseException if the date string given doesn't match the format
     */
    public Carbon(String time) throws ParseException
    {
        this.time = Calendar.getInstance();

        this.time.setFirstDayOfWeek(weekStartsAt.getId());
        this.time.setTime(format.parse(time));
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
     * Creates a new Carbon instance with the date and time set to tomorrow.
     *
     * @return a Carbon instance with the date and time set to tomorrows date.
     */
    public static Carbon tomorrow()
    {
        return now().addDay();
    }

    /**
     * Creates a new Carbon instance with the date and time set to yesterday.
     *
     * @return a Carbon instance with the date and time set to yesterdays date.
     */
    public static Carbon yesterday()
    {
        return now().subDay();
    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////////// GETTERS AND SETTERS /////////////////////
    ///////////////////////////////////////////////////////////////////
    public Carbon setSecond(int second)
    {
        time.set(Calendar.SECOND, second);

        return this;
    }

    public int getSecond()
    {
        return time.get(Calendar.SECOND);
    }

    public Carbon setMinute(int minute)
    {
        time.set(Calendar.MINUTE, minute);

        return this;
    }

    public int getMinute()
    {
        return time.get(Calendar.MINUTE);
    }

    public Carbon setHour(int hour)
    {
        time.set(Calendar.HOUR, hour);

        return this;
    }

    public int getHour()
    {
        return time.get(Calendar.HOUR);
    }

    public Carbon setDay(int day)
    {
        time.set(Calendar.DAY_OF_MONTH, day);

        return this;
    }

    public int getDay()
    {
        return time.get(Calendar.HOUR);
    }

    public Carbon setWeek(int week)
    {
        time.set(Calendar.WEEK_OF_MONTH, week);

        return this;
    }

    public int getWeek()
    {
        return time.get(Calendar.WEEK_OF_MONTH);
    }

    public Carbon setMonth(int month)
    {
        time.set(Calendar.MONTH, month);

        return this;
    }

    public int getMonth()
    {
        return time.get(Calendar.MONTH);
    }

    public Carbon setYear(int year)
    {
        time.set(Calendar.YEAR, year);

        return this;
    }

    public int getYear()
    {
        return time.get(Calendar.YEAR);
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// ADDITIONS AND SUBTRACTIONS ////////////////////
    ///////////////////////////////////////////////////////////////////
    public Carbon addSecond()
    {
        return addSeconds(1);
    }

    public Carbon addSeconds(int seconds)
    {
        time.add(Calendar.SECOND, getPositive(seconds));

        return this;
    }

    public Carbon subSecond()
    {
        return subSeconds(1);
    }

    public Carbon subSeconds(int seconds)
    {
        time.add(Calendar.SECOND, getNegative(seconds));

        return this;
    }

    public Carbon addMinute()
    {
        return addMinutes(1);
    }

    public Carbon addMinutes(int minutes)
    {
        time.add(Calendar.MINUTE, getPositive(minutes));

        return this;
    }

    public Carbon subMinute()
    {
        return subMinutes(1);
    }

    public Carbon subMinutes(int minutes)
    {
        time.add(Calendar.MINUTE, getNegative(minutes));

        return this;
    }

    public Carbon addHour()
    {
        return addHours(1);
    }

    public Carbon addHours(int hours)
    {
        time.add(Calendar.HOUR, getPositive(hours));

        return this;
    }

    public Carbon subHour()
    {
        return subHours(1);
    }

    public Carbon subHours(int hours)
    {
        time.add(Calendar.HOUR, getNegative(hours));

        return this;
    }

    public Carbon addDay()
    {
        return addDays(1);
    }

    public Carbon addDays(int days)
    {
        time.add(Calendar.DAY_OF_MONTH, getPositive(days));

        return this;
    }

    public Carbon subDay()
    {
        return subDays(1);
    }

    public Carbon subDays(int days)
    {
        time.add(Calendar.DAY_OF_MONTH, getNegative(days));

        return this;
    }

    public Carbon addWeek()
    {
        return addWeeks(1);
    }

    public Carbon addWeeks(int weeks)
    {
        time.add(Calendar.WEEK_OF_MONTH, getPositive(weeks));

        return this;
    }

    public Carbon subWeek()
    {
        return subWeeks(1);
    }

    public Carbon subWeeks(int weeks)
    {
        time.add(Calendar.WEEK_OF_MONTH, getNegative(weeks));

        return this;
    }

    public Carbon addMonth()
    {
        return addMonths(1);
    }

    public Carbon addMonths(int months)
    {
        time.add(Calendar.MONTH, getPositive(months));

        return this;
    }

    public Carbon subMonth()
    {
        return addMonths(1);
    }

    public Carbon subMonths(int months)
    {
        time.add(Calendar.MONTH, getNegative(months));

        return this;
    }

    public Carbon addYear()
    {
        return addYears(1);
    }

    public Carbon addYears(int years)
    {
        time.add(Calendar.YEAR, getPositive(years));

        return this;
    }

    public Carbon subYear()
    {
        return subYears(1);
    }

    public Carbon subYears(int years)
    {
        time.add(Calendar.YEAR, getNegative(years));

        return this;
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
    /////////////////////////// DIFFERENCES ///////////////////////////
    ///////////////////////////////////////////////////////////////////
    public boolean isPast()
    {
        return Calendar.getInstance().getTimeInMillis() < time.getTimeInMillis();
    }

    public boolean isFuture()
    {
        return !isPast();
    }

    public long diff()
    {
        long current = System.currentTimeMillis();
        long unixTime = time.getTimeInMillis();

        long value = (current - unixTime) / 1000;

        return value >= 0 ? value : value * -1;
    }

    public String diffForHumans()
    {
        long unix = diff();

        if (unix == 0) {
            return "now";
        }

        StringBuilder sb = new StringBuilder();

        long sec = (unix >= 60 ? unix % 60 : unix);
        long min = (unix = (unix / 60)) >= 60 ? unix % 60 : unix;
        long hrs = (unix = (unix / 60)) >= 24 ? unix % 24 : unix;
        long days = (unix = (unix / 24)) >= 30 ? unix % 30 : unix;
        long months = (unix = (unix / 30)) >= 12 ? unix % 12 : unix;
        long years = (unix / 12);

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
            sb.append("about ").append(sec).append(" seconds");
        }

        if (isPast()) {
            sb.append(" from now");
        } else {
            sb.append(" ago");
        }

        return sb.toString().trim();
    }

    ///////////////////////////////////////////////////////////////////
    //////////////////////////// MODIFIERS ////////////////////////////
    ///////////////////////////////////////////////////////////////////
    public Carbon startOfDay()
    {
        return setHour(0).setMinute(0).setSecond(0);
    }

    public Carbon endOfDay()
    {
        return setHour(23).setMinute(59).setSecond(59);
    }

    public Carbon startOfMonth()
    {
        return startOfDay().setDay(1);
    }

    public Carbon endOfMonth()
    {
        Calendar cal = new GregorianCalendar(getYear(), getMonth(), getDay());

        return setDay(cal.getActualMaximum(Calendar.DAY_OF_MONTH)).endOfDay();
    }

    public Carbon startOfYear()
    {
        return setMonth(1).startOfMonth();
    }

    public Carbon endOfYear()
    {
        return setMonth(Time.MONTHS_PER_YEAR.getTime()).endOfMonth();
    }

    @Override
    public String toString()
    {
        return format.format(time.getTime());
    }

    @Override
    public Carbon clone()
    {
        try {
            return new Carbon(toString());
        } catch (ParseException ex) {
            Logger.getLogger(Carbon.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
