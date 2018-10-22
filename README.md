# Forex rates API

This is a simple application that acts as a local proxy for getting exchange rates. It's a service that can be consumed by other internal services to get the exchange rate between a set of currencies, so they don't have to care about the specifics of third-party providers.

The proxy use free tier of the 1forge Api which supports 1.000 requests per day. We need to cache the results for a time span (90 second). It limits the quota so that the result will be up-to-date all day

## Environment

- Java Development Kit (8.0 or above)
- Sbt (1.x)
  
## How to Use:

```
sbt run
```
The default service's port is `8888`

## API documentation

### Get all quotes

**URL** : `/quotes`

**Method** : `GET`

**Content-Type**: `application/json`

**Auth required** : NO

### Success Response

**Code** : `200 OK`

**Content example**

```json
[{
  "symbol": {
    "from":"AUD",
    "to":"CAD" 
  },
  "price": 0.931445,
  "bid":0.93141, 
  "ask":0.93148,
  "timestamp":"2018-10-22T11:21:25+07:00"
}]
```

### Convert currency values

**URL** : `/converts?{from}{to}{quantity}`

**Method** : `GET`

**Parameters**:
- from (string): From currency code. Support `USD`, `JPY`, `EUR`, `AUD`, `CAD`, `CHF`, `GBP`, `NZD`, `SGD`
- to (string): Target currency code that need to be converted
- quantity (number): Quantity of currency

**Content-Type**: `application/json`

**Auth required** : NO

### Success Response

**Code** : `200 OK`

**Content example**

```json
{ 
  "from":"USD",
  "to":"JPY",
  "price": 11259.2,
  "timestamp":"2018-10-22T11:21:25+07:00"
}
```
