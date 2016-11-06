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

import com.github.kilork.sss.StockPrice;
import com.github.kilork.sss.StockTradeType;
import com.github.kilork.sss.gbce.GBCEStockTrade;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public class MemoryGBCEStockTradeBuilderTest {
    
    private final StockPrice price = new StockPrice(100);
    private final StockPrice price2 = new StockPrice(200);
    
    private final MemoryGBCEStockTradeFactory instance = new MemoryGBCEStockTradeFactory();

    @Test(expected = IllegalArgumentException.class)
    public void testSellZeroQuantity() {
        instance.sell(0, price);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSellNegativeQuantity() {
        instance.sell(-1, price);
    }

    @Test
    public void testSellNormalQuantity() {
        GBCEStockTrade result = instance.sell(1, price);
        assertThat(result, notNullValue());
        assertThat(result.getQuantity(), equalTo(1));
        assertThat(result.getPrice(), equalTo(price));
        assertThat(result.getType(), equalTo(StockTradeType.SELL));
        
        result = instance.sell(2, price);
        assertThat(result, notNullValue());
        assertThat(result.getQuantity(), equalTo(2));
        assertThat(result.getPrice(), equalTo(price));
        assertThat(result.getType(), equalTo(StockTradeType.SELL));

        result = instance.sell(3, price2);
        assertThat(result, notNullValue());
        assertThat(result.getQuantity(), equalTo(3));
        assertThat(result.getPrice(), equalTo(price2));
        assertThat(result.getType(), equalTo(StockTradeType.SELL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuyZeroQuantity() {
        instance.buy(0, price);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuyNegativeQuantity() {
        instance.buy(-1, price);
    }

    @Test
    public void testBuyNormalQuantity() {
        GBCEStockTrade result = instance.buy(1, price);
        assertThat(result, notNullValue());
        assertThat(result.getQuantity(), equalTo(1));
        assertThat(result.getPrice(), equalTo(price));
        assertThat(result.getType(), equalTo(StockTradeType.BUY));
        
        result = instance.buy(2, price);
        assertThat(result, notNullValue());
        assertThat(result.getQuantity(), equalTo(2));
        assertThat(result.getPrice(), equalTo(price));
        assertThat(result.getType(), equalTo(StockTradeType.BUY));

        result = instance.buy(3, price2);
        assertThat(result, notNullValue());
        assertThat(result.getQuantity(), equalTo(3));
        assertThat(result.getPrice(), equalTo(price2));
        assertThat(result.getType(), equalTo(StockTradeType.BUY));
    }
    
}
