package pl.pioro.nbpproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pioro.nbpproject.client.Quotation;
import pl.pioro.nbpproject.client.Rate;
import pl.pioro.nbpproject.client.RestClient;
import pl.pioro.nbpproject.dto.QuotationDto;
import pl.pioro.nbpproject.dto.RateDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuotationServiceTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private QuotationService quotationService;

    private Quotation quotation;
    private Rate rate1;

    @BeforeEach
    void prepare() {
        rate1 = Rate.builder()
                .bid(BigDecimal.valueOf(3.4515))
                .ask(BigDecimal.valueOf(3.5251))
                .effectiveDate(LocalDate.of(2019, 12, 13))
                .no("1")
                .build();
        Rate rate2 = Rate.builder()
                .bid(BigDecimal.valueOf(3.6515))
                .ask(BigDecimal.valueOf(3.7568))
                .effectiveDate(LocalDate.of(2019, 12, 14))
                .no("2")
                .build();
        List<Rate> rates = List.of(rate1, rate2);
        quotation = Quotation.builder()
                .code("USD")
                .table("A")
                .rates(rates)
                .build();
    }

    @Test
    void shouldTakeQuotationsFromGivenDate() {
        //given
        when(restClient.getUSDQuotationList(any(), any())).thenReturn(quotation);

        //when
        QuotationDto quotationDto = quotationService.getQuotationsFromProvidedDateToNow(LocalDate.now().minusDays(1));

        //then
        assertThat(quotationDto.getCurrencyRate().size()).isEqualTo(2);
        assertThat(quotationDto.getCurrencyRate().get(0).getDifferenceInPrice()).isEqualTo(rate1.getBid().subtract(rate1.getAsk()));
    }

    @Test
    void shouldCountDifferenceInAskAndBidPriceBetweenDays() {
        //given
        RateDto rateDto1 = RateDto.builder()
                .bidPrice(BigDecimal.valueOf(3.6515))
                .askPrice(BigDecimal.valueOf(3.7568))
                .build();

        RateDto rateDto2 = RateDto.builder()
                .bidPrice(BigDecimal.valueOf(3.6515))
                .askPrice(BigDecimal.valueOf(3.7568))
                .build();
        List<RateDto> rates = List.of(rateDto1, rateDto2);

        //when
        List<RateDto> ratesWithPrice = quotationService.countDifferenceInPriceBetweenDays(rates);

        //then
        assertThat(ratesWithPrice.get(1).getDifferenceFromPreviousAsk()).isEqualTo(rateDto2.getAskPrice().subtract(rateDto2.getAskPrice()));
        assertThat(ratesWithPrice.get(1).getDifferenceFromPreviousBid()).isEqualTo(rateDto2.getBidPrice().subtract(rateDto1.getBidPrice()));
        assertThat(ratesWithPrice.get(0).getDifferenceFromPreviousAsk()).isEqualTo(BigDecimal.ZERO);
        assertThat(ratesWithPrice.get(0).getDifferenceFromPreviousBid()).isEqualTo(BigDecimal.ZERO);

    }
}
