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
import com.github.kilork.sss.gbce.GBCEStockTrade;
import java.util.Objects;
import com.github.kilork.sss.gbce.GBCEStockTradeFactory;

/**
 * Memory GBCE Stock Trade Builder.
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
class MemoryGBCEStockTradeFactory implements GBCEStockTradeFactory {

    @Override
    public GBCEStockTrade sell(int quantity, StockPrice price) {
        return sell(StockTradeTimestamp.now(), quantity, price);
    }

    @Override
    public GBCEStockTrade buy(int quantity, StockPrice price) {
        return buy(StockTradeTimestamp.now(), quantity, price);
    }

    @Override
    public GBCEStockTrade sell(StockTradeTimestamp timestamp, int quantity, StockPrice price) {
        validate(quantity, price);
        return new MemoryGBCEStockTrade(timestamp, -quantity, price);
    }

    @Override
    public GBCEStockTrade buy(StockTradeTimestamp timestamp, int quantity, StockPrice price) {
        validate(quantity, price);
        return new MemoryGBCEStockTrade(timestamp, quantity, price);
    }

    private void validate(int quantity, StockPrice price) {
        if (!(quantity > 0)) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (!(Objects.requireNonNull(price, "Price must be non null value").compareTo(StockPrice.ZERO) > 0)) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }

}
