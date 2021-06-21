package com.cvc.resouces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import com.cvc.dto.TransferDTO;
import com.cvc.model.RequestStatus;
import com.cvc.resources.TransferResource;
import com.cvc.service.TransferService;

@ExtendWith(MockitoExtension.class)
public class TransferResourceTest {

	@Mock
	private TransferService service;

	@InjectMocks
	private TransferResource resource;
	
	@MockitoSettings(strictness = Strictness.WARN)
	@Test
	public void scheduleTransfer_test() {
		TransferDTO dto = loadDto();
		final RequestStatus responseTest = new RequestStatus(HttpStatus.OK.value(), " ");

		 Mockito.when(service.scheduleTransfer(any())).thenReturn(responseTest);

		assertEquals(HttpStatus.OK.value(), resource.transfer(dto).getStatus());
	}
	
	@Test
	public void findAll_test() {
		TransferDTO dto = loadDto();
		List<TransferDTO> listModel = new ArrayList<>();
		listModel.add(dto);
		Mockito.when(service.findAll()).thenReturn(listModel);

		assertEquals(listModel,resource.findAll());
	}
	
	@Test
	public void findBySchedulingDate_test() {
		TransferDTO dto = loadDto();
		List<TransferDTO> listModel = new ArrayList<>();
		listModel.add(dto);
		String schedulingDate = "2021-06-19";
		
		Mockito.when(service.findBySchedulingDate(schedulingDate)).thenReturn(listModel);

		assertEquals(listModel,resource.findBySchedulingDate(schedulingDate));
	}
	
	@Test
	public void findByTransferDate_test() {
		TransferDTO dto = loadDto();
		List<TransferDTO> listModel = new ArrayList<>();
		listModel.add(dto);
		
		Mockito.when(service.findByTransferDate(dto.getTransferDate())).thenReturn(listModel);

		assertEquals(listModel,resource.findByTransferDate(dto.getTransferDate()));
	}

	private TransferDTO loadDto() {
		TransferDTO dto = new TransferDTO();
		dto.setOriginAccount("123456");
		dto.setDestinationAccount("123456");
		dto.setTransferDate("2021-06-19");
		dto.setValue(BigDecimal.TEN);
		return dto;
	}
}
