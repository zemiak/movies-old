package com.zemiak.movies.strings;

import java.text.Normalizer;

/**
 *
 * @author vasko
 */
public class Encodings {
    public static String toAscii(final String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
