# Configuration
Datasource properties are located in `application-dev.yml`. There is also a `schema.sql` file that will automatically run on application startup. So, you just have update the datasource properties and run the app to create the tables.
# Deployment
## Standard
###
```java
./gradlew bootRun
```
## Seed Data
This will insert 10k service records in the db after startup. See `ApplicationDataSeedRunner.java`.
###
```java
./gradlew bootRun --args='--seed=10000'
```
# API Documentation

## Get a Service
### Request

`GET /api/v1/services/{id}`

    curl -X GET http://localhost:8080/api/v1/services/20301564-321c-452a-89c3-07ffd17ecbb3

### Response

    {
        "id": "20301564-321c-452a-89c3-07ffd17ecbb3",
        "name": "service-5",
        "description": "This service does something cool.",
        "specificationName": "spec-5",
        "specificationVersion": "v2.0",
        "requestCount": 0,
        "endpoints": [
            {
                "url": "http://localhost/api/v1/test-5",
                "httpVerb": "GET",
                "oauth2Supported": true,
                "oauth1aSupported": false
            },
            {
                "url": "http://localhost/api/v1/test-5",
                "httpVerb": "POST",
                "oauth2Supported": true,
                "oauth1aSupported": false
            },
            {
                "url": "http://localhost/api/v1/test-5",
                "httpVerb": "PUT",
                "oauth2Supported": true,
                "oauth1aSupported": false
            },
            {
                "url": "http://localhost/api/v1/test-5",
                "httpVerb": "DELETE",
                "oauth2Supported": true,
                "oauth1aSupported": false
            }
        ]
    }

## Get Services
This endpoint uses cursor-based pagination to retrieve items. I also introduced spring caching since this data is pretty static.

### Request

`GET /api/v1/services?cursor={cursor}&limit={limit}`

    curl -X GET http://localhost:8080/api/v1/services?limit=1
### Parameters
- `cursor`(optional): Used to paginate items. Not needed on the initial request but should be supplied on subsequent requests until it is `null` in the api response. 
- `limit`(optional): Items per page (default: `100`, max: `100`)

### Response

    {
        "cursor": "Mg==",
        "limit": 1,
        "items": [
            {
                "id": "788291e3-b50c-4fb9-8297-a7ba48c1125e",
                "name": "service-1",
                "description": "This service does something cool.",
                "specificationName": "spec-1",
                "specificationVersion": "v2.0",
                "requestCount": 0,
                "endpoints": [
                    {
                        "url": "http://localhost/api/v1/test-1",
                        "httpVerb": "GET",
                        "oauth2Supported": true,
                        "oauth1aSupported": false
                    },
                    {
                        "url": "http://localhost/api/v1/test-1",
                        "httpVerb": "POST",
                        "oauth2Supported": true,
                        "oauth1aSupported": false
                    },
                    {
                        "url": "http://localhost/api/v1/test-1",
                        "httpVerb": "PUT",
                        "oauth2Supported": true,
                        "oauth1aSupported": false
                    },
                    {
                        "url": "http://localhost/api/v1/test-1",
                        "httpVerb": "DELETE",
                        "oauth2Supported": true,
                        "oauth1aSupported": false
                    }
                ]
            }
        ]
    }
