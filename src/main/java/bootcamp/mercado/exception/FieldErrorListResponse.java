package bootcamp.mercado.exception;

import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class FieldErrorListResponse {
    List<FieldErrorResponse> errors;

    public FieldErrorListResponse(List<FieldError> errors,
                                  MessageSource messageSource) {

        this.errors = errors.stream()
                .map(i -> { return new FieldErrorResponse(i, messageSource); })
                .collect(Collectors.toList());
    }

    public List<FieldErrorResponse> getErrors() {
        return errors;
    }

}
