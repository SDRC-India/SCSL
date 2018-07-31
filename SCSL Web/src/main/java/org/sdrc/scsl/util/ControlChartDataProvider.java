package org.sdrc.scsl.util;

import java.text.DecimalFormat;

import org.sdrc.scsl.model.web.ControlChartModel;
import org.springframework.stereotype.Component;

/** 
* @author Sarita Panigrahi, created on: 11-Oct-2017
* This class provides control chart data i.e. 
* P-Average
* SD-Standard Deviation
* UCL-upper control limit
* LCL-Lower control limit
* Fractional index
*/
@Component
public class ControlChartDataProvider {
	
	private static DecimalFormat df = new DecimalFormat("#.##");

	//pAverage gets calculated directly from query
	public static ControlChartModel getControlChartData(Long numeratorVal, Long denominatorVal, Double pAverage) {
		ControlChartModel controlChartModel = new ControlChartModel();
		
		// as per the formula (=SQRT((PAverage*(1-PAverage))/Denominator))
		if(denominatorVal!=null && numeratorVal!=null){
			controlChartModel.setStandardDeviation(denominatorVal!=0? Math.sqrt((pAverage * (1 - pAverage)) / denominatorVal) : 0 ); //check infinity
			controlChartModel.setFractionalIndex(denominatorVal!=0? Double.valueOf(df.format((double) numeratorVal / (double) denominatorVal)) : 0);
		}
		// Formula : PAverage + 3* StandardDeviation
		if(controlChartModel.getStandardDeviation()!=null && pAverage!=null){
			controlChartModel.setUcl(!controlChartModel.getStandardDeviation().isNaN() ?
					Double.valueOf(df.format(pAverage + (3 * controlChartModel.getStandardDeviation()))) : 0.0);
			// Formula : PAverage - 3* StandardDeviation
			controlChartModel.setLcl(!controlChartModel.getStandardDeviation().isNaN() ?
					Double.valueOf(df.format(pAverage - (3 * controlChartModel.getStandardDeviation()))): 0.0);
		}
		
		
		return controlChartModel;
	}
	
}
