package com.cvc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import com.cvc.dto.TransferDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer")
public class TransferModel implements Serializable {
	
	private static final long serialVersionUID = 5006554807322181656L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
	private Long id;
	
	@Basic(optional = false)
    @Column(name = "originAccount", nullable = false)
	private String originAccount;
	
	@Basic(optional = false)
    @Column(name = "destinationAccount", nullable = false)
	private String destinationAccount;
	
	@Basic(optional = false)
    @Column(name = "rate", nullable = false)
	private BigDecimal rate;
	
	@Basic(optional = false)
    @Column(name = "value", nullable = false)
	private BigDecimal value;
	
	@Basic(optional = false)
    @Column(name = "dtTransfer", nullable = false)
	private String transferDate;
	
	@Basic(optional = false)
    @Column(name = "dtScheduling", nullable = false)
	private LocalDate schedulingDate;
	
	public TransferDTO convertEntityToDTO() {
		return new ModelMapper().map(this, TransferDTO.class);
	}
	
}

