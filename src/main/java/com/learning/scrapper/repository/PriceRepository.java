package com.learning.scrapper.repository;

import com.learning.scrapper.config.AppConfig;
import com.learning.scrapper.domain.Price;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Repository
public class PriceRepository {

    private final String priceUrl;
    private final SqlRepository sqlRepository;
    private final DateTimeFormatter dateTimeFormatter;
    private static final String TBODY = "tbody";
    private static final String SPAN = "span";

    public PriceRepository(AppConfig appConfig, SqlRepository sqlRepository) {
        this.priceUrl = appConfig.getPriceUrl();
        this.sqlRepository = sqlRepository;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(appConfig.getDatePattern());
    }

    private Elements getPriceRows() throws IOException {
        Document doc = Jsoup.connect(priceUrl).get();
        Element mmtcPampElement = doc.select("._tabmmtc-pamp").get(0);
        Element priceTableBody = mmtcPampElement.getElementsByTag(TBODY).get(0);

        return priceTableBody.getElementsByTag("tr");
    }

    public String getCurrentPrice() throws IOException {
        log.info("Fetching realtime current price");

        Document document = Jsoup.connect(priceUrl).get();
        Element element = document.select("._gdtpw ._flx").get(0);
        String currentPrice = element.select("._gdprc").get(0).getElementsByTag(SPAN).get(0).text();

        return currentPrice;
    }

    public List<Price> getHistoricalPrices() throws IOException {
        log.info("Fetching last 30 days prices in realtime");

        return getPriceRows()
                .stream()
                .map(row -> row.getElementsByTag("td")
                        .stream()
                        .map(data -> data.select("td").text())
                        .collect(Collectors.toList()))
                .map(price -> Price.buildPriceFromList(price, dateTimeFormatter))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "prices")
    public List<Price> getSavedHistoricalPrices(int size) {
        log.info("Fetching saved historical prices");

        return StreamSupport
                .stream(sqlRepository.getHistoricalPrices(size).spliterator(), true)
                .sorted(Comparator.comparing(Price::getDate))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "prices")
    public void saveHistoricalPrices() throws IOException {
        log.info("Saving prices for last 30 days");
        sqlRepository.saveAll(getHistoricalPrices());
    }

    public Optional<Price> getSavedPriceByDate(String date) {
        return sqlRepository.getPriceByDate(date);
    }
}
