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
import com.github.kilork.sss.StockValue;
import com.github.kilork.sss.gbce.GBCEStock;
import com.github.kilork.sss.gbce.GBCEStockTrade;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import static org.junit.Assert.*;
import com.github.kilork.sss.gbce.GBCEStockTradeFactory;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class MemoryGBCEStocksServiceImplTest {
    
    public MemoryGBCEStocksServiceImplTest() {
    }

    Map<String, GBCEStock> stocks = testData();
    
    @Test
    public void testGetStocks() {
        MemoryGBCEStocksServiceImpl instance = new MemoryGBCEStocksServiceImpl(stocks);
        Collection<GBCEStock> result = instance.getStocks();
        assertThat(result, hasSize(stocks.size()));
    }

    @Test
    public void testStockBySymbolNull() {
        MemoryGBCEStocksServiceImpl instance = new MemoryGBCEStocksServiceImpl(stocks);
        Optional<GBCEStock> result = instance.stockBySymbol(null);
        assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    public void testStockBySymbolNonExistent() {
        MemoryGBCEStocksServiceImpl instance = new MemoryGBCEStocksServiceImpl(stocks);
        Optional<GBCEStock> result = instance.stockBySymbol("DUMMY");
        assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    public void testStockBySymbolFromTestData() {
        MemoryGBCEStocksServiceImpl instance = new MemoryGBCEStocksServiceImpl(stocks);
        for(Entry<String, GBCEStock> stock : stocks.entrySet()) {
            Optional<GBCEStock> result = instance.stockBySymbol(stock.getKey());
            assertThat(result, equalTo(Optional.of(stock.getValue())));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllShareIndexNoData() {
        MemoryGBCEStocksServiceImpl instance = new MemoryGBCEStocksServiceImpl(stocks);
        instance.allShareIndex();
    }

    @Test()
    public void testAllShareIndexDataWithVolume() {
        MemoryGBCEStocksServiceImpl instance = new MemoryGBCEStocksServiceImpl(testDataWithVolume());
        StockPrice result = instance.allShareIndex();
        BigDecimal expResult = BigDecimal.valueOf(4l);
        assertThat(result.asStockValue().getValue(), closeTo(expResult, new BigDecimal("0.0000001")));
    }

    private Map<String, GBCEStock> testData() {
        Map<String, GBCEStock> data = new HashMap<>();
        data.put("ABC", new TestGBCEStock("Test1"));
        data.put("DEF", new TestGBCEStock("Test2"));
        data.put("GHI", new TestGBCEStock("Test3"));
        return data;
    }

    private Map<String, GBCEStock> testDataWithVolume() {
        Map<String, GBCEStock> data = new HashMap<>();
        data.put("ABC", new TestGBCEStockWithVolume("Test1", 2));
        data.put("DEF", new TestGBCEStockWithVolume("Test2", 4));
        data.put("GHI", new TestGBCEStockWithVolume("Test3", 8));
        return data;
    }

    private static class TestGBCEStockWithVolume extends TestGBCEStock {

        private final StockPrice volumeWeightedStockPrice;
        
        public TestGBCEStockWithVolume(String symbol, int volumeWeightedStockPrice) {
            super(symbol);
            this.volumeWeightedStockPrice = new StockPrice(volumeWeightedStockPrice);
        }

        @Override
        public StockPrice calculateVolumeWeightedStockPrice(StockTradePeriod period) {
            return this.volumeWeightedStockPrice;
        }
        
    }

    private static class TestGBCEStock implements GBCEStock {

        private final String symbol;

        private TestGBCEStock(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public void setCalculations(StockCalculations calculations) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public GBCEStockTradeFactory create() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void add(GBCEStockTrade trade) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String symbol() {
            return symbol;
        }

        @Override
        public Collection<GBCEStockTrade> tradesForPeriod(StockTradePeriod period) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public StockValue calculateDividendYield(StockPrice price) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public StockValue calculatePERatio(StockPrice price) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public StockPrice calculateVolumeWeightedStockPrice(StockTradePeriod period) {
            throw new IllegalArgumentException();
        }
        
    }

}
