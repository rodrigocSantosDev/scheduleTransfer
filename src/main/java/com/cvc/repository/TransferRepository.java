package com.cvc.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cvc.model.TransferModel;

@Repository
public interface TransferRepository extends CrudRepository<TransferModel, Integer> {

	List<TransferModel> findAll();
	
	List<TransferModel> findBySchedulingDate(LocalDate schedulingDate);
	
	List<TransferModel> findByTransferDate(String transferDate);
	
}
