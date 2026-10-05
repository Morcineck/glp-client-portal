package com.glp.client_portal.dashboard;

import com.glp.client_portal.cliente.ClienteRepository;
import com.glp.client_portal.consumo.ConsumoMensal;
import com.glp.client_portal.consumo.ConsumoRepository;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.dashboard.dto.DashboardResponse;
import com.glp.client_portal.economia.Economia;
import com.glp.client_portal.economia.EconomiaRepository;
import com.glp.client_portal.usuario.UsuarioRepository;
import com.glp.client_portal.usuario.auth.security.AccessValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final ContratoRepository contratoRepository;
    private final ConsumoRepository consumoRepository;
    private final EconomiaRepository economiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AccessValidator accessValidator;

    public DashboardService(
            ClienteRepository clienteRepository,
            ContratoRepository contratoRepository,
            ConsumoRepository consumoRepository,
            EconomiaRepository economiaRepository,
            UsuarioRepository usuarioRepository,
            AccessValidator accessValidator
    ) {
        this.clienteRepository = clienteRepository;
        this.contratoRepository = contratoRepository;
        this.consumoRepository = consumoRepository;
        this.economiaRepository = economiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.accessValidator = accessValidator;
    }

    public DashboardResponse buscarResumo(UserDetails userDetails) {
        UUID clienteId = accessValidator.getClienteIdUsuario(userDetails);

        if (clienteId == null) {
            return buscarResumoAdministrador();
        }

        return buscarResumoCliente(clienteId);
    }

    private DashboardResponse buscarResumoAdministrador() {
        BigDecimal consumoTotal = somarConsumo(consumoRepository.findAll());
        BigDecimal economiaTotal = somarEconomia(economiaRepository.findAll());

        return new DashboardResponse(
                clienteRepository.count(),
                contratoRepository.count(),
                usuarioRepository.count(),
                consumoTotal,
                economiaTotal
        );
    }

    private DashboardResponse buscarResumoCliente(UUID clienteId) {
        List<Contrato> contratos = contratoRepository.findByClienteId(clienteId);
        List<ConsumoMensal> consumos = contratos.stream()
                .flatMap(contrato -> consumoRepository.findByContratoId(contrato.getId()).stream())
                .toList();

        BigDecimal consumoTotal = somarConsumo(consumos);
        BigDecimal economiaTotal = somarEconomia(economiaRepository.findByContratoClienteId(clienteId));

        return new DashboardResponse(
                1L,
                contratos.size(),
                0L,
                consumoTotal,
                economiaTotal
        );
    }

    private BigDecimal somarConsumo(List<ConsumoMensal> consumos) {
        return consumos.stream()
                .map(ConsumoMensal::getKwhConsumido)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarEconomia(List<Economia> economias) {
        return economias.stream()
                .map(Economia::getEconomiaGerada)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
