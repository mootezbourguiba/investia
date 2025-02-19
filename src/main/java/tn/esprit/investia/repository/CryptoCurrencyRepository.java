package tn.esprit.investia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.investia.model.CryptoCurrency;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {
    boolean existsByName(String name);
}