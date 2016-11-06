/*
 * Copyright (C) 2016 Alexander Korolev <kilork at yandex.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kilork.sss;

import java.time.temporal.ChronoUnit;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class StockTradePeriodTest {
    
    @Test
    public void testLastNMinutes() {
        System.out.println("lastNMinutes");
        long n = 5;
        StockTradePeriod result = StockTradePeriod.lastNMinutes(n);
        assertThat(result, notNullValue());
        assertThat(result.getFrom(), lessThan(result.getTo()));
    }

    @Test
    public void testNormalOrder() {
        StockTradeTimestamp from = StockTradeTimestamp.now();
        StockTradeTimestamp to = StockTradeTimestamp.plus(from, 5, ChronoUnit.MINUTES);
        new StockTradePeriod(from, to);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseOrder() {
        StockTradeTimestamp from = StockTradeTimestamp.now();
        StockTradeTimestamp to = StockTradeTimestamp.plus(from, 5, ChronoUnit.MINUTES);
        new StockTradePeriod(to, from);
    }

}
