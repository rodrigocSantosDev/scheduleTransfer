package com.cvc.resouces;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.cvc.model.RequestStatus;
import com.cvc.model.TransferModel;
import com.cvc.resources.TransferResource;
import com.cvc.service.TransferService;

@ExtendWith(MockitoExtension.class)
public class TransferResourceTest {

	@Mock
	private TransferService service;

	@InjectMocks
	private TransferResource resource;
	
	@Test
	public void scheduleTransfer_test() {
		TransferModel model = loadModel();
		final RequestStatus responseTest = new RequestStatus(HttpStatus.OK.value(), " ");

		Mockito.when(service.scheduleTransfer(model)).thenReturn(responseTest);

		assertEquals(HttpStatus.OK.value(), resource.transfer(model).getStatus());
	}
	
	@Test
	public void findAll_test() {
		TransferModel model = loadModel();
		List<TransferModel> listModel = new ArrayList<>();
		listModel.add(model);
		Mockito.when(service.findAll()).thenReturn(listModel);

		assertEquals(listModel,resource.findAll());
	}
	
	@Test
	public void findBySchedulingDate_test() {
		TransferModel model = loadModel();
		List<TransferModel> listModel = new ArrayList<>();
		listModel.add(model);
		String schedulingDate = "2021-06-19";
		
		Mockito.when(service.findBySchedulingDate(schedulingDate)).thenReturn(listModel);

		assertEquals(listModel,resource.findBySchedulingDate(schedulingDate));
	}
	
	@Test
	public void findByTransferDate_test() {
		TransferModel model = loadModel();
		List<TransferModel> listModel = new ArrayList<>();
		listModel.add(model);
		
		Mockito.when(service.findByTransferDate(model.getTransferDate())).thenReturn(listModel);

		assertEquals(listModel,resource.findByTransferDate(model.getTransferDate()));
	}

	private TransferModel loadModel() {
		TransferModel model = new TransferModel();
		model.setOriginAccount("123456");
		model.setDestinationAccount("123456");
		model.setTransferDate("2021-06-19");
		model.setValue(BigDecimal.TEN);
		return model;
	}
}
