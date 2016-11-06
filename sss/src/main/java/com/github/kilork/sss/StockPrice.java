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
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Price holder.
 *
 * Can be transformed to StockValue.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public final class StockPrice implements Comparable<StockPrice>, Serializable {

    public static final StockPrice ZERO = new StockPrice(0);
    public static final StockPrice ONE = new StockPrice(1);
    
    private static final MathContext MATH_CONTEXT = new MathContext(6, RoundingMode.HALF_UP);

    private final BigDecimal amount;

    public StockPrice(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount, "Require non null amount");
    }

    public StockPrice(int amount) {
        this(BigDecimal.valueOf(amount));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.amount);

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
        final StockPrice other = (StockPrice) obj;

        return Objects.equals(this.amount, other.amount);
    }

    public StockValue asStockValue() {
        return new StockValue(amount);
    }

    public StockValue divide(StockPrice divisor) {
        return new StockValue(amount.divide(divisor.amount, MATH_CONTEXT));
    }

    public StockPrice divide(int divisor) {
        return new StockPrice(amount.divide(BigDecimal.valueOf(divisor), MATH_CONTEXT));
    }

    private StockPrice multiply(BigDecimal multiplicand) {
        return new StockPrice(amount.multiply(multiplicand));
    }

    public StockPrice multiply(StockValue multiplicand) {
        return multiply(multiplicand.getValue());
    }

    public StockPrice multiply(int multiplicand) {
        return multiply(BigDecimal.valueOf(multiplicand));
    }

    public StockPrice add(StockPrice augend) {
        return new StockPrice(amount.add(augend.amount));
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(amount);
    }

    @Override
    public int compareTo(StockPrice o) {
        return amount.compareTo(o.amount);
    }
    
    /**
     * Convert string value to stock price.
     * 
     * @param value
     * @throws NumberFormatException if {@code val} is not a valid
     *         representation of a {@code BigDecimal}.
     * @return 
     */
    public static StockPrice valueOf(String value) {
        return new StockPrice(new BigDecimal(value));
    }
}
