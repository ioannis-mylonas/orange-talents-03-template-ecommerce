package bootcamp.mercado.exception;

import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class FieldErrorListResponse {
    List<ObjectErrorResponse> objectErrors;
    List<FieldErrorResponse> fieldErrors;

    public FieldErrorListResponse(List<FieldError> fieldErrors,
                                  List<ObjectError> objectErrors,
                                  MessageSource messageSource) {

        this.fieldErrors = fieldErrors.stream()
                .map(i -> { return new FieldErrorResponse(i, messageSource); })
                .collect(Collectors.toList());

        /**
         * Adiciona apenas as mensagens de erros que não sejam
         * FieldError, como validações de classe ou método
         */
        this.objectErrors = objectErrors.stream()
                .filter(i -> { return !(i instanceof FieldError); })
                .map(i -> { return new ObjectErrorResponse(i, messageSource); })
                .collect(Collectors.toList());
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }

    public List<ObjectErrorResponse> getObjectErrors() {
        return objectErrors;
    }
}
