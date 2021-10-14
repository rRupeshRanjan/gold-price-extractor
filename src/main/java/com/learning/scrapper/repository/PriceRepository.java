package com.learning.scrapper.repository;

import com.learning.scrapper.config.AppConfig;
import com.learning.scrapper.domain.Price;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class PriceRepository {
    private final String priceUrl;
    private final SqlRepository sqlRepository;
    private final DateTimeFormatter dateTimeFormatter;

    public PriceRepository(AppConfig appConfig, SqlRepository sqlRepository) {
        this.priceUrl = appConfig.getPriceUrl();
        this.sqlRepository = sqlRepository;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(appConfig.getDatePattern());
    }

    @Cacheable
    private Elements getPriceRows() throws IOException {
        Document doc = Jsoup.connect(priceUrl).get();
        Element mmtcPampElement = doc.select("._tabmmtc-pamp").get(0);
        Element priceTableBody = mmtcPampElement.getElementsByTag("tbody").get(0);

        return priceTableBody.getElementsByTag("tr");
    }

    public double getCurrentPrice() throws IOException {
        Document document = Jsoup.connect(priceUrl).get();
        Element element = document.select("._gdtpw ._flx").get(0);
        String currentPrice = element.select("._gdprc").get(0).getElementsByTag("span").get(0).text();

        return Double.parseDouble(
                currentPrice.substring(1, currentPrice.indexOf("/"))
                        .replaceAll(",", ""));
    }

    public List<Price> getSavedHistoricalPrices() {
        return StreamSupport
                .stream(sqlRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    public Price getSavedPriceByDate(String date) {
        return sqlRepository.getPriceByDate(date);
    }

    public List<Price> getHistoricalPrices() throws IOException {
        return getPriceRows()
                .stream()
                .map(row -> row.getElementsByTag("td")
                        .stream()
                        .map(data -> data.select("td").text())
                        .collect(Collectors.toList()))
                .map(price -> Price.buildPriceFromList(price, dateTimeFormatter))
                .collect(Collectors.toList());
    }

    public Optional<Price> getPriceByDate(String date) throws IOException {
        return getHistoricalPrices()
                .stream()
                .filter(price -> dateTimeFormatter.format(price.getDate()).equals(date))
                .findFirst();
    }

    public void saveHistoricalPrices() throws IOException {
        sqlRepository.saveAll(getHistoricalPrices());
    }

    public void savePriceByDate(String date) throws IOException {
        getPriceByDate(date).ifPresent(sqlRepository::save);
    }
}
