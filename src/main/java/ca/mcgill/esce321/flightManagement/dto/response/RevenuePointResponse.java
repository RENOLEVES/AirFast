
package ca.mcgill.esce321.flightManagement.dto.response;

import java.time.LocalDate;

public record RevenuePointResponse(
        LocalDate date,
        double cumulativeRevenue
) {}