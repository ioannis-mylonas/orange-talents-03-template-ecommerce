package bootcamp.mercado.config.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;

public class FieldErrorResponse {
    private String field;
    private String message;

    public FieldErrorResponse(FieldError fieldError, MessageSource messageSource) {
        this.field = fieldError.getField();
        this.message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
