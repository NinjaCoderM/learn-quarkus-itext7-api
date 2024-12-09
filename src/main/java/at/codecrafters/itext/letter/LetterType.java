package at.codecrafters.itext.letter;

import at.codecrafters.itext.documents.KundenInfoPdf;
import at.codecrafters.itext.letter.data.KundenInfoData;

public enum LetterType {
    KUNDEN_INFO(KundenInfoData.class);

    private final Class<?> dataClass;

    LetterType(Class<?> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<?> getDataClass() {
        return dataClass;
    }
}