package com.glp.client_portal.cliente;

import com.glp.client_portal.exception.IllegalArgumentBusinessException;

public final class DocumentoFiscalValidator {

    private DocumentoFiscalValidator() {
    }

    public static String validarENormalizar(String documento, TipoDocumento tipoDocumento) {
        String digits = documento == null ? "" : documento.replaceAll("\\D", "");

        boolean valido = switch (tipoDocumento) {
            case CPF -> validarCpf(digits);
            case CNPJ -> validarCnpj(digits);
        };

        if (!valido) {
            throw new IllegalArgumentBusinessException(
                    tipoDocumento + " inválido"
            );
        }

        return digits;
    }

    static boolean validarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || todosDigitosIguais(cpf)) {
            return false;
        }

        int primeiro = calcularDigito(cpf.substring(0, 9), 10);
        int segundo = calcularDigito(cpf.substring(0, 9) + primeiro, 11);

        return cpf.charAt(9) - '0' == primeiro
                && cpf.charAt(10) - '0' == segundo;
    }

    static boolean validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || todosDigitosIguais(cnpj)) {
            return false;
        }

        int[] pesosPrimeiro = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosSegundo = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int primeiro = calcularDigitoCnpj(cnpj.substring(0, 12), pesosPrimeiro);
        int segundo = calcularDigitoCnpj(cnpj.substring(0, 12) + primeiro, pesosSegundo);

        return cnpj.charAt(12) - '0' == primeiro
                && cnpj.charAt(13) - '0' == segundo;
    }

    private static int calcularDigito(String base, int pesoInicial) {
        int soma = 0;

        for (int i = 0; i < base.length(); i++) {
            soma += (base.charAt(i) - '0') * (pesoInicial - i);
        }

        int resto = 11 - (soma % 11);
        return resto >= 10 ? 0 : resto;
    }

    private static int calcularDigitoCnpj(String base, int[] pesos) {
        int soma = 0;

        for (int i = 0; i < base.length(); i++) {
            soma += (base.charAt(i) - '0') * pesos[i];
        }

        int resto = 11 - (soma % 11);
        return resto >= 10 ? 0 : resto;
    }

    private static boolean todosDigitosIguais(String value) {
        return value.chars().distinct().count() == 1;
    }
}
