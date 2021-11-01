package com.detabes.time.core;

import com.detabes.constant.time.TimeFormat;
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

    public void testTestBeginGteEnd() {
        assertEquals(TimeContrastUtil.beginGteEnd(DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),false);
        assertEquals(TimeContrastUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),true);
        assertEquals(TimeContrastUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),false);
        assertEquals(TimeContrastUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),true);
    }

    public void testBeginGtEnd() {
        assertEquals(TimeContrastUtil.beginGtEnd(DateTime.now().plusDays(-2).toDate(), new Date()),false);
        assertEquals(TimeContrastUtil.beginGtEnd(new Date(), new Date()),false);
        assertEquals(TimeContrastUtil.beginGtEnd(new Date(), DateTime.now().plusDays(2).toDate()),false);
        assertEquals(TimeContrastUtil.beginGtEnd(new Date(), DateTime.now().plusDays(-2).toDate()),true);
    }

    public void testTestBeginGtEnd() {
        assertEquals(TimeContrastUtil.beginGtEnd(DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),false);
        assertEquals(TimeContrastUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),false);
        assertEquals(TimeContrastUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),false);
        assertEquals(TimeContrastUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)),true);
    }

    public void testTimeGteNow() {
//        assertEquals(TimeContrastUtil.timeGteNow(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), false);
        assertEquals(TimeContrastUtil.timeGteNow(DateTime.now().plusDays(+1).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), true);
        assertEquals(TimeContrastUtil.timeGteNow(DateTime.now().plusMinutes(-10).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), false);
    }

    public void testTimeGtNow() {
//        assertEquals(TimeContrastUtil.timeGtNow(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), false);
        assertEquals(TimeContrastUtil.timeGtNow(DateTime.now().plusDays(+1).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), true);
        assertEquals(TimeContrastUtil.timeGtNow(DateTime.now().plusMinutes(-10).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)), false);
    }
}