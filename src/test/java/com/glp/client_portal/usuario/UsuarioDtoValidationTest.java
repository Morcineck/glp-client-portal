package com.glp.client_portal.usuario;

import com.glp.client_portal.usuario.dto.AlterarSenhaRequest;
import com.glp.client_portal.usuario.dto.CriarUsuarioRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveRejeitarSenhaCurtaAoCriarUsuario() {
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "novo@glp.com",
                "1234567",
                Role.ADMIN,
                null
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void deveAceitarSenhaComOitoCaracteresAoCriarUsuario() {
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "novo@glp.com",
                "12345678",
                Role.ADMIN,
                null
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void deveRejeitarNovaSenhaCurtaNaAlteracao() {
        AlterarSenhaRequest request = new AlterarSenhaRequest(
                "senha-antiga",
                "1234567"
        );

        assertFalse(validator.validate(request).isEmpty());
    }
}
