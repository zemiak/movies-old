package com.zemiak.movies.strings;

import java.text.Normalizer;

public final class Encodings {
    public static String toAscii(final String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
