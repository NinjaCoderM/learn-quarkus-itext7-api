package at.codecrafters.itext.letter;

import at.codecrafters.itext.letter.data.KundenInfoData;

public enum LetterType {
    KUNDEN_INFO(KundenInfoData.class);

    private final Class<?> dataClass;

    LetterType(@SuppressWarnings("SameParameterValue") Class<?> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<?> getDataClass() {
        return dataClass;
    }
}