package com.cvc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cvc.model.RequestStatus;
import com.cvc.model.TransferModel;
import com.cvc.repository.TransferRepository;
import com.cvc.util.Util;

@Service
public class TransferService {

	private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
	
	@Autowired
	private TransferRepository repository;
	
	public RequestStatus transfer(TransferModel transfer) {
		try {
			transfer.setSchedulingDate(LocalDate.now());
			RequestStatus resquestStatus = validateRules(transfer);
			
			if(resquestStatus != null) {
				return resquestStatus;
			}
			
			calculateRates(transfer);
			
			if (transfer.getRate() == null || transfer.getRate().compareTo(BigDecimal.ZERO) == 0) {
				logger.error("No rate were found for the transfer request made. Request date: {} - requisition data: {}", LocalDate.now(), transfer);
				return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), "An internal error has occurred, please try again later.");
			}
		
		} catch (DateTimeParseException e) {
			logger.error("Error in setting the date.", e);
			return new RequestStatus(HttpStatus.BAD_REQUEST.value(), "Error in setting the date. Expected format: yyyy-mm-dd");
		}
		repository.save(transfer);
		
		return new RequestStatus(HttpStatus.OK.value(), "Transfer Successfully. Rate charged from: "+ transfer.getRate().setScale(2,BigDecimal.ROUND_HALF_EVEN));
	}


	private RequestStatus validateRules(TransferModel transfer) {
		
		if(transfer.getValue().compareTo(new BigDecimal(0)) <= 0) {
			logger.info("Process closed, it is necessary to inform a valid value.");
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), "It is necessary to enter a valid value. Greater than zero");
		}
		
		RequestStatus requestStatus = validateAccountSize(transfer);
		
		if(requestStatus == null) {
			requestStatus = validateTransferDate(transfer);
		}
		
		return requestStatus;
	}
	
	private void calculateRates(TransferModel transfer) {
		LocalDate transferDate = Util.convertStringToLocalDate(transfer.getTransferDate()); 
		long differenceDays = ChronoUnit.DAYS.between(transfer.getSchedulingDate(), transferDate);
		
		if(transfer.getSchedulingDate().format(formatter).equals(transferDate.format(formatter))) {
			BigDecimal rate = new BigDecimal(3);
			calculateRateWithPercentageOnValue(transfer, rate, 3d);
		} else {
			calculareRatesPerDay(transfer, differenceDays);
		}
	}

	private void calculareRatesPerDay(TransferModel transfer, long differenceInDays) {
		if (differenceInDays <= 10) {
			transfer.setRate(new BigDecimal(12).multiply(new BigDecimal(differenceInDays)));
		} else if(differenceInDays > 10 && differenceInDays <= 20) {
			calculateRateWithPercentageOnValue(transfer, BigDecimal.ZERO, 8d);
		} else if(differenceInDays > 20 && differenceInDays <= 30) {
			calculateRateWithPercentageOnValue(transfer, BigDecimal.ZERO, 6d);
		} else if(differenceInDays > 30 && differenceInDays <= 40) {
			calculateRateWithPercentageOnValue(transfer, BigDecimal.ZERO, 4d);
		} else if(differenceInDays > 40 && transfer.getValue().compareTo(new BigDecimal(100000)) > 0) {
			calculateRateWithPercentageOnValue(transfer, BigDecimal.ZERO, 2d);
		}
	}

	private void calculateRateWithPercentageOnValue(TransferModel transfer, BigDecimal rate, double percentage) {
		percentage = percentage / 100d;
		transfer.setRate(transfer.getValue().multiply(BigDecimal.valueOf(percentage)).add(rate));
	}

	private RequestStatus validateTransferDate(TransferModel transfer) {
		LocalDate transferDate = Util.convertStringToLocalDate(transfer.getTransferDate());
		if(transfer.getTransferDate() == null) {
			logger.info("Process closed due to failure in the criteria in which the transfer date was not informed");
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), "Transfer date is mandatory");
		}else if(transferDate.isBefore(transfer.getSchedulingDate())) {
			logger.info("Process closed due to failure in criteria where the transfer date is before the current date");
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), "Transfer date cannot be earlier than current date");
		}
		return null;
	}

	private RequestStatus validateAccountSize(TransferModel transfer) {
		
		if(transfer.getOriginAccount().length() != 6 || transfer.getDestinationAccount().length() !=6) {
			logger.info("Process was terminated due to failure in the 6-digit criteria of the source and/or destination account");
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), "The size of source account and destination account must be 6");
		}
		
		return null;
	}

	public Iterable<TransferModel> findTransfer() {
		return repository.findAll();
	}

	public Iterable<TransferModel> findBySchedulingDate(String schedulingDate) {
		try {
			return repository.findBySchedulingDate(Util.convertStringToLocalDate(schedulingDate));
		} catch (DateTimeParseException e) {
			logger.error("Error in setting the date.", e);
			return new ArrayList<>();
		}
	}
	
	public Iterable<TransferModel> findByTransferDate(String transferDate) {
		return repository.findByTransferDate(transferDate);
	}
	
}
