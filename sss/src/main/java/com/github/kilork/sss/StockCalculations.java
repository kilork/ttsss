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

import java.util.Collection;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public interface StockCalculations<T extends StockTrade> {

    StockValue dividendYieldCommon(StockPrice lastDividend, StockPrice price);

    StockValue dividendYieldPreferred(StockValue fixedDividend, StockPrice parValue, StockPrice price);

    StockPrice geometricMean(Collection<StockPrice> prices);

    StockValue priceEarningsRatio(StockPrice price, StockPrice dividend);

    StockPrice volumeWeightedStockPrice(Collection<T> trades);

}
