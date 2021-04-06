package bootcamp.mercado.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.ObjectError;

public class ObjectErrorResponse {
    String message;

    public ObjectErrorResponse(ObjectError error, MessageSource messageSource) {
        this.message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }

    public String getMessage() {
        return message;
    }
}
