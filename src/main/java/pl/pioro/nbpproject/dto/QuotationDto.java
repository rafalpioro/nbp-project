package pl.pioro.nbpproject.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder(toBuilder = true)
@Data
public class QuotationDto {
    private String currencyName;
    private String currencyCode;
    private List<RateDto> currencyRate;
}
