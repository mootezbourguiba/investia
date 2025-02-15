package tn.esprit.investia.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class CryptoPrice {
    @Id
    private String id;
    private double usd;
    private double eur;

}
