# API Definition

Until you have a better documentation you can consult the definition of the service API in RAML format.


# REST resources
0.8.1-SNAPSHOT


## `POST operations/jts/buffer`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

**Query Param**: `distance`, `double`

**Query Param**: `segments`, `int`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

---------------------------------------
## `POST operations/jts/centroid`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`


---------------------------------------
## `POST operations/jts/difference`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`


---------------------------------------
## `POST operations/jts/difference/elements`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/division`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/division/line`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.DivisionData`)

`{"divisionLine":{"crs":"string","id":"string","wkt":"string"},"geomToDivide":{"crs":"string","id":"string","wkt":"string"}}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/division/polygon`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.DivisionData`)

`{"divisionLine":{"crs":"string","id":"string","wkt":"string"},"geomToDivide":{"crs":"string","id":"string","wkt":"string"}}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/envelope`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`


---------------------------------------
## `POST operations/jts/intersect/elements`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/intersection`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

---------------------------------------
## `POST operations/jts/intersection/elements`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/sym-difference`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`


---------------------------------------
## `POST operations/jts/sym-difference/elements`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

**Query Param**: `tolerance`, `double`


### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/union`

### Request

**Content-Type**: `application/json`

**Request Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`


---------------------------------------
## `POST operations/jts/union/combined`

### Request

**Content-Type**: `application/json`

**Request Body**: (Collection of `org.geowe.service.model.FlatGeometry`)

`[{"crs":"string","id":"string","wkt":"string"}]`


### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`


---------------------------------------
## `POST operations/jts/union/overlaped`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.OperationData`)

`{"overlayData":[{"crs":"string","id":"string","wkt":"string"}],"sourceData":[{"crs":"string","id":"string","wkt":"string"}]}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (Collection of `org.geowe.service.model.FlatGeometry`)
`[{"crs":"string","id":"string","wkt":"string"}]`


---------------------------------------
## `POST operations/jts/validation`

### Request

**Content-Type**: `application/json`

**Request Body**: (`org.geowe.service.model.FlatGeometry`)

`{"crs":"string","id":"string","wkt":"string"}`

### Response

**Content-Type**: `application/json`

#### `201 Created`

**Response Body**: (`org.geowe.service.model.ValidationResult`)

`{"valid":boolean,"validatedFlatGeometry":flatGeometry,"errors":ValidationErrorData[]}`
