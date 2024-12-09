package at.codecrafters.itext.letter;

import at.codecrafters.itext.letter.data.KundenInfoData;

import java.time.LocalDate;
import java.util.UUID;

public class LetterFactory {
    public static Object createData(LetterType letterType) {
        //noinspection SwitchStatementWithTooFewBranches,EnhancedSwitchMigration
        switch (letterType) {
            case KUNDEN_INFO:
                return new KundenInfoData("Martin Scheibelreiter", "Triester Straße 40 / 6 /504", "1100" , "Wien", "Österreich" , LocalDate.now(), UUID.randomUUID().toString());
            default:
                throw new UnsupportedOperationException("Unsupported LetterType: " + letterType);
        }
    }
}
