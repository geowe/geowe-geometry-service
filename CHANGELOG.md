# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).


## [Unreleased]
### Added
- More resources
- Geometry validations
- Asynchronous tasks

## [0.9.0]SNAPSHOT - 2017-03-31

### Added
- Geometry validation Resource (POST). For the moment basic topological validations with JTS

## [0.8.0]SNAPSHOT - 2017-03-21

### Added
- Improve performance in intersects resource (stream parallel processing)
	

### Removed
- API resource (OPTIONS)

## [0.7.0]SNAPSHOT - 2017-03-17

### Added
- Implemented resources for JTS operations:
	- Decompose multi geometry into basic geometry /jts/division

- Divide LineString resource (/jts/division/line) return FlatGeometries
- Divide Polygon (/jts/division/polygon) return FlatGeometries
	
	
## [0.6.0]SNAPSHOT - 2017-03-16

### Added
- Implemented resources for JTS operations:
	- Divide LineString /jts/division/line
	- Divide Polygon /jts/division/polygon

### Removed
- Divide LineStrings resource: /jts/division/lines
- Divide polygons resource: /jts/division/polygons

## [0.5.0]SNAPSHOT - 2017-03-15

### Added
- Implemented resources for JTS operations:
	- Divide LineString (not supported division for crossed linestrings)
	
## [0.4.1]SNAPSHOT - 2017-03-14

### Added
- Implemented resources for JTS operations:
	- Divide Polygons

## [0.3.1]SNAPSHOT - 2017-03-11

### Added
- Implemented resources for JTS operations:
	- Union: combine
	- Union: overlap


## [0.2.1]SNAPSHOT - 2017-03-10

### Added
- Intersection, Difference and Symmetric Difference returns FlatGeometries.
- improve performance

## [0.2.0]SNAPSHOT - 2017-03-09

### Added
- Implemented resources for JTS operations:
	- Intersect

## [0.1.0]SNAPSHOT - 2017-03-08

### Added
- Implemented resources for JTS operations:
	- Difference
	- Symmetric Difference 
- Added support for Points and LineStrings in intersection, Difference and Symmetric Difference operations. Beware with tolerance to obtain best results

## [0.0.1]SNAPSHOT - 2017-03-06

### Added
- Implemented resources for JTS operations: 
  - Buffer 
  - Envelope 
  - Centroid 
  - Union 
  - Intersection
