package com.cvc.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Api(hidden = true)
@Entity(name = "Trasnfer")
public class TransferModel {
	
	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
	private Long id;
	
	@ApiModelProperty(position = 1)
	@Basic(optional = false)
    @NotNull
    @Column(name = "originAccount", nullable = false)
	private String originAccount;
	
	@ApiModelProperty(position = 2)
	@Basic(optional = false)
    @NotNull
    @Column(name = "destinationAccount", nullable = false)
	private String destinationAccount;
	
	@JsonIgnore
	@Basic(optional = false)
    @NotNull
    @Column(name = "rate", nullable = false)
	private BigDecimal rate;
	
	@ApiModelProperty(position = 4)
	@Basic(optional = false)
    @NotNull
    @Column(name = "value", nullable = false)
	private BigDecimal value;
	
	@ApiModelProperty(position = 3)
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtTransfer", nullable = false)
	private String transferDate;
	
	@JsonIgnore
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtScheduling", nullable = false)
	private LocalDate schedulingDate;
	
	/**
	 * @return the originAccount
	 */
	public String getOriginAccount() {
		return originAccount;
	}
	/**
	 * @return the destinationAccount
	 */
	public String getDestinationAccount() {
		return destinationAccount;
	}
	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}
	/**
	 * @return the transferDate
	 */
	public String getTransferDate() {
		return transferDate;
	}
	/**
	 * @return the schedulingDate
	 */
	public LocalDate getSchedulingDate() {
		return schedulingDate;
	}
	/**
	 * @param originAccount the originAccount to set
	 */
	public void setOriginAccount(String originAccount) {
		this.originAccount = originAccount;
	}
	/**
	 * @param destinationAccount the destinationAccount to set
	 */
	public void setDestinationAccount(String destinationAccount) {
		this.destinationAccount = destinationAccount;
	}
	/**
	 * @param valor the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	/**
	 * @param transferDate the transferDate to set
	 */
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
	/**
	 * @param schedulingDate the schedulingDate to set
	 */
	public void setSchedulingDate(LocalDate schedulingDate) {
		this.schedulingDate = schedulingDate;
	}
	/**
	 * @return the rate
	 */
	public BigDecimal getRate() {
		return rate;
	}
	/**
	 * @param taxa the rate to set
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}

