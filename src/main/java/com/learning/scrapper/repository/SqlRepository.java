package com.learning.scrapper.repository;

import com.learning.scrapper.domain.Price;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SqlRepository extends CrudRepository<Price, Integer> {

    @Query(value = "SELECT * FROM price WHERE date=?1", nativeQuery = true)
    Optional<Price> getPriceByDate(String date);

    @Query(value = "SELECT * FROM price ORDER BY date DESC LIMIT ?1", nativeQuery = true)
    Iterable<Price> getHistoricalPrices(int size);
}
