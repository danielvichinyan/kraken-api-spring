package com.nexo.marketdata.model.enums;

public enum KrakenRestApiMethod {

    /* Public methods */
    TIME("Time", true),
    ASSETS("Assets", true),
    ASSET_PAIRS("AssetPairs", true),
    TICKER("Ticker", true),
    OHLC("OHLC", true),
    DEPTH("Depth", true),
    TRADES("Trades", true),
    SPREAD("Spread", true),

    /* Private methods */
    BALANCE("Balance", false),
    TRADE_BALANCE("TradeBalance", false),
    OPEN_ORDERS("OpenOrders", false),
    CLOSED_ORDERS("ClosedOrders", false),
    QUERY_ORDERS("QueryOrders", false),
    TRADES_HISTORY("TradesHistory", false),
    QUERY_TRADES("QueryTrades", false),
    OPEN_POSITIONS("OpenPositions", false),
    LEDGERS("Ledgers", false),
    QUERY_LEDGERS("QueryLedgers", false),
    TRADE_VOLUME("TradeVolume", false),
    ADD_ORDER("AddOrder", false),
    CANCEL_ORDER("CancelOrder", false),
    DEPOSIT_METHODS("DepositMethods", false),
    DEPOSIT_ADDRESSES("DepositAddresses", false),
    DEPOSIT_STATUS("DepositStatus", false),
    WITHDRAW_INFO("WithdrawInfo", false),
    WITHDRAW("Withdraw", false),
    WITHDRAW_STATUS("WithdrawStatus", false),
    WITHDRAW_CANCEL("WithdrawCancel", false),;

    public final String name;
    public final boolean isPublic;

    KrakenRestApiMethod(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }
}
