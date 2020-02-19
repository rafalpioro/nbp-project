package pl.pioro.nbpproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.pioro.nbpproject.dto.QuotationDto;
import pl.pioro.nbpproject.service.QuotationService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @GetMapping("api/getQuotation/{from}")
    public QuotationDto getCurrency(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from) {
        return quotationService.getQuotationsFromProvidedDateToNow(from);
    }
}
