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

import com.github.kilork.sss.DefaultStockCalculations;
import com.github.kilork.sss.StockCalculations;
import com.github.kilork.sss.StockTradePeriod;
import com.github.kilork.sss.StockPrice;
import com.github.kilork.sss.gbce.GBCEStock;
import com.github.kilork.sss.gbce.GBCEStockTrade;
import com.github.kilork.sss.gbce.GBCEStocksService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In memory GBCE stock service implementation
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class MemoryGBCEStocksServiceImpl implements GBCEStocksService {

    private final Map<String, GBCEStock> stocks;
    private static final String VERSION = "0.0.1";
    private static final StockCalculations<GBCEStockTrade> CALCULATIONS = new DefaultStockCalculations<>();

    public MemoryGBCEStocksServiceImpl(Map<String, GBCEStock> stocks) {
        this.stocks = Collections.unmodifiableMap(stocks);
    }

    public MemoryGBCEStocksServiceImpl() {
        this(defaultGBCEStocks());
    }

    private static Map<String, GBCEStock> defaultGBCEStocks() {
        HashMap<String, GBCEStock> defaultStocks = new HashMap<>();
        putNewCommonGBCEStock(defaultStocks, "TEA", 0, 100);
        putNewCommonGBCEStock(defaultStocks, "POP", 8, 100);
        putNewCommonGBCEStock(defaultStocks, "ALE", 23, 60);
        putNewPreferredGBCEStock(defaultStocks, "GIN", 8, 2, 100);
        putNewCommonGBCEStock(defaultStocks, "JOE", 13, 250);
        return defaultStocks;
    }

    private static void putNewCommonGBCEStock(HashMap<String, GBCEStock> stocks, String symbol, int lastDivident, int parValue) {
        GBCEStock stock = MemoryGBCEStockFactory.createCommon(symbol, lastDivident, parValue);
        stock.setCalculations(CALCULATIONS);
        stocks.put(symbol, stock);
    }

    private static void putNewPreferredGBCEStock(HashMap<String, GBCEStock> stocks, String symbol, int lastDivident, int fixedDivident, int parValue) {
        GBCEStock stock = MemoryGBCEStockFactory.createPreferred(symbol, lastDivident, fixedDivident, parValue);
        stock.setCalculations(CALCULATIONS);
        stocks.put(symbol, stock);
    }

    @Override
    public Collection<GBCEStock> getStocks() {
        return Collections.unmodifiableCollection(stocks.values());
    }

    @Override
    public Optional<GBCEStock> stockBySymbol(String symbol) {
        GBCEStock stock = stocks.get(symbol);
        return Optional.ofNullable(stock);
    }

    @Override
    public StockPrice allShareIndex() {
        StockTradePeriod stockPeriod = StockTradePeriod.lastNMinutes(5);
        List<StockPrice> stockPrices = stocks.values().stream()
                .map($ -> $.calculateVolumeWeightedStockPrice(stockPeriod))
                .collect(Collectors.toList());
        StockPrice geometricMean = CALCULATIONS.geometricMean(stockPrices);
        return geometricMean;
    }

    @Override
    public String version() {
        return VERSION;
    }

}
