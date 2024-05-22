package org.mrp.mrp.enums.errormessages;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum ErrorMessage {
    HTTP_MESSAGE_NOT_READABLE_MESSAGE_EN("Message not readable", "Wrong request format", "en", ErrorMessageCategory.HTTP_MESSAGE_NOT_READABLE),
    HTTP_MESSAGE_NOT_READABLE_MESSAGE_LT("Žinutė neperskaitoma", "Neteisingas prašymo formatas", "lt", ErrorMessageCategory.HTTP_MESSAGE_NOT_READABLE),
    TYPE_MISMATCH_MESSAGE_EN("Type mismatch", "Incorect request type", "en", ErrorMessageCategory.TYPE_MISMATCH),
    TYPE_MISMATCH_MESSAGE_LT("Neteisingas tipas", "Neteisingas prašymo tipas", "lt", ErrorMessageCategory.TYPE_MISMATCH),
    ENTITY_NOT_FOUND_MESSAGE_EN("Entity not found", "Requested entity does not exist", "en", ErrorMessageCategory.ENTITY_NOT_FOUND),
    ENTITY_NOT_FOUND_MESSAGE_LT("Duomuo nerastas", "Duomuo neegzistuoja", "lt", ErrorMessageCategory.ENTITY_NOT_FOUND),
    METHOD_ARGUMENT_NOT_VALID_MESSAGE_EN("Method argument not valid", "Method has failed validation", "en", ErrorMessageCategory.METHOD_ARGUMENT_NOT_VALID),
    METHOD_ARGUMENT_NOT_VALID_MESSAGE_LT("Metodo argumentas neteisingas", "Nepavyko patvirtinti objekto", "lt", ErrorMessageCategory.METHOD_ARGUMENT_NOT_VALID),
    SQL_EXCEPTION_MESSAGE_EN("SQL exception", "Database connection error", "en", ErrorMessageCategory.SQL_EXCEPTION),
    SQL_EXCEPTION_MESSAGE_LT("SQL klaida", "Duomenų bazės klaida", "lt", ErrorMessageCategory.SQL_EXCEPTION),
    NUMBER_FORMAT_EXCEPTION_MESSAGE_EN("Number format exception", "Unable to process data in format provided", "en", ErrorMessageCategory.NUMBER_FORMAT_EXCEPTION),
    NUMBER_FORMAT_EXCEPTION_MESSAGE_LT("Skaičiaus formato klaida", "Nepavyko apdoroti duomenų paduotu formatu", "lt", ErrorMessageCategory.NUMBER_FORMAT_EXCEPTION),
    UNSPECIFIED_EXCEPTION_MESSAGE_EN("Unspecified exception", "Unable to specify exception", "en", ErrorMessageCategory.UNSPECIFIED_EXCEPTION),
    UNSPECIFIED_EXCEPTION_MESSAGE_LT("Nenurodyta klaida", "Nepavyko nurodyti klaidos", "lt", ErrorMessageCategory.UNSPECIFIED_EXCEPTION);

    private final String message;
    private final String error;
    private final String locale;
    private final ErrorMessageCategory errorMessageCategory;

    ErrorMessage(String message, String error, String locale, ErrorMessageCategory errorMessageCategory) {
        this.errorMessageCategory = errorMessageCategory;
        this.message = message;
        this.error = error;
        this.locale = locale;
    }

    public static ErrorMessage getErrorMessage(Locale locale, ErrorMessageCategory errorMessageCategory) {
        for (ErrorMessage errorMessage : ErrorMessage.values()) {
            if(errorMessage.errorMessageCategory.equals(errorMessageCategory) && errorMessage.locale.equals(locale.toString())) {
                return errorMessage;
            }
        }
        return ErrorMessage.getErrorMessage(Locale.ENGLISH, errorMessageCategory);
    }
}
