# Maapstricht — Project Transitor 

A route planner for Maastricht that calculates routes, distance and travel time between two postal codes by **walking, cycling or bus**, shown on an interactive map.

Built as a group project (Project 1-2, BSc Computer Science, Maastricht University, 2024).

Project report: [Report (PDF)](./03-FinalReport.pdf)

## Features
- **Walking & cycling:** distance with the Haversine formula and travel time based on average speeds for Maastricht residents (5 km/h walking, 15 km/h cycling)
- **Public transport:** finds the bus stops near both postal codes (within 300 m, widening to 500 m) and finds the fastest route using GTFS timetable data
- **Transfers:** routes with bus transfers, found either with a SQL-based search or with **A\***, using the straight-line distance at an average bus speed of 20 km/h as the heuristic
- **Route drawing:** bus routes follow the real road shape (GTFS shapes) on an interactive map. Walking and cycling routes are shown as dotted lines
- **Accessibility score per postal code:** based on Hansen's Gravity Model with exponential distance decay, counting nearby shops, schools, healthcare, tourism and bus stops, with bonus points for wheelchair-accessible places
- **Postal code lookup:** unknown postal codes are looked up through an external API and cached

## Built with
- Java 17, Maven
- JavaFX + Gluon Maps (interface and map)
- MySQL (GTFS timetable data and postal code coordinates)
- GraphHopper (routing on OpenStreetMap data)
- Gson, OpenCSV, JSON-simple
- JUnit 5 + JaCoCo (tests and coverage)

## How it evolved
| Phase | What was added |
|---|---|
| 1 | Distance and time calculator, Swing GUI drawing a straight line on a static map |
| 2 | Direct bus routing from a GTFS database, switch to JavaFX + Gluon Maps, routes drawn along real roads |
| 3 | Bus transfers (SQL query + A*), accessibility scores per postal code, faster database access |

The branches (`Phase2`, `Phase3`, `Phase3-AStar`, `AstarDrawing`, `transfers`, `accessibility`, …) contain the work from each stage. `main` is the final version.

## Getting started

### Requirements
- Java 17 or newer
- A MySQL database containing the GTFS data for the Netherlands. The database is **not included** in this repo, so you need to import the GTFS feed yourself and set your connection details in the code.

### Map data
The OpenStreetMap file is too large for GitHub (1.7 GB), so it isn't included:
1. Download the Limburg (NL) extract from [Geofabrik](https://download.geofabrik.de/europe/netherlands/limburg.html)
2. Save it as `src/main/java/bus/maapstrichtfinal/Map/data/limburg-latest.osm`

### Run
```bash
./mvnw clean javafx:run
```
On Windows, use `mvnw.cmd clean javafx:run`.

## Team
Stefani Parvina · Kamil Lipiński · Allen Mirzoyan ·  Alexia Raportaru · Antoni Sienkiewicz · Mila Spasova · Alexandra Stan
