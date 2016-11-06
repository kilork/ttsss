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

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Stock Trade Period.
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public final class StockTradePeriod implements Serializable {

    private final StockTradeTimestamp from;
    private final StockTradeTimestamp to;
    
    public static StockTradePeriod lastNMinutes(long n) {
        StockTradeTimestamp now = StockTradeTimestamp.now();
        return new StockTradePeriod(StockTradeTimestamp.minus(now, n, ChronoUnit.MINUTES), now);
    }

    public StockTradePeriod(StockTradeTimestamp from, StockTradeTimestamp to) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("From must be before to.");
        }
    }

    public StockTradeTimestamp getFrom() {
        return from;
    }

    public StockTradeTimestamp getTo() {
        return to;
    }
    
}
