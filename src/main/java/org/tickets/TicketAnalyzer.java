package org.tickets;

import com.google.gson.*;
import org.tickets.dto.Ticket;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The TicketAnalyzer class provides methods to read, analyze, and print information about flight tickets
 * from a JSON file. The main functionalities include reading ticket data, analyzing flight durations and prices,
 * and printing the minimum flight times and price differences.
 */
public class TicketAnalyzer
{
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");

    /**
     * The main method is the entry point of the application. It reads the JSON file containing ticket data,
     * analyzes the tickets, and handles any IOExceptions that occur during the process.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args)
    {
        try
        {
            JsonArray ticketsArray = readJsonFile("tickets.json");
            analyzeTickets(ticketsArray);
        }
        catch (IOException e)
        {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        }
    }

    /**
     * Reads the JSON file from the resources folder and parses it into a JsonArray.
     *
     * @param filePath The path to the JSON file in the resources folder.
     * @return A JsonArray containing the ticket data.
     * @throws IOException If an error occurs while reading the file.
     */
    private static JsonArray readJsonFile(String filePath) throws IOException {
        InputStream inputStream = TicketAnalyzer.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        return jsonObject.getAsJsonArray("tickets");
    }

    /**
     * Analyzes the tickets to group them by carrier, calculate prices, and print the results.
     *
     * @param ticketsArray A JsonArray containing the ticket data.
     */
    private static void analyzeTickets(JsonArray ticketsArray)
    {
        Map<String, List<Ticket>> carrierTicketsMap = new HashMap<>();
        List<Integer> prices = new ArrayList<>();

        for (var jsonElement : ticketsArray)
        {
            JsonObject ticketJson = jsonElement.getAsJsonObject();
            Ticket ticket = parseTicket(ticketJson);

            if (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV"))
            {
                carrierTicketsMap.computeIfAbsent(ticket.getCarrier(), k -> new ArrayList<>()).add(ticket);
                prices.add(ticket.getPrice());
            }
        }

        printMinimumFlightTimes(carrierTicketsMap);
        printPriceDifference(prices);
    }

    /**
     * Parses a JsonObject representing a ticket into a Ticket object.
     *
     * @param ticketJson A JsonObject containing the ticket data.
     * @return A Ticket object with the parsed data.
     */
    private static Ticket parseTicket(JsonObject ticketJson)
    {
        String origin = ticketJson.get("origin").getAsString();
        String destination = ticketJson.get("destination").getAsString();
        String carrier = ticketJson.get("carrier").getAsString();
        String departureDate = ticketJson.get("departure_date").getAsString();
        String departureTime = ticketJson.get("departure_time").getAsString();
        String arrivalDate = ticketJson.get("arrival_date").getAsString();
        String arrivalTime = ticketJson.get("arrival_time").getAsString();
        int price = ticketJson.get("price").getAsInt();

        LocalDateTime departureDateTime = LocalDateTime.parse(departureDate + " " + departureTime, DATE_TIME_FORMATTER);
        LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalDate + " " + arrivalTime, DATE_TIME_FORMATTER);
        long flightDurationMinutes = Duration.between(departureDateTime, arrivalDateTime).toMinutes();

        return new Ticket(origin, destination, carrier, price, flightDurationMinutes);
    }

    /**
     * Prints the minimum flight times for each carrier from Vladivostok to Tel-Aviv.
     *
     * @param carrierTicketsMap A map where the key is the carrier name and the value is a list of tickets for that carrier.
     */
    private static void printMinimumFlightTimes(Map<String, List<Ticket>> carrierTicketsMap)
    {
        System.out.println("Minimum flight times from Vladivostok to Tel-Aviv for each carrier:");

        for (var entry : carrierTicketsMap.entrySet())
        {
            String carrier = entry.getKey();
            List<Ticket> tickets = entry.getValue();
            long minFlightTime = tickets.stream().mapToLong(Ticket::getFlightDurationMinutes).min().orElse(0);
            System.out.printf("Carrier: %s, Minimum Flight Time: %d minutes%n", carrier, minFlightTime);
        }
    }

    /**
     * Prints the average price, median price, and the difference between the average and median prices.
     *
     * @param prices A list of ticket prices.
     */
    private static void printPriceDifference(List<Integer> prices)
    {
        if (prices.isEmpty())
        {
            System.out.println("No prices available for calculation.");
            return;
        }

        double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0);
        double medianPrice = calculateMedian(prices);

        System.out.printf("Average Price: %.2f%n", averagePrice);
        System.out.printf("Median Price: %.2f%n", medianPrice);
        System.out.printf("Difference between average price and median: %.2f%n", averagePrice - medianPrice);
    }

    /**
     * Calculates the median of a list of prices.
     *
     * @param prices A list of ticket prices.
     * @return The median price.
     */
    private static double calculateMedian(List<Integer> prices)
    {
        List<Integer> sortedPrices = prices.stream().sorted().toList();
        int size = sortedPrices.size();
        if (size % 2 == 0)
        {
            return (sortedPrices.get(size / 2 - 1) + sortedPrices.get(size / 2)) / 2.0;
        }
        else
        {
            return sortedPrices.get(size / 2);
        }
    }
}