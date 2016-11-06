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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

/**
 * Stock Trade Timestamp. All dates work with this class. Implementation based
 * around LocalDateTime.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public final class StockTradeTimestamp implements Comparable<StockTradeTimestamp> {

    private final LocalDateTime timestamp;

    @Override
    public int compareTo(StockTradeTimestamp o) {
        return timestamp.compareTo(o.timestamp);
    }

    private StockTradeTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.timestamp);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StockTradeTimestamp other = (StockTradeTimestamp) obj;
        return Objects.equals(this.timestamp, other.timestamp);
    }

    public static StockTradeTimestamp now() {
        return new StockTradeTimestamp(LocalDateTime.now());
    }

    public static StockTradeTimestamp plus(StockTradeTimestamp t, long amount, TemporalUnit unit) {
        return new StockTradeTimestamp(t.timestamp.plus(amount, unit));
    }

    public static StockTradeTimestamp minus(StockTradeTimestamp t, long amount, TemporalUnit unit) {
        return new StockTradeTimestamp(t.timestamp.minus(amount, unit));
    }

    @Override
    public String toString() {
        return timestamp.toString();
    }

    /**
     *
     * @param value
     * @return
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static StockTradeTimestamp valueOf(String value) {
        LocalDateTime parsedTimestamp = LocalDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
        return new StockTradeTimestamp(parsedTimestamp);
    }

}
