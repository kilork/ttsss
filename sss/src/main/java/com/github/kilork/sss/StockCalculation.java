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

/**
 * Possible stock calculations.
 * 
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public interface StockCalculation {

    /**
     * Calculate Dividend Yield
     * @param price
     * @return 
     */
    StockValue calculateDividendYield(StockPrice price);

    /**
     * Calculate P/E Ratio.
     * 
     * @param price
     * @return Return StockValue of P/E Ratio meaning how much you must pay to get 1 entity of earnings.
     */
    StockValue calculatePERatio(StockPrice price);

    /**
     * Calculate Volume Weighted Stock Price.
     * @param period
     * @return 
     */
    StockPrice calculateVolumeWeightedStockPrice(StockTradePeriod period);

}
