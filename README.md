# ShortWayForTickets
TicketAnalyzer is a Java application that reads flight ticket data from a JSON file, analyzes the flight durations and prices, and prints useful information such as the minimum flight times for each carrier and the difference between average and median prices.

## Features

- Reads ticket data from a JSON file located in the `resources` folder.
- Analyzes flight durations and prices for flights from Vladivostok (VVO) to Tel-Aviv (TLV).
- Prints the minimum flight times for each carrier.
- Calculates and prints the average price, median price, and the difference between the average and median prices.

## Requirements

- Java 11 or higher
- Maven

## Getting Started

### Prerequisites

Ensure you have Java 17 or higher installed. You can check your Java version by running:

```sh
java -version
```
Ensure you have Maven installed. You can check your Maven version by running:
```Java
mvn -version
```
## Installation
1. Clone the repository:
```sh
git clone https://github.com/kripkeroman/shortWayForTickets.git
cd ticket-analyzer
```
2. Build the project using Maven:
```sh
mvn clean install
```
## Running the Application
1. Place the file or use an existing file in the Tickets.json resources. The JSON file must contain ticket data in the following format:
```json
{
  "tickets": [
    {
      "origin": "VVO",
      "destination": "TLV",
      "carrier": "Carrier1",
      "departure_date": "12.05.18",
      "departure_time": "9:40",
      "arrival_date": "12.05.18",
      "arrival_time": "19:00",
      "price": 200
    }
    // more tickets...
  ]
}
```
2. Run the application:
```sh
mvn exec:java -Dexec.mainClass="org.example.TicketAnalyzer"
```
