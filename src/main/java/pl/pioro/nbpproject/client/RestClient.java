package pl.pioro.nbpproject.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RestClient {

    private final RestTemplate restTemplate;

    @Value("${nbp.api.url}")
    private String apiUrl;

    public Quotation getUSDQuotationList(LocalDate from, LocalDate to) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = String.format(apiUrl +"%s/%s", from.toString(), to.toString());

        return restTemplate.exchange(url, HttpMethod.GET, entity, Quotation.class).getBody();
    }
}
