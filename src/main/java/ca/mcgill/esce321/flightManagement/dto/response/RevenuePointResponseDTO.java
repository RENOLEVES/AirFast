
package ca.mcgill.esce321.flightManagement.dto.response;

import java.time.LocalDate;

public record RevenuePointResponseDTO(
        LocalDate date,
        double cumulativeRevenue
) {}