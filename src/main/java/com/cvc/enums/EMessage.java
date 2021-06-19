package com.cvc.enums;

public enum EMessage {
	
	SUCESSO("Transfer successfully. Rate charged from: "),
	WITHOUT_RATE("No rate were found for the transfer request made. Request date: {} - requisition data: {}"),
	INTERNAL_ERROR("An internal error has occurred, please try again later."),
	SETTING_DATE_ERROR("Error in setting the date. Expected format: yyyy-mm-dd"),
	PROCESS_CLOSED_RULE_DATE("Process closed due to failure in criteria where the transfer date is before the current date"),
	DATE_LESS_THAN_CURRENT("Transfer date cannot be earlier than current date"),
	SIZE_ACCOUNT_INVALID("The size of source account and destination account must be 6"),
	PROCESS_TERMINATED_INVALID_ACCOUNT("Process was terminated due to failure in the 6-digit criteria of the source and/or destination account"),
	VALUE_ERROR("It is necessary to enter a valid value. Greater than zero");
	
    private String value;

    EMessage(String value){
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
}
