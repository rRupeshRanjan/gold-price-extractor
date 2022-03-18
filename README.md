This project scraps mmtc-pamp digital gold (24k) prices from [Gadgets 360](https://gadgets.ndtv.com/finance/digital-gold-price-in-india). 
The historical data is then stored in SQL table, which can be retrieved via rest apis.

**APIs available:**
- GET _/digital-gold-prices/get-historical-prices_
  - Fetches historical prices
- GET _/digital-gold-prices/get-current-price_
  - Fetches current price
- POST _/digital-gold-prices/save-historical-prices_
  - Saves last 30 days prices
- POST _/pnl/calculatePnl?buyDate=2022-01-01&sellDate=2022-01-31_
  - calculate pnl between date range in YYYY-MM-DD format

**SQL Schema:**
```
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| date          | date         | NO   | PRI | NULL    |       |
| close_price   | double       | NO   |     | NULL    |       |
| delta         | varchar(255) | YES  |     | NULL    |       |
| highest_price | double       | NO   |     | NULL    |       |
| lowest_price  | double       | NO   |     | NULL    |       |
| open_price    | double       | NO   |     | NULL    |       |
| price         | double       | NO   |     | NULL    |       |
+---------------+--------------+------+-----+---------+-------+
```

**Tech Stack:**
- Java (8 or higher)
- Spring boot
- Spring JPA
- Jsoup (for web scrapping)
- SQL
