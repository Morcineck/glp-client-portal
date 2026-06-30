package com.glp.client_portal.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Captura erros de IDs não encontrados (Status 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Captura erros de validação de campos (@Valid) (Status 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                   HttpServletRequest request) {
        List<ErrorResponse.FieldErrorDetail> detalheDosErros = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> new ErrorResponse.FieldErrorDetail(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dados de requisição inválidos",
                "Houve erros de validação nos campos preenchidos",
                request.getRequestURI(), detalheDosErros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Captura erros de leitura de JSON (ex: enviar texto em campo numérico/UUID) (Status 400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                               HttpServletRequest request) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Corpo de requisição malformado",
                "O formato de algum campo enviado está inválido ou ilegível",
                request.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 500 - qualquer erro inesperado
    @ExceptionHandler(IllegalArgumentBusinessException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentBusinessException ex, HttpServletRequest request) {

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de negócio violada",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Captura QUALQUER outro erro inesperado (NullPointer, banco fora, etc.) (Status 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex,
                                                               HttpServletRequest request) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor",
                "Ocorreu um erro inesperado no nosso sistema. Por favor, tente novamente mais tarde.",
                request.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

}
