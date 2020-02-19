package pl.pioro.nbpproject.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;
}
