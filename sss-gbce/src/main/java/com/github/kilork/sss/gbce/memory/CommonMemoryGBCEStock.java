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
package com.github.kilork.sss.gbce.memory;

import com.github.kilork.sss.StockCalculations;
import com.github.kilork.sss.StockPrice;
import com.github.kilork.sss.StockTradePeriod;
import com.github.kilork.sss.StockTradeTimestamp;
import java.util.NavigableMap;
import java.util.TreeMap;
import com.github.kilork.sss.StockValue;
import com.github.kilork.sss.gbce.GBCEStock;
import com.github.kilork.sss.gbce.GBCEStockTrade;
import java.util.Collection;
import java.util.Collections;
import com.github.kilork.sss.gbce.GBCEStockTradeFactory;

/**
 * Common Stock.
 * 
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class CommonMemoryGBCEStock implements GBCEStock {

    private final String symbol;
    private final NavigableMap<StockTradeTimestamp, GBCEStockTrade> trades = new TreeMap<>();
    private final StockPrice lastDividend;
    private final StockPrice parValue;
    private StockCalculations<GBCEStockTrade> calculations;

    CommonMemoryGBCEStock(String symbol, int lastDivident, int parValue) {
        this.symbol = symbol;
        this.lastDividend = new StockPrice(lastDivident);
        this.parValue = new StockPrice(parValue);
    }

    @Override
    public GBCEStockTradeFactory create() {
        return new MemoryGBCEStockTradeFactory();
    }

    @Override
    public final void add(GBCEStockTrade trade) {
        if (trade == null) {
            throw new IllegalArgumentException("Trade must be not null.");
        }
        trades.put(trade.getTimestamp(), trade);
    }

    @Override
    public final String symbol() {
        return symbol;
    }

    @Override
    public Collection<GBCEStockTrade> tradesForPeriod(StockTradePeriod period) {
        Collection<GBCEStockTrade> values = tradesForPeriodInternal(period);
        return Collections.unmodifiableCollection(values);
    }

    private Collection<GBCEStockTrade> tradesForPeriodInternal(StockTradePeriod period) {
        NavigableMap<StockTradeTimestamp, GBCEStockTrade> tradesSubmap = trades.subMap(period.getFrom(), true, period.getTo(), true);
        Collection<GBCEStockTrade> values = tradesSubmap.values();
        return values;
    }

    @Override
    public StockValue calculateDividendYield(StockPrice price) {
        return calculations.dividendYieldCommon(lastDividend, price);
    }

    @Override
    public StockValue calculatePERatio(StockPrice price) {
        return calculations.priceEarningsRatio(price, lastDividend);
    }

    @Override
    public StockPrice calculateVolumeWeightedStockPrice(StockTradePeriod period) {
        Collection<GBCEStockTrade> tradesForPeriod = tradesForPeriodInternal(period);
        if (tradesForPeriod.isEmpty()) {
            throw new IllegalArgumentException(String.format("No trades found to calculate volume weighted stock price for %s", symbol()));
        }
        return calculations.volumeWeightedStockPrice(tradesForPeriod);
    }

    @Override
    public final void setCalculations(StockCalculations calculations) {
        this.calculations = calculations;
    }

    protected final StockCalculations getCalculations() {
        return calculations;
    }

    protected final StockPrice getParValue() {
        return parValue;
    }

    protected final StockPrice getLastDividend() {
        return lastDividend;
    }
    
    @Override
    public String toString() {
        return "Common Stock {" + "Symbol=" + symbol + ", Last Dividend=" + lastDividend + ", Par Value=" + parValue + '}';
    }

}
