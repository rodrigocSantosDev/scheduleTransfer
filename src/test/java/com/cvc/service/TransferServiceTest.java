package com.cvc.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.cvc.model.TransferModel;
import com.cvc.repository.TransferRepository;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

	@Mock
	private TransferRepository repository; 
	
	@InjectMocks
	private TransferService service;
	
	@DisplayName("Transfer successfully")
	@Test
	public void transfer_test() {
		TransferModel model = loadModel();

		assertEquals(HttpStatus.OK.value(), service.scheduleTransfer(model).getStatus());
	}
	
	@DisplayName("Schedule transfer Bad request. Error in setting the date")
	@Test
	public void transferBadRequest_test() {
		TransferModel model = loadModel();
		model.setTransferDate("19-06-2021");

		assertEquals(HttpStatus.BAD_REQUEST.value(), service.scheduleTransfer(model).getStatus());
	}
	
	@DisplayName("Validate percentage rules")
	@Test
	public void transferCases_test() {
		TransferModel model = loadModel();
		
		LocalDate date = LocalDate.now();
		date = date.plusDays(2);
		model.setTransferDate(date.toString());
		assertTrue(service.scheduleTransfer(model).getResponseBody().contains("24.00"));
		
		//rule over 10 to 20 days. Tracking 2 + 9 = 12 days
		date = date.plusDays(10);
		model.setTransferDate(date.toString());
		assertTrue(service.scheduleTransfer(model).getResponseBody().contains("8.00"));
		
		//rule over 20 to 30 days. Tracking 12 + 10 = 22 days
		date = date.plusDays(10);
		model.setTransferDate(date.toString());
		assertTrue(service.scheduleTransfer(model).getResponseBody().contains("6.00"));
		
		//rule over 30 to 40 days. Tracking 22 + 10 = 32 days
		date = date.plusDays(10);
		model.setTransferDate(date.toString());
		assertTrue(service.scheduleTransfer(model).getResponseBody().contains("4.00"));
		
		//Rule over 40 days and value over 100.000. Tracking 32 + 10 = 42 days
		date = date.plusDays(10);
		model.setTransferDate(date.toString());
		model.setValue(new BigDecimal(100010));
		assertTrue(service.scheduleTransfer(model).getResponseBody().contains("2000.20"));
		
	}
	
	@DisplayName("Validate without rate")
	@Test
	public void transferWithoutRate_test() {
		TransferModel model = loadModel();
		
		//without rate
		LocalDate date = LocalDate.now();
		date = date.plusDays(42);
		model.setTransferDate(date.toString());
		model.setValue(new BigDecimal(100000));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), service.scheduleTransfer(model).getStatus());
	}
	
	@DisplayName("Validate zero value")
	@Test
	public void transferZeroValue_test() {
		TransferModel model = loadModel();
		
		model.setValue(BigDecimal.ZERO);
		assertEquals(HttpStatus.PRECONDITION_FAILED.value(), service.scheduleTransfer(model).getStatus());
	}
	
	@DisplayName("Validate tranfer date less than current date")
	@Test
	public void transferDateLessThanCurrentDate_test() {
		TransferModel model = loadModel();
		
		model.setTransferDate("2020-06-19");
		assertEquals(HttpStatus.PRECONDITION_FAILED.value(), service.scheduleTransfer(model).getStatus());
	}
	
	@DisplayName("Validate Account Size")
	@Test
	public void transferValidateAccountSize_test() {
		TransferModel model = loadModel();
		
		model.setOriginAccount("12345");;
		assertEquals(HttpStatus.PRECONDITION_FAILED.value(), service.scheduleTransfer(model).getStatus());
	}
	
	@DisplayName("List all transfers")
	@Test
	public void findAll_test() {
		TransferModel model = loadModel();
		List<TransferModel> listModel = new ArrayList<>();
		listModel.add(model);
		Mockito.when(repository.findAll()).thenReturn(listModel);

		assertEquals(listModel,service.findAll());
	}
	
	@DisplayName("List transfers by scheduling Date")
	@Test
	public void findBySchedulingDate_test() {
		TransferModel model = loadModel();
		List<TransferModel> listModel = new ArrayList<>();
		listModel.add(model);
		String schedulingDate = "2021-06-19";
		
		Mockito.when(repository.findBySchedulingDate(LocalDate.parse(schedulingDate))).thenReturn(listModel);

		assertEquals(listModel,service.findBySchedulingDate(schedulingDate));
	}
	
	@DisplayName("List transfers by scheduling Date - DateTimeParseException")
	@Test
	public void findBySchedulingDateDateTimeParseException_test() {
		String schedulingDate = "19-06-2021";
		
		assertEquals(new ArrayList<>(),service.findBySchedulingDate(schedulingDate));
	}
	
	@DisplayName("List transfers by transfer Date")
	@Test
	public void findByTransferDate_test() {
		TransferModel model = loadModel();
		List<TransferModel> listModel = new ArrayList<>();
		listModel.add(model);
		
		Mockito.when(repository.findByTransferDate(model.getTransferDate())).thenReturn(listModel);

		assertEquals(listModel,service.findByTransferDate(model.getTransferDate()));
	}
	
	private TransferModel loadModel() {
		TransferModel model = new TransferModel();
		model.setOriginAccount("123456");
		model.setDestinationAccount("123456");
		model.setTransferDate(LocalDate.now().toString());
		model.setValue(new BigDecimal(100));
		return model;
	}
	
}
