package com.cvc.dto;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import com.cvc.model.TransferModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransferDTO extends RepresentationModel<TransferDTO> {

	@JsonIgnore
	private Long id;
	
	@ApiModelProperty(position = 1)
	private String originAccount;
	
	@ApiModelProperty(position = 2)
	private String destinationAccount;

	@ApiModelProperty(position = 3)
	private String transferDate;
	
	@ApiModelProperty(position = 4)
	private BigDecimal value;
	
	
	public TransferModel convertDTOToEntity() {
		return new ModelMapper().map(this, TransferModel.class);
	}
}
