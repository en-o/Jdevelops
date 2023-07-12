package cn.jdevelops.util.core.string;

import junit.framework.TestCase;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexUtilTest extends TestCase {

    public void testIsMatcher() {
        String regex = "\\{([^\\}]*?)\\}";
        assertTrue(RegexUtil.isMatcher(regex,"{product}{storeName} €10 €20", Pattern.CASE_INSENSITIVE));
        assertFalse(RegexUtil.isMatcher(regex,"€10 €20", Pattern.CASE_INSENSITIVE));
    }

    public void testIsMatcherIgnore() {

        String regex = "\\{([^\\}]*?)\\}";
        assertTrue(RegexUtil.isMatcherIgnore(regex,"{product}{storeName} €10 €20"));
        assertFalse(RegexUtil.isMatcherIgnore(regex,"€10 €20"));
    }

    public void testExtractBracesList() {
        List<Map<String, Integer>> keywords = RegexUtil.extractBracesList("{product}{storeName} €10 €20");
        assertEquals(keywords.toString(),"[{product=1}, {storeName=1}]");
        keywords = RegexUtil.extractBracesList("{product}{storeName} €10 €20","product","storeName");
        assertTrue(keywords.isEmpty());
        keywords = RegexUtil.extractBracesList("{aa}{bb}{storeName} €10 €20");
        assertEquals(keywords.toString(),"[{aa=1}, {bb=1}, {storeName=1}]");
        keywords = RegexUtil.extractBracesList("{aa}{aa}{bb}{storeName} €10 €20","storeName");
        assertEquals(keywords.toString(),"[{aa=2}, {aa=2}, {bb=1}]");
        keywords = RegexUtil.extractBracesList("{aa} {aa} {bb} {bb} {aa} €10 €20");
        assertEquals(keywords.toString(),"[{aa=3}, {aa=3}, {bb=2}, {bb=2}, {aa=3}]");
    }

    public void testExtractBracesMap() {

        Map<String, Integer> keywords = RegexUtil.extractBracesMap("{product}{storeName} €10 €20");
        assertEquals(keywords.toString(),"{product=1, storeName=1}");
        keywords = RegexUtil.extractBracesMap("{product}{storeName} €10 €20","product","storeName");
        assertTrue(keywords.isEmpty());
        keywords = RegexUtil.extractBracesMap("{aa}{bb}{storeName} €10 €20","storeName");
        assertEquals(keywords.toString(),"{aa=1, bb=1}");
        keywords = RegexUtil.extractBracesMap("{aa}{aa}{bb}{storeName} €10 €20","storeName");
        assertEquals(keywords.toString(),"{aa=2, bb=1}");
        keywords = RegexUtil.extractBracesMap("{aa} {aa} {bb} {bb} {aa} €10 €20");
        assertEquals(keywords.toString(),"{aa=3, bb=2}");
    }
}
