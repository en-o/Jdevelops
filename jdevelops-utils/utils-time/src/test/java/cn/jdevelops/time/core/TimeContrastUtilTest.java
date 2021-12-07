package cn.jdevelops.time.core;

import cn.jdevelops.constant.time.TimeFormat;
import junit.framework.TestCase;
import org.joda.time.DateTime;

import java.util.Date;

public class TimeContrastUtilTest extends TestCase {

    public void testBeginGteEnd() {
        assertFalse(TimeContrastUtil.beginGteEnd(DateTime.now().plusDays(-2).toDate(), new Date()));
        assertTrue(TimeContrastUtil.beginGteEnd(new Date(), new Date()));
        assertFalse(TimeContrastUtil.beginGteEnd(new Date(), DateTime.now().plusDays(2).toDate()));
        assertTrue(TimeContrastUtil.beginGteEnd(new Date(), DateTime.now().plusDays(-2).toDate()));
    }

    public void testTestBeginGteEnd() {
        assertFalse(TimeContrastUtil.beginGteEnd(DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertTrue(TimeContrastUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeContrastUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertTrue(TimeContrastUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }

    public void testBeginGtEnd() {
        assertFalse(TimeContrastUtil.beginGtEnd(DateTime.now().plusDays(-2).toDate(), new Date()));
        assertFalse(TimeContrastUtil.beginGtEnd(new Date(), new Date()));
        assertFalse(TimeContrastUtil.beginGtEnd(new Date(), DateTime.now().plusDays(2).toDate()));
        assertTrue(TimeContrastUtil.beginGtEnd(new Date(), DateTime.now().plusDays(-2).toDate()));
    }

    public void testTestBeginGtEnd() {
        assertFalse(TimeContrastUtil.beginGtEnd(DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeContrastUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeContrastUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertTrue(TimeContrastUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }

    public void testTimeGteNow() {
//        assertEquals(TimeContrastUtil.timeGteNow(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), false);
        assertTrue(TimeContrastUtil.timeGteNow(DateTime.now().plusDays(+1).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeContrastUtil.timeGteNow(DateTime.now().plusMinutes(-10).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }

    public void testTimeGtNow() {
//        assertEquals(TimeContrastUtil.timeGtNow(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), false);
        assertTrue(TimeContrastUtil.timeGtNow(DateTime.now().plusDays(+1).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeContrastUtil.timeGtNow(DateTime.now().plusMinutes(-10).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }
}