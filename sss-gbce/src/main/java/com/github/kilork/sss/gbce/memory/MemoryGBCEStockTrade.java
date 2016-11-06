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
import com.github.kilork.sss.StockTradeTimestamp;
import com.github.kilork.sss.StockTradeType;
import com.github.kilork.sss.gbce.GBCEStockTrade;

/**
 * Memory GBCE Stock Trade.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class MemoryGBCEStockTrade implements GBCEStockTrade {

    private final StockTradeTimestamp timestamp;
    private final int quantity;
    private final StockPrice price;

    MemoryGBCEStockTrade(StockTradeTimestamp timestamp, int quantity, StockPrice price) {
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public StockTradeTimestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public int getQuantity() {
        return quantity > 0 ? quantity : -quantity;
    }

    @Override
    public StockPrice getPrice() {
        return price;
    }

    @Override
    public StockTradeType getType() {
        if (quantity > 0) {
            return StockTradeType.BUY;
        }
        return StockTradeType.SELL;
    }

    @Override
    public String toString() {
        return "Timestamp: " + timestamp + ", Quantity: " + getQuantity() + ", Price: " + price + ", Indicator: " + getType();
    }

}
