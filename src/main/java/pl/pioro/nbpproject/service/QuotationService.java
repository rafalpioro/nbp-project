package pl.pioro.nbpproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pioro.nbpproject.client.Quotation;
import pl.pioro.nbpproject.client.Rate;
import pl.pioro.nbpproject.client.RestClient;
import pl.pioro.nbpproject.dto.QuotationDto;
import pl.pioro.nbpproject.dto.RateDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final RestClient restClient;

    public QuotationDto getQuotationsFromProvidedDateToNow(LocalDate from) {

        if(from.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date can not be in future");
        }

        Quotation currencyList = restClient.getUSDQuotationList(from, LocalDate.now());

        List<RateDto> rates = currencyList.getRates().stream()
                .map(this::mapToRateDto)
                .collect(Collectors.toList());

        List<RateDto> ratesWithDifferenceInPrices = countDifferenceInPriceBetweenDays(rates);
        return mapToQuotationDto(currencyList, ratesWithDifferenceInPrices);

    }

    public List<RateDto> countDifferenceInPriceBetweenDays(List<RateDto> list) {

        int size = list.size();

        while (size > 1) {
            RateDto rateDto = list.get(size - 1);
            RateDto rateDto1 = list.get(size - 2);

            rateDto.setDifferenceFromPreviousAsk(rateDto1.getAskPrice().subtract(rateDto.getAskPrice()));
            rateDto.setDifferenceFromPreviousBid(rateDto1.getBidPrice().subtract(rateDto.getBidPrice()));
            size--;
        }
        list.get(0).setDifferenceFromPreviousAsk(BigDecimal.ZERO);
        list.get(0).setDifferenceFromPreviousBid(BigDecimal.ZERO);
        return list;
    }

    private RateDto mapToRateDto(Rate rate) {
        return RateDto.builder()
                .askPrice(rate.getAsk())
                .bidPrice(rate.getBid())
                .date(rate.getEffectiveDate())
                .differenceInPrice(rate.getBid().subtract(rate.getAsk()))
                .build();
    }

    private QuotationDto mapToQuotationDto(Quotation quotation, List<RateDto> rates) {
        return QuotationDto.builder()
                .currencyCode(quotation.getCode())
                .currencyName(quotation.getCurrency())
                .currencyRate(rates)
                .build();
    }
}
