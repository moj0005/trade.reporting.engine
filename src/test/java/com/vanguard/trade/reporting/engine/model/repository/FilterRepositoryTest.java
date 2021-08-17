package com.vanguard.trade.reporting.engine.model.repository;

import com.vanguard.trade.reporting.engine.model.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest(properties = {"spring.datasource.url=jdbc:h2:mem:CustomerStore", "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa", "spring.datasource.password=password"})
class FilterRepositoryTest {

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {

        Filter filter1 = new Filter("buyer_party", "//buyerPartyReference/@href");
        filter1.id = 1L;
        entityManager.persist(filter1);

        Filter filter2 = new Filter("seller_party", "//sellerPartyReference/@href");
        filter1.id = 2L;
        entityManager.persist(filter2);
    }

    void getAllFilterNamesTest() {

        List<String> filterNamesActual = filterRepository.getAllFilterNames();

        String expected[] = {"buyer_party", "seller_party"};


        assertEquals(expected, Arrays.asList(filterNamesActual));

    }

}