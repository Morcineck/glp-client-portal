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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ConsumoRepository consumoRepository;

    @Mock
    private EconomiaRepository economiaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AccessValidator accessValidator;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void deveRetornarResumoGlobalParaAdministrador() {
        ConsumoMensal consumo = new ConsumoMensal();
        consumo.setKwhConsumido(new BigDecimal("150.50"));

        Economia economia = new Economia();
        economia.setEconomiaGerada(new BigDecimal("75.25"));

        when(accessValidator.getClienteIdUsuario(userDetails)).thenReturn(null);
        when(clienteRepository.count()).thenReturn(4L);
        when(contratoRepository.count()).thenReturn(6L);
        when(usuarioRepository.count()).thenReturn(5L);
        when(consumoRepository.findAll()).thenReturn(List.of(consumo));
        when(economiaRepository.findAll()).thenReturn(List.of(economia));

        DashboardResponse resposta = dashboardService.buscarResumo(userDetails);

        assertEquals(4L, resposta.totalClientes());
        assertEquals(6L, resposta.totalContratos());
        assertEquals(5L, resposta.totalUsuarios());
        assertEquals(new BigDecimal("150.50"), resposta.consumoTotalKwh());
        assertEquals(new BigDecimal("75.25"), resposta.economiaTotal());
    }

    @Test
    void deveRetornarSomenteDadosDoClienteAutenticado() {
        UUID clienteId = UUID.randomUUID();
        UUID contratoId = UUID.randomUUID();

        Contrato contrato = new Contrato();
        contrato.setId(contratoId);

        ConsumoMensal consumo = new ConsumoMensal();
        consumo.setKwhConsumido(new BigDecimal("90.00"));

        Economia economia = new Economia();
        economia.setEconomiaGerada(new BigDecimal("30.00"));

        when(accessValidator.getClienteIdUsuario(userDetails)).thenReturn(clienteId);
        when(contratoRepository.findByClienteId(clienteId)).thenReturn(List.of(contrato));
        when(consumoRepository.findByContratoId(contratoId)).thenReturn(List.of(consumo));
        when(economiaRepository.findByContratoClienteId(clienteId)).thenReturn(List.of(economia));

        DashboardResponse resposta = dashboardService.buscarResumo(userDetails);

        assertEquals(1L, resposta.totalClientes());
        assertEquals(1L, resposta.totalContratos());
        assertEquals(0L, resposta.totalUsuarios());
        assertEquals(new BigDecimal("90.00"), resposta.consumoTotalKwh());
        assertEquals(new BigDecimal("30.00"), resposta.economiaTotal());
    }
}
