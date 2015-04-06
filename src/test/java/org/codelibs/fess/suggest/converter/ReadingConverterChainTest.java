package org.codelibs.fess.suggest.converter;

import junit.framework.TestCase;

import java.util.List;

public class ReadingConverterChainTest extends TestCase {
    public void test_convert() {
        ReadingConverterChain chain = new ReadingConverterChain();
        chain.addConverter(new KatakanaConverter());
        chain.addConverter(new KatakanaToAlphabetConverter());

        List<String> list = chain.convert("検索");
        assertTrue(list.contains("ケンサク"));
        assertTrue(list.contains("kennsaku"));
    }
}
