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
package com.github.kilork.sss.gbce;

import com.github.kilork.sss.StocksServiceProvider;
import com.github.kilork.sss.gbce.memory.MemoryGBCEStocksServiceProvider;

/**
 *
 * @author Alexander Korolev <kilork at yandex.ru>
 */
public interface GBCEStocksServiceProvider {

    static StocksServiceProvider<GBCEStocksService> inMemory() {
        return new MemoryGBCEStocksServiceProvider();
    }

}
