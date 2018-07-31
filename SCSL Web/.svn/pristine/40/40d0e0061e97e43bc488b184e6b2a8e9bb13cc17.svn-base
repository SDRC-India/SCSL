package org.sdrc.scsl.web.controller;

import org.sdrc.scsl.service.AggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) 
 * this controller will hold the mappings for time wise aggregation
 */
@Controller
public class AggregationController {

	@Autowired
	private AggregationService aggregationService;

	@RequestMapping("aggregateData")
	public @ResponseBody String persistAggregateData(@RequestParam("aggregationFrequency") String aggregationFrequency,
			@RequestParam( value="tpid", required=false) Integer tpid) {
		aggregationService.aggregateSNCUData(aggregationFrequency, tpid);
		return "true";
	}
	
//	@RequestMapping("aggregateData")
//	public @ResponseBody String persistAggregateData(@RequestParam("aggregationFrequency") String aggregationFrequency) {
//		aggregationService.aggregateSNCUData(aggregationFrequency);
//		return "true";
//	}
	
	//comment this before deploying 
	/*@RequestMapping("createAggregateQuartelyAndYearlyTps")
	public @ResponseBody String createAggregateQuartelyAndYearlyTps() {
		return aggregationService.createAggregateQuartelyAndYearlyTps();
	}

//	comment this before deploying
	@RequestMapping("aggregateMonthly")
	public @ResponseBody String aggregateMonthly() {
		return aggregationService.aggregateMonthly();
	}*/
}
