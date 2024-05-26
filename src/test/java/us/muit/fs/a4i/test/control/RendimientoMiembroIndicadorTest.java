package us.muit.fs.a4i.test.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import us.muit.fs.a4i.control.RendimientoMiembroIndicador;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class RendimientoMiembroIndicadorTest {

    private RendimientoMiembroIndicador indicador;

    @BeforeEach
    public void setUp() {
        indicador = new RendimientoMiembroIndicador();
    }

    @Test
    public void testCalcIndicator() throws ReportItemException, NotAvailableMetricException {
        // Crear métricas simuladas
        ReportItemI<Double> createdIssuesMes = new ReportItem.ReportItemBuilder<Double>("createdIssuesMes", 100.0).build();
        ReportItemI<Double> closedIssuesMes = new ReportItem.ReportItemBuilder<Double>("closedIssuesMes", 80.0).build();
        ReportItemI<Double> assignedDevIssuesMes = new ReportItem.ReportItemBuilder<Double>("assingedDevIssuesMes", 8.0).build();
        ReportItemI<Double> closedDevIssuesMes = new ReportItem.ReportItemBuilder<Double>("closedDevIssuesMes", 5.0).build();

        // Calcular el indicador
        List<ReportItemI<Double>> metrics = Arrays.asList(createdIssuesMes, closedIssuesMes, assignedDevIssuesMes, closedDevIssuesMes);
        ReportItemI<Double> result = indicador.calcIndicator(metrics);

        // Verificar el resultado
        Double expectedIndicator = (80.0 / 100.0) / (5.0 / 8.0);
        assertEquals(expectedIndicator, result.getValue());
    }

    @Test
    public void testCalcIndicatorWithMissingMetrics() {
        // Crear métricas simuladas incompletas
        ReportItemI<Double> createdIssuesMes = new ReportItem.ReportItemBuilder<Double>("createdIssuesMes", 100.0).build();
        ReportItemI<Double> closedIssuesMes = new ReportItem.ReportItemBuilder<Double>("closedIssuesMes", 80.0).build();

        // Calcular el indicador
        List<ReportItemI<Double>> metrics = Arrays.asList(createdIssuesMes, closedIssuesMes);

        // Verificar que se lanza una excepción
        assertThrows(NotAvailableMetricException.class, () -> {
            indicador.calcIndicator(metrics);
        });
    }
}

