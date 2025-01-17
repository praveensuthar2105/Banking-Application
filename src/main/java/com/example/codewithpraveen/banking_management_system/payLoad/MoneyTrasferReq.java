package com.example.codewithpraveen.banking_management_system.payLoad;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MoneyTrasferReq {
	
	private long senderAccountNumber;
	private long receiverAccountNumber;
	private double amount;
}
