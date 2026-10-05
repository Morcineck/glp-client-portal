package com.glp.client_portal.financeiro;

import com.glp.client_portal.financeiro.dto.CriarMensalidadeRequest;
import com.glp.client_portal.financeiro.dto.FinanceiroResumoResponse;
import com.glp.client_portal.financeiro.dto.RegistrarPagamentoRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/financeiro")
public class FinanceiroController {

    private final MensalidadeService mensalidadeService;

    public FinanceiroController(MensalidadeService mensalidadeService) {
        this.mensalidadeService = mensalidadeService;
    }

    @PostMapping("/contratos/{contratoId}/mensalidades")
    public ResponseEntity<Mensalidade> criar(
            @PathVariable UUID contratoId,
            @Valid @RequestBody CriarMensalidadeRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mensalidadeService.criar(contratoId, request));
    }

    @GetMapping("/mensalidades")
    public ResponseEntity<List<Mensalidade>> listar() {
        return ResponseEntity.ok(mensalidadeService.listar());
    }

    @PatchMapping("/mensalidades/{mensalidadeId}/pagar")
    public ResponseEntity<Mensalidade> registrarPagamento(
            @PathVariable UUID mensalidadeId,
            @RequestBody RegistrarPagamentoRequest request
    ) {
        return ResponseEntity.ok(
                mensalidadeService.registrarPagamento(mensalidadeId, request)
        );
    }

    @GetMapping("/resumo")
    public ResponseEntity<FinanceiroResumoResponse> resumo() {
        return ResponseEntity.ok(mensalidadeService.resumo());
    }
}
