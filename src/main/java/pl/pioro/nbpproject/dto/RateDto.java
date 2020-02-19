package pl.pioro.nbpproject.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
public class RateDto {
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private LocalDate date;
    private BigDecimal differenceInPrice;
    private BigDecimal differenceFromPreviousBid;
    private BigDecimal differenceFromPreviousAsk;
}
