package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.CompraStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PaypalStatus {
    SUCESSO(CompraStatus.SUCESSO, 1),
    ERRO(CompraStatus.FALHA, 0);

    private final CompraStatus status;
    private final int code;

    PaypalStatus(CompraStatus status, int code) {
        this.status = status;
        this.code = code;
    }

    @JsonValue
    public int toValue() { return code; }

    public static PaypalStatus fromCode(int code) {
        return Arrays.stream(PaypalStatus.values())
                .filter(i -> { return i.getCode() == code; })
                .findFirst().orElse(null);
    }

    @JsonCreator
    public static PaypalStatus receive(int value) {
        return PaypalStatus.fromCode(value);
    }

    public CompraStatus getStatus() { return status; }

    public int getCode() {
        return code;
    }
}
