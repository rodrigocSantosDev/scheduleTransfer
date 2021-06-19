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

import com.cvc.enums.EMessage;
import com.cvc.model.RequestStatus;
import com.cvc.model.TransferModel;
import com.cvc.repository.TransferRepository;

/**
 * Service responsible for controlling transfer scheduling.
 * @author rodrigoSantos
 *
 */
@Service
public class TransferService {

	private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
	
	@Autowired
	private TransferRepository repository;
	
	/**
	 * Schedule Transfer
	 * @param transfer
	 * @return RequestStatus. Returns success and error codes in a simpler and more controlled way.
	 */
	public RequestStatus scheduleTransfer(TransferModel transfer) {
		try {
			transfer.setSchedulingDate(LocalDate.now());
			RequestStatus resquestStatus = validateRules(transfer);
			
			if(resquestStatus != null) {
				return resquestStatus;
			}
			
			calculateRates(transfer);
			
			//No rates were applied, the system is not prepared for the requested scheduling type.
			if (transfer.getRate() == null || transfer.getRate().compareTo(BigDecimal.ZERO) == 0) {
				logger.error(EMessage.WITHOUT_RATE.getValue(), LocalDate.now(), transfer);
				return new RequestStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), EMessage.INTERNAL_ERROR.getValue());
			}
		
		//Catch exception, logger in console but return something simpler and more understandable.
		} catch (DateTimeParseException e) {
			logger.error(EMessage.SETTING_DATE_ERROR.getValue(), e);
			return new RequestStatus(HttpStatus.BAD_REQUEST.value(), EMessage.SETTING_DATE_ERROR.getValue());
		}
		repository.save(transfer);
		
		return new RequestStatus(HttpStatus.OK.value(), EMessage.SUCESSO.getValue() + transfer.getRate().setScale(2,BigDecimal.ROUND_HALF_EVEN));
	}


	/**
	 * Validate requisition rules.
	 * @param transfer
	 * @return RequestStatus. Returns success and error codes in a simpler and more controlled way.
	 */
	private RequestStatus validateRules(TransferModel transfer) {
		
		if(transfer.getValue().compareTo(new BigDecimal(0)) <= 0) {
			logger.info(EMessage.VALUE_ERROR.getValue());
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), EMessage.VALUE_ERROR.getValue());
		}
		
		RequestStatus requestStatus = validateAccountSize(transfer);
		
		if(requestStatus == null) {
			requestStatus = validateTransferDate(transfer);
		}
		
		return requestStatus;
	}
	
	/**
	 * Calculation of rates according to the number of days of the transfer date and value.
	 * @param transfer
	 */
	private void calculateRates(TransferModel transfer) {
		LocalDate transferDate = LocalDate.parse(transfer.getTransferDate()); 
		long differenceDays = ChronoUnit.DAYS.between(transfer.getSchedulingDate(), transferDate);
		
		if(transfer.getSchedulingDate().format(formatter).equals(transferDate.format(formatter))) {
			BigDecimal rate = new BigDecimal(3);
			calculateRateWithPercentageOnValue(transfer, rate, 3d);
		} else {
			calculareRatesPerDay(transfer, differenceDays);
		}
	}

	/**
	 * Set rates according to day rules.
	 * @param transfer
	 * @param differenceInDays
	 */
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

	/**
	 * Set rates according to percentage rules.
	 * @param transfer
	 * @param rate
	 * @param percentage
	 */
	private void calculateRateWithPercentageOnValue(TransferModel transfer, BigDecimal rate, double percentage) {
		percentage = percentage / 100f;
		transfer.setRate(transfer.getValue().multiply(BigDecimal.valueOf(percentage)).add(rate));
	}

	/**
	 * Validate transfer date
	 * @param transfer
	 * @return
	 */
	private RequestStatus validateTransferDate(TransferModel transfer) {
		LocalDate transferDate = LocalDate.parse(transfer.getTransferDate());
		if(transferDate.isBefore(transfer.getSchedulingDate())) {
			logger.info(EMessage.PROCESS_CLOSED_RULE_DATE.getValue());
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), EMessage.DATE_LESS_THAN_CURRENT.getValue());
		}
		return null;
	}

	/**
	 * Validate size of origin account and destination account.
	 * @param transfer
	 * @return
	 */
	private RequestStatus validateAccountSize(TransferModel transfer) {
		
		if(transfer.getOriginAccount().length() != 6 || transfer.getDestinationAccount().length() !=6) {
			logger.info(EMessage.PROCESS_TERMINATED_INVALID_ACCOUNT.getValue());
			return new RequestStatus(HttpStatus.PRECONDITION_FAILED.value(), EMessage.SIZE_ACCOUNT_INVALID.getValue());
		}
		
		return null;
	}

	/**
	 * Search all registered schedulings.
	 * @return
	 */
	public Iterable<TransferModel> findAll() {
		return repository.findAll();
	}

	/**
	 * Search registered schedulings filtered by schedulingDate.
	 * @param schedulingDate
	 * @return
	 */
	public Iterable<TransferModel> findBySchedulingDate(String schedulingDate) {
		try {
			return repository.findBySchedulingDate(LocalDate.parse(schedulingDate));
		} catch (DateTimeParseException e) {
			logger.error(EMessage.SETTING_DATE_ERROR.getValue(), e);
			return new ArrayList<>();
		}
	}
	
	/**
	 * Search registered schedulings filtered by transferDate.
	 * @param transferDate
	 * @return
	 */
	public Iterable<TransferModel> findByTransferDate(String transferDate) {
		return repository.findByTransferDate(transferDate);
	}
	
}
