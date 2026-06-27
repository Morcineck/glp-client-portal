package com.glp.client_portal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse  {

    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private String path;
    private List<FieldErrorDetail> erros;

    @Data
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String campo;
        private String mensagem;
    }

}
