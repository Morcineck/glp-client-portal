package com.glp.client_portal.cliente;

import com.glp.client_portal.consumo.ConsumoMensal;
import com.glp.client_portal.consumo.ConsumoService;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoService;
import com.glp.client_portal.economia.Economia;
import com.glp.client_portal.economia.EconomiaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RelatorioService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ConsumoService consumoService;

    @Autowired
    private EconomiaService economiaService;

    public byte[] gerarRelatorioCliente(UUID clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        List<Contrato> contratos = contratoService.listarPorCliente(clienteId);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Fontes
            Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fonteSubtitulo = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            Font fonteNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fonteBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            // Cabeçalho
            document.add(new Paragraph("GLP Consultoria Energética", fonteTitulo));
            document.add(new Paragraph("Relatório do Cliente", fonteSubtitulo));
            document.add(new Paragraph("Data de geração: " + LocalDate.now(), fonteNormal));
            document.add(Chunk.NEWLINE);

            // Dados do cliente
            document.add(new Paragraph("Dados do Cliente", fonteSubtitulo));
            document.add(new Paragraph("Nome: " + cliente.getNome(), fonteNormal));
            document.add(new Paragraph("Email: " + cliente.getEmail(), fonteNormal));
            document.add(new Paragraph("Documento: " + cliente.getDocumento() + " (" + cliente.getTipoDocumento() + ")", fonteNormal));
            document.add(new Paragraph("Telefone: " + (cliente.getTelefone() != null ? cliente.getTelefone() : "-"), fonteNormal));
            document.add(Chunk.NEWLINE);

            // Contratos
            document.add(new Paragraph("Contratos", fonteSubtitulo));
            document.add(Chunk.NEWLINE);

            for (Contrato contrato : contratos) {
                document.add(new Paragraph("Contrato: " + contrato.getTipoContrato(), fonteBold));
                document.add(new Paragraph("Vigência: " + contrato.getDataInicio() + " até " + contrato.getDataFim(), fonteNormal));
                document.add(new Paragraph("Valor Mensal: R$ " + contrato.getValorMensal(), fonteNormal));
                document.add(Chunk.NEWLINE);

                // Consumo mensal
                List<ConsumoMensal> consumos = consumoService.listarPorContrato(contrato.getId());
                if (!consumos.isEmpty()) {
                    document.add(new Paragraph("Histórico de Consumo", fonteBold));

                    PdfPTable tabelaConsumo = new PdfPTable(3);
                    tabelaConsumo.setWidthPercentage(100);
                    tabelaConsumo.addCell(new PdfPCell(new Phrase("Mês", fonteBold)));
                    tabelaConsumo.addCell(new PdfPCell(new Phrase("kWh Consumido", fonteBold)));
                    tabelaConsumo.addCell(new PdfPCell(new Phrase("Custo Total", fonteBold)));

                    for (ConsumoMensal consumo : consumos) {
                        tabelaConsumo.addCell(new Phrase(consumo.getMesReferencia().toString(), fonteNormal));
                        tabelaConsumo.addCell(new Phrase(consumo.getKwhConsumido().toString(), fonteNormal));
                        tabelaConsumo.addCell(new Phrase("R$ " + consumo.getCustoTotal(), fonteNormal));
                    }
                    document.add(tabelaConsumo);
                    document.add(Chunk.NEWLINE);
                }

                // Economia
                List<Economia> economias = economiaService.listarPorContrato(contrato.getId());
                if (!economias.isEmpty()) {
                    document.add(new Paragraph("Histórico de Economia", fonteBold));

                    PdfPTable tabelaEconomia = new PdfPTable(4);
                    tabelaEconomia.setWidthPercentage(100);
                    tabelaEconomia.addCell(new PdfPCell(new Phrase("Mês", fonteBold)));
                    tabelaEconomia.addCell(new PdfPCell(new Phrase("Custo Antes", fonteBold)));
                    tabelaEconomia.addCell(new PdfPCell(new Phrase("Custo Depois", fonteBold)));
                    tabelaEconomia.addCell(new PdfPCell(new Phrase("Economia", fonteBold)));

                    for (Economia economia : economias) {
                        tabelaEconomia.addCell(new Phrase(economia.getMesReferencia().toString(), fonteNormal));
                        tabelaEconomia.addCell(new Phrase("R$ " + economia.getCustoAntes(), fonteNormal));
                        tabelaEconomia.addCell(new Phrase("R$ " + economia.getCustoDepois(), fonteNormal));
                        tabelaEconomia.addCell(new Phrase("R$ " + economia.getEconomiaGerada(), fonteNormal));
                    }
                    document.add(tabelaEconomia);
                    document.add(Chunk.NEWLINE);
                }
            }

            // Total economizado
            document.add(new Paragraph("Total Economizado: R$ " +
                    economiaService.totalEconomizadoPorCliente(clienteId), fonteBold));

            document.close();
            return outputStream.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }
    }
}