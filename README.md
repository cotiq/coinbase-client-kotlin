# Coinbase Advanced Trade API Client

This is an unofficial Kotlin client for the [Coinbase Advanced Trade API](https://docs.cdp.coinbase.com/advanced-trade/docs/welcome).

**⚠ Warning: This library is in the early development phase. Use at your own risk.**

## Supported endpoints

|    | REST API Endpoints          |
|----|-----------------------------|
| ✅ | Accounts                    |
| ✅ | Products                    |
| ✅ | Orders                      |
| ☐  | Portfolios                  |
| ☐  | Futures                     |
| ☐  | Perpetuals                  |
| ✅ | Fees                        |
| ☐  | Converts                    |
| ☐  | Public                      |
| ☐  | Payment Methods             |
| ☐  | Data API                    |

## Releases

The latest release is available on [Maven Central](https://central.sonatype.com/artifact/com.cotiq.lab/coinbase-client-kotlin):

```kotlin
implementation("com.cotiq.lab:coinbase-client-kotlin:0.0.1")
```

## Quick Start

Generate API credentials and configure permissions as described in the [Getting Started Guide](https://docs.cdp.coinbase.com/advanced-trade/docs/getting-started)

### REST API

To communicate with [Advanced Trade API](https://docs.cdp.coinbase.com/advanced-trade/docs/api-overview) endpoints create a `RestApiClient` with `RestApiClientBuilder`:

```kotlin
val apiClient = RestApiClientBuilder()
    .setCredentials(ApiCredentials(apiKeyName = "your_api_key", apiPrivateKey = "your_private_key"))
    .build()

// Request all accounts
val accounts = apiClient.accountApi.getAccounts().callBlocking()
println("My accounts: $accounts")
```

### WebSocket API

Follow [Advanced Trade WebSocket Best Practices](https://docs.cdp.coinbase.com/advanced-trade/docs/ws-best-practices):
> Try to spread subscriptions over more than one WebSocket client connection.
> For example, do not subscribe to BTC-USD and ETH-USD on the same channel if possible.
> Instead, open up two separate WebSocket connections to help load balance those inbound messages across separate connections.

To receive real-time market data updates for orders and trades from [Advanced Trade WebSocket](https://docs.cdp.coinbase.com/advanced-trade/docs/ws-overview), use `WebSocketClient`.

Use heartbeats channel to keep your connection alive:

```kotlin
val client = WebSocketClientBuilder()
    .setDataFeed(DataFeed.MarketData)
    .build()
client.onMessage<HeartbeatEvent> {
    println("onMessage: $it")
}
client.onState {
    println("onState: $it")
    if (it == ConnectionState.Connected) {
        client.subscribe(FeedChannel.Heartbeats, null).callBlocking()
    }
}
client.start().callBlocking()
```

## Contribution

Contributions are welcome! If you would like to contribute:

1. Fork the repository.
2. Make your changes in a separate branch.
3. Open a pull request (PR) with a clear description of your changes.

For more details, see the [GitHub Contribution Guide](https://docs.github.com/en/get-started/exploring-projects-on-github/contributing-to-a-project).

## License

This project is distributed under the
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0);
see LICENSE.txt for more information.