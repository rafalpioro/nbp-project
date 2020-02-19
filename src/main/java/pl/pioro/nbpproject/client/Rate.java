package pl.pioro.nbpproject.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    private String no;
    private LocalDate effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;
}
