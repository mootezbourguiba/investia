package tn.esprit.investia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class CryptoPrice {

    @Id
    private String id; // Exemple: BTC-USD-2024-03-20T14:30:00

    private String cryptoSymbol; // Exemple: BTC
    private String currency;      // Exemple: USD
    private LocalDateTime timestamp;
    private Double price;

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCryptoSymbol() { return cryptoSymbol; }
    public void setCryptoSymbol(String cryptoSymbol) { this.cryptoSymbol = cryptoSymbol; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}