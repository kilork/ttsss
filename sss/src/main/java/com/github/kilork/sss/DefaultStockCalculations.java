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

import com.github.kilork.sss.util.MathCalculus;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Collection of default stock calculations.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class DefaultStockCalculations<T extends StockTrade> implements StockCalculations<T> {

    @Override
    public final StockValue dividendYieldCommon(StockPrice lastDividend, StockPrice price) {
        if (StockPrice.ZERO.compareTo(price) == 0) {
            throw new IllegalArgumentException("Price must not be zero");
        }
        return lastDividend.divide(price);
    }

    @Override
    public final StockValue dividendYieldPreferred(StockValue fixedDividend, StockPrice parValue, StockPrice price) {
        return parValue.multiply(fixedDividend).divide(price);
    }

    @Override
    public final StockValue priceEarningsRatio(StockPrice price, StockPrice dividend) {
        if (StockPrice.ZERO.compareTo(dividend) == 0) {
            throw new IllegalArgumentException("Dividend must not be zero");
        }
        return price.divide(dividend);
    }

    @Override
    public final StockPrice geometricMean(Collection<StockPrice> prices) {
        if (Objects.requireNonNull(prices, "Prices must be non null").isEmpty()) {
            return StockPrice.ZERO;
        }
        List<BigDecimal> values = prices.stream()
                .map(StockPrice::asStockValue)
                .map(StockValue::getValue)
                .collect(Collectors.toList());
        BigDecimal geometricMean = MathCalculus.geometricMean(values);

        return new StockPrice(geometricMean);
    }

    @Override
    public final StockPrice volumeWeightedStockPrice(Collection<T> trades) {
        StockPrice totalSum = trades.stream()
                .map($ -> $.getPrice().multiply($.getQuantity()))
                .reduce(StockPrice.ZERO, (a, b) -> a.add(b));
        Integer quantitySum = trades.stream()
                .collect(Collectors.summingInt(StockTrade::getQuantity));

        return totalSum.divide(quantitySum);
    }
}
