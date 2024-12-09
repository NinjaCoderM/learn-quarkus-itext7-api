package at.codecrafters.itext.letter.data;

import java.time.LocalDate;

public record KundenInfoData(String recipientName, String street, String postalCode, String city, String country,
                             LocalDate erstellungsDatum, String docId) {

    public String[] getRecipientAddressLines() {
        return new String[]{
                recipientName,
                street,
                postalCode + " " + city,
                country
        };
    }
}
