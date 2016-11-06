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
package com.github.kilork.sss.util;

import com.github.kilork.sss.StockPrice;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Took here: http://stackoverflow.com/questions/22695654/computing-the-nth-root-of-p-using-bigdecimals
 * 
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public final class MathCalculus {

    private static final int SCALE = 10;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_DOWN;

    public static BigDecimal geometricMean(List<BigDecimal> values) {
        if (Objects.requireNonNull(values, "Values must be non null").isEmpty()) {
            return BigDecimal.ZERO;
        }
        if (values.size() == 1) { // do not waste resources for trivial case
            return values.get(0);
        }
        //TODO: not the best way to calculate this, probably we must use log-average approach
        // https://en.wikipedia.org/wiki/Geometric_mean
        
        BigDecimal mul = BigDecimal.ONE;
        for(BigDecimal value : values) {
            mul = mul.multiply(value);
        }
        BigDecimal nthRoot = MathCalculus.nthRoot(values.size(), mul);
        return nthRoot;
    }
    
    public static BigDecimal nthRoot(final int n, final BigDecimal a) {
        return nthRoot(n, a, BigDecimal.valueOf(.1).movePointLeft(SCALE));
    }

    private static BigDecimal nthRoot(final int n, final BigDecimal a, final BigDecimal p) {
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("nth root can only be calculated for positive numbers");
        }
        if (a.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        BigDecimal xPrev = a;
        BigDecimal x = a.divide(new BigDecimal(n), SCALE, ROUNDING_MODE);  // starting "guessed" value...
        while (x.subtract(xPrev).abs().compareTo(p) > 0) {
            xPrev = x;
            x = BigDecimal.valueOf(n - 1.0)
                    .multiply(x)
                    .add(a.divide(x.pow(n - 1), SCALE, ROUNDING_MODE))
                    .divide(new BigDecimal(n), SCALE, ROUNDING_MODE);
        }
        return x;
    }

    private MathCalculus() {
    }
}
