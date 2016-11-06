# Super Simple Stocks

## Overview

Super Simple Stocks is library and application that allows to do basic stock market operations.

Operations:

1. For a given stock,
    * Given any price as input, calculate the dividend yield
    * Given any price as input, calculate the P/E ratio
    * Record a trade, with timestamp, quantity, buy or sell indicator and price
2. Calculate the GBCE All Share Index using the geometric mean of the Volume Weighted Stock Price for all stocks

## Application requirements

1. Java 8 SDK
2. Maven 3 (required for first build)

## How to build and test

To build and test, go to sss folder and run
```
mvn clean install
```

To test
```
mvn test
```

To run integration tests
```
mvn verify
```

## Typical use-case

1. Run Super Simple Stocks Application. To do this locate *sssc* and run from that folder
```
cd sssc
mvn exec:java -Dexec.mainClass="com.github.kilork.sssc.App"
```

2. Use interactive dialog to perform operations

## GBCE Sample Data


| Stock Symbol |Type|Last Dividend|Fixed Dividend|Par Value|
|-----------:|---:|------------:|-------------:|--------:|
|TEA|Common|0||100|
|POP|Common|8||100|
|ALE|Common|23||60|
|GIN|Preferred|8|2%|100|
|JOE|Common|13||250|

