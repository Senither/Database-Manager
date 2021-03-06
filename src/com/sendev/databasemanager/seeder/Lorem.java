package com.sendev.databasemanager.seeder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.join;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Lorem extends Generator
{
    static {
        StringBuilder builder = new StringBuilder(36);

        for (char number = '0'; number <= '9'; number++) {
            builder.append(number);
        }

        for (char character = 'a'; character <= 'z'; character++) {
            builder.append(character);
        }

        characters = builder.toString().toCharArray();
    }

    private static final char[] characters;

    public Lorem(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public char character()
    {
        return character(false);
    }

    public char character(boolean includeUppercase)
    {
        return characters(1).charAt(0);
    }

    public String characters()
    {
        return characters(255, false);
    }

    public String characters(boolean includeUppercase)
    {
        return characters(255, false);
    }

    public String characters(int minimumLength, int maximumLength)
    {
        return characters(random().nextInt(maximumLength - minimumLength) + minimumLength, false);
    }

    public String characters(int minimumLength, int maximumLength, boolean includeUppercase)
    {
        return characters(random().nextInt(maximumLength - minimumLength) + minimumLength, includeUppercase);
    }

    public String characters(int fixedNumberOfCharacters)
    {
        return characters(fixedNumberOfCharacters, false);
    }

    public String characters(int fixedNumberOfCharacters, boolean includeUppercase)
    {
        if (fixedNumberOfCharacters < 1) {
            return "";
        }

        char[] buffer = new char[fixedNumberOfCharacters];

        for (int i = 0; i < buffer.length; i++) {
            char randomCharacter = characters[random().nextInt(characters.length)];

            if (includeUppercase && random().nextBoolean()) {
                randomCharacter = Character.toUpperCase(randomCharacter);
            }

            buffer[i] = randomCharacter;
        }

        return new String(buffer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<String> words(int num)
    {
        List<String> returnList = new ArrayList();

        for (int i = 0; i < num; i++) {
            returnList.add(word());
        }

        return returnList;
    }

    public List<String> words()
    {
        return words(3);
    }

    public String word()
    {
        return service().safeFetch("lorem.words");
    }

    public String sentence(int wordCount)
    {
        return capitalize(join(words(wordCount + random().nextInt(6)), " ") + ".");
    }

    public String sentence()
    {
        return sentence(3);
    }

    public List<String> sentences(int sentenceCount)
    {
        List<String> sentences = new ArrayList<String>(sentenceCount);

        for (int i = 0; i < sentenceCount; i++) {
            sentences.add(sentence());
        }

        return sentences;
    }

    public String paragraph(int sentenceCount)
    {
        return join(sentences(sentenceCount + random().nextInt(3)), " ");
    }

    public String paragraph()
    {
        return paragraph(3);
    }

    public List<String> paragraphs(int paragraphCount)
    {
        List<String> paragraphs = new ArrayList<String>(paragraphCount);

        for (int i = 0; i < paragraphCount; i++) {
            paragraphs.add(paragraph());
        }

        return paragraphs;
    }

    /**
     * Create a string with a fixed size. Can be useful for testing
     * validator based on length string for example
     *
     * @param numberOfLetters size of the expected String
     *
     * @return a string with a fixed size
     */
    public String fixedString(int numberOfLetters)
    {
        StringBuilder builder = new StringBuilder();

        while (builder.length() < numberOfLetters) {
            builder.append(sentence());
        }

        return StringUtils.substring(builder.toString(), 0, numberOfLetters);
    }
}
