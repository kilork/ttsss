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
import com.github.kilork.sss.StockValue;
import com.github.kilork.sss.gbce.GBCEStockTrade;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import com.github.kilork.sss.gbce.GBCEStockTradeFactory;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class CommonMemoryGBCEStockTest {

    private final StockTradeTimestamp startTimestamp = StockTradeTimestamp.now();
    private final StockPrice price = new StockPrice(100);

    private List<MemoryGBCEStockTrade> testData() {
        List<MemoryGBCEStockTrade> data = new ArrayList<>();
        StockTradeTimestamp timestamp = StockTradeTimestamp.plus(startTimestamp, 0, ChronoUnit.SECONDS);
        data.add(new MemoryGBCEStockTrade(timestamp, 1, new StockPrice(new BigDecimal("1"))));
        timestamp = StockTradeTimestamp.plus(startTimestamp, 30, ChronoUnit.SECONDS);
        data.add(new MemoryGBCEStockTrade(timestamp, 2, new StockPrice(new BigDecimal("1"))));
        timestamp = StockTradeTimestamp.plus(startTimestamp, 60, ChronoUnit.SECONDS);
        data.add(new MemoryGBCEStockTrade(timestamp, 3, new StockPrice(new BigDecimal("1"))));
        return data;
    }

    private static class TestCommonMemoryGBCEStock extends CommonMemoryGBCEStock {

        public TestCommonMemoryGBCEStock(String symbol, int lastDivident, int parValue) {
            super(symbol, lastDivident, parValue);
        }

    }
    
    private static class TestStockCalculations implements StockCalculations<MemoryGBCEStockTrade> {

        StockPrice lastDividend;
        StockPrice price;
        StockPrice dividend;
        private final StockValue dividendYieldCommon = new StockValue(BigDecimal.valueOf(200));
        private final StockValue priceEarningsRatio = new StockValue(BigDecimal.valueOf(300));

        @Override
        public StockValue dividendYieldCommon(StockPrice lastDividend, StockPrice price) {
            this.lastDividend = lastDividend;
            this.price = price;
            return dividendYieldCommon;
        }

        @Override
        public StockValue dividendYieldPreferred(StockValue fixedDividend, StockPrice parValue, StockPrice price) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public StockPrice geometricMean(Collection<StockPrice> prices) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public StockValue priceEarningsRatio(StockPrice price, StockPrice dividend) {
            this.price = price;
            this.dividend = dividend;
            return priceEarningsRatio;
        }

        @Override
        public StockPrice volumeWeightedStockPrice(Collection<MemoryGBCEStockTrade> trades) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    private final List<MemoryGBCEStockTrade> trades = testData();

    @Test
    public void testCreate() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock(null, 0, 0);
        GBCEStockTradeFactory result = instance.create();
        assertThat(result, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock(null, 0, 0);
        instance.add(null);
    }

    @Test
    public void testAdd() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock(null, 0, 0);
        for (MemoryGBCEStockTrade trade : trades) {
            instance.add(trade);
        }
    }

    @Test
    public void testSymbol() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock("Test", 0, 0);
        assertThat(instance.symbol(), equalTo("Test"));
    }

    @Test
    public void testTradesForPeriod() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock(null, 0, 0);
        for (MemoryGBCEStockTrade trade : trades) {
            instance.add(trade);
        }
        StockTradeTimestamp from = StockTradeTimestamp.minus(startTimestamp, 5, ChronoUnit.MINUTES);
        StockTradeTimestamp to = startTimestamp;
        StockTradePeriod period = new StockTradePeriod(from, to);
        Collection<GBCEStockTrade> result = instance.tradesForPeriod(period);
        assertThat(result, hasSize(1));
        
        from = startTimestamp;
        to = StockTradeTimestamp.plus(startTimestamp, 5, ChronoUnit.MINUTES);
        period = new StockTradePeriod(from, to);
        result = instance.tradesForPeriod(period);
        assertThat(result, hasSize(3));
    }
    @Test
    public void testCalculateDividentYield() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock(null, 1, 2);
        TestStockCalculations calculations = new TestStockCalculations();
        instance.setCalculations(calculations);
        StockValue calculateDividentYield = instance.calculateDividendYield(price);
    }

    @Test
    public void testCalculatePERatio() {
        TestCommonMemoryGBCEStock instance = new TestCommonMemoryGBCEStock(null, 1, 2);
        TestStockCalculations calculations = new TestStockCalculations();
        instance.setCalculations(calculations);
        StockValue calculatePERatio = instance.calculatePERatio(price);
    }

    /*
    
    TODO: use mockito for mocking calculations for tests more friendly
    @Test
    public void testCalculateVolumeWeightedStockPrice() {
        System.out.println("calculateVolumeWeightedStockPrice");
    }

    @Test
    public void testSetCalculations() {
        System.out.println("setCalculations");
    }

    @Test
    public void testGetCalculations() {
        System.out.println("getCalculations");
    }

    @Test
    public void testGetParValue() {
        System.out.println("getParValue");
    }

    @Test
    public void testToString() {
        System.out.println("toString");
    }
     */
}
