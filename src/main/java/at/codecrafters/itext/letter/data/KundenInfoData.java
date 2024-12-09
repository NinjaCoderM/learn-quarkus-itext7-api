package at.codecrafters.itext.letter.data;

import java.time.LocalDate;

public class KundenInfoData {
    private String recipientName;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private LocalDate erstellungsDatum;
    private String docId;

    public KundenInfoData(String recipientName, String street, String postalCode, String city, String country, LocalDate erstellungsDatum, String docId) {
        this.recipientName = recipientName;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.erstellungsDatum = erstellungsDatum;
        this.docId = docId;
    }


    public String getRecipientName() {
        return recipientName;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getErstellungsDatum() {
        return erstellungsDatum;
    }

    public String getDocId() {
        return docId;
    }

    public String[] getRecipientAddressLines() {
        return new String[]{
                recipientName,
                street,
                postalCode + " " + city,
                country
        };
    }
}
