package com.cvc.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cvc.model.TransferModel;

@Repository
public interface TransferRepository extends CrudRepository<TransferModel, Integer> {

	Iterable<TransferModel> findBySchedulingDate(LocalDate schedulingDate);
	
	Iterable<TransferModel> findByTransferDate(String transferDate);
	
}
