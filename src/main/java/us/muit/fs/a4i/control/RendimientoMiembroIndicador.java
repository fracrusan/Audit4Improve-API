package us.muit.fs.a4i.control;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class RendimientoMiembroIndicador implements IndicatorStrategy<Double>{

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// M�tricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("createdIssuesMes", "closedIssuesMes","assingedDevIssuesMes", "closedDevIssuesMes");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
		// Se obtienen y se comprueba que se pasan las m�tricas necesarias para calcular
		// el indicador.
		Optional<ReportItemI<Double>>  createdIssuesMes = metrics.stream().filter(m -> REQUIRED_METRICS.get(0).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>>  closedIssuesMes = metrics.stream().filter(m -> REQUIRED_METRICS.get(1).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>>  assignedDevIssuesMes = metrics.stream().filter(m -> REQUIRED_METRICS.get(2).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>>  closedDevIssuesMes = metrics.stream().filter(m -> REQUIRED_METRICS.get(3).equals(m.getName())).findAny();
				ReportItemI<Double> indicatorReport = null;

		if (createdIssuesMes.isPresent() && closedIssuesMes.isPresent() && assignedDevIssuesMes.isPresent() && closedDevIssuesMes.isPresent()) {
			Double rendimientoMiembro;

			// Se realiza el c�lculo del indicador
			if(createdIssuesMes.get().getValue()!=0 && assignedDevIssuesMes.get().getValue()!=0 && closedDevIssuesMes.get().getValue()!=0) 
				rendimientoMiembro = (closedIssuesMes.get().getValue()/createdIssuesMes.get().getValue())/(closedDevIssuesMes.get().getValue()/assignedDevIssuesMes.get().getValue());
			else if(closedDevIssuesMes.get().getValue()!=0)
				rendimientoMiembro = 1.0;
			else
				rendimientoMiembro=0.0;

			try {
				// Se crea el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("rendimientoMiembro", rendimientoMiembro)
						.metrics(Arrays.asList(createdIssuesMes.get(), closedIssuesMes.get(), assignedDevIssuesMes.get(), closedDevIssuesMes.get()))
						.indicator(IndicatorState.UNDEFINED).build();
			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder.");
				e.printStackTrace();
			}

		} else {
			log.info("No se han proporcionado las m�tricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		return  indicatorReport;
	}

	@Override
	public List<String> requiredMetrics() {
		// Para calcular el indicador "rendimientoMiembro", ser�n necesarias las m�tricas
		// "openIssues" y "closedIssues".
		return REQUIRED_METRICS;
	}
}
