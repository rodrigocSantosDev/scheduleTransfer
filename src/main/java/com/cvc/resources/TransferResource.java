package com.cvc.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvc.model.RequestStatus;
import com.cvc.model.TransferModel;
import com.cvc.service.TransferService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/cvc", produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE})
public class TransferResource {


	private static final Logger LOGGER = LoggerFactory.getLogger(TransferResource.class);

	@Autowired
	private TransferService service;
	
	public TransferResource() {
		super();
	}
	
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "ransfer scheduling successful"),
		    @ApiResponse(code = 412, message = "any rule was not met"),
		    @ApiResponse(code = 500, message = "An exception was generated"),
		})
	@PostMapping(value = "/transfer", produces="application/json", consumes="application/json")
	public RequestStatus tranfer(@RequestBody TransferModel transferencia) {
		LOGGER.info("START PROCESSING SAVE TRANSFER");
		
		return service.transfer(transferencia);
	}
	
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "return transfer list"),
	    @ApiResponse(code = 403, message = "You do not have permission to access this feature."),
	    @ApiResponse(code = 500, message = "An exception was generated"),
	})
	@GetMapping(value = "/findAll", produces="application/json")
	public Iterable<TransferModel> findAll() {
		LOGGER.info("START PROCESSING FIND ALL TRANSFER");
		
		return service.findTransfer();
	}
	
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "return transfer list"),
		    @ApiResponse(code = 403, message = "You do not have permission to access this feature."),
		    @ApiResponse(code = 500, message = "An exception was generated"),
		})
	@GetMapping(value = "/findBySchedulingDate/{date}", produces="application/json")
	public Iterable<TransferModel> findBySchedulingDate(@PathVariable("date") String schedulingDate) {
		LOGGER.info("START PROCESSING FIND SCHEDULING DATE TRANSFER");
		
		return service.findBySchedulingDate(schedulingDate);
	}
	
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "return transfer list"),
		    @ApiResponse(code = 403, message = "You do not have permission to access this feature."),
		    @ApiResponse(code = 500, message = "An exception was generated"),
		})
	@GetMapping(value = "/findByTransferDate/{date}", produces="application/json")
	public Iterable<TransferModel> findByTransferDate(@PathVariable("date") String transferDate) {
		LOGGER.info("START PROCESSING FIND TRANSFER DATE TRANSFER");
		
		return service.findByTransferDate(transferDate);
	}
	

}
