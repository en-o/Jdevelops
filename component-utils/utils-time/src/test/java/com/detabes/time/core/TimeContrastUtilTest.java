package com.detabes.time.core;

import junit.framework.TestCase;
import org.joda.time.DateTime;

import java.util.Date;

public class TimeContrastUtilTest extends TestCase {

    public void testBeginGteEnd() {
        assertEquals(TimeContrastUtil.beginGteEnd(DateTime.now().plusDays(-2).toDate(), new Date()),false);
        assertEquals(TimeContrastUtil.beginGteEnd(new Date(), new Date()),true);
        assertEquals(TimeContrastUtil.beginGteEnd(new Date(), DateTime.now().plusDays(2).toDate()),false);
        assertEquals(TimeContrastUtil.beginGteEnd(new Date(), DateTime.now().plusDays(-2).toDate()),true);
    }

    public void testBeginGtEnd() {
        assertEquals(TimeContrastUtil.beginGtEnd(DateTime.now().plusDays(-2).toDate(), new Date()),false);
        assertEquals(TimeContrastUtil.beginGtEnd(new Date(), new Date()),false);
        assertEquals(TimeContrastUtil.beginGtEnd(new Date(), DateTime.now().plusDays(2).toDate()),false);
        assertEquals(TimeContrastUtil.beginGtEnd(new Date(), DateTime.now().plusDays(-2).toDate()),true);
    }
}