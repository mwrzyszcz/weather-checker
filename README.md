## Weather checker service - 5 day forecast

---

### How to use

1. Clone repository `$ git clone https://github.com/mwrzyszcz/weather-checker.git`
2. Run application
    * OpenJDK 11
    * Maven 3+
    * Spring Boot 2.3.4
    
3. Send `GET` request

#### Example   
`GET http://localhost:8080/forecast/daily/5day/20-400`  - use polish postal code
    
4. Consume response

#### Example

```json
{
    "postalCode": "20-400",
    "voivodeship": "Lubelskie",
    "forecastInformationDtos": [
        {
            "date": "2020-10-20",
            "dayOfWeek": "TUESDAY",
            "minTemp": 6.4,
            "maxTemp": 11.1
        },
        {
            "date": "2020-10-21",
            "dayOfWeek": "WEDNESDAY",
            "minTemp": 7.9,
            "maxTemp": 13.2
        },
        {
            "date": "2020-10-22",
            "dayOfWeek": "THURSDAY",
            "minTemp": 8.8,
            "maxTemp": 16.2
        },
        {
            "date": "2020-10-23",
            "dayOfWeek": "FRIDAY",
            "minTemp": 10.5,
            "maxTemp": 16.1
        },
        {
            "date": "2020-10-24",
            "dayOfWeek": "SATURDAY",
            "minTemp": 5.3,
            "maxTemp": 13.7
        }
    ]
}
```
---
## Notes
###### Daily rate limit: 50 requests
###### Using [accuweather API](https://developer.accuweather.com/)
