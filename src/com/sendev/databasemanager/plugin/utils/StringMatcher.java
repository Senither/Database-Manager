package com.sendev.databasemanager.plugin.utils;

import java.util.Collections;
import java.util.List;

public class StringMatcher
{

    public static Match match(String base, List<String> possibilities)
    {
        if (base == null || base.isEmpty()) {
            return null;
        }

        if (possibilities == null || possibilities.isEmpty()) {
            return null;
        }

        Collections.sort(possibilities, ( String option1, String option2 ) -> {
            if (option1.length() == option2.length()) {
                return 0;
            }

            if (option1.length() > option2.length()) {
                return -1;
            }

            if (option1.length() < option2.length()) {
                return 1;
            }

            return 0;
        });

        int baseLength = base.length();

        Match bestMatch = new Match(base, -1);

        for (String poss : possibilities) {
            if (poss.isEmpty()) {
                continue;
            }

            int matches = 0;
            int pos = -1;

            posLoop:
            for (int i = 0; i < Math.min(baseLength, poss.length()); i++) {
                if (base.charAt(i) == poss.charAt(i)) {
                    if (pos != -1) {
                        break;
                    }

                    pos = i;
                }
            }

            for (int i = 0; i < Math.min(baseLength, poss.length()); i++) {
                if (pos == -1) {
                    continue;
                }

                if (base.charAt(i) == poss.charAt(Math.min(i + pos, poss.length() - 1))) {
                    matches++;
                }
            }

            if (matches > bestMatch.length) {
                bestMatch = new Match(poss, matches);
            }
        }

        return bestMatch;
    }

    public static class Match implements Comparable<Match>
    {
        protected final String match;
        protected final int length;

        protected Match(String match, int length)
        {
            this.match = match;
            this.length = length;
        }

        public String getMatch()
        {
            return match;
        }

        public int getMatchLength()
        {
            return length;
        }

        @Override
        public int compareTo(Match other)
        {
            return Integer.valueOf(other.length).compareTo(length);
        }

        @Override
        public String toString()
        {
            return "Match {" + match + "," + length + "}";
        }
    }
}
