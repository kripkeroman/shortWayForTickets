package org.crptApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    String origin;
    String destination;
    String carrier;
    int price;
    long flightDurationMinutes;
}
