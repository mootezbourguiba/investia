package tn.esprit.investia.repository;


import tn.esprit.investia.model.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, String> {



}
