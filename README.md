# 📦 Tracking Number Generator API

A scalable Spring Boot REST API that generates unique tracking numbers for parcels.  
Designed for high concurrency and horizontal scalability.

---

## 🚀 Features
- RESTful API endpoint:  
  `GET /api/v1/tracking/next-tracking-number`
- Parameters:
  - `origin_country_id` – Origin country (ISO 3166-1 alpha-2)
  - `destination_country_id` – Destination country
  - `weight` – Weight in kilograms (up to 3 decimals)
  - `created_at` – RFC 3339 timestamp
  - `customer_id` – Customer UUID
  - `customer_name` – Customer’s name
  - `customer_slug` – Slug/kebab case name
- Tracking number:
  - Matches regex: `^[A-Z0-9]{1,16}$`
  - Guaranteed unique
  - Efficiently generated using a Snowflake-inspired ID generator
 
- git clone [https://github.com/rajreja3/tracking-number-generator.git](https://github.com/Rajreja3/tracking-id-generator.git)
- ./mvnw clean package -DskipTests
- java -jar target/tracking-0.0.1-SNAPSHOT.jar


-Curl Request 
  curl -G --data-urlencode "origin_country_id=MY" \
     --data-urlencode "destination_country_id=ID" \
     --data-urlencode "weight=1.234" \
     --data-urlencode "created_at=2018-11-20T19:29:32+08:00" \
     --data-urlencode "customer_id=de619854-b59b-425e-9db4-943979e1bd49" \
     --data-urlencode "customer_name=RedBox Logistics" \
     --data-urlencode "customer_slug=redbox-logistics" \
     "https://cooperative-comfort-production.up.railway.app/api/v1/tracking/next-tracking-number"

- JSON response includes:
- {"tracking_number":"2QAR89V9QKN4","created_at":"2025-09-18T17:51:10.555039314Z","generator":"snowflake-base36"}
  
- Deployed URL : https://cooperative-comfort-production.up.railway.app/
- Deployed on : [Railway](https://railway.com/)






