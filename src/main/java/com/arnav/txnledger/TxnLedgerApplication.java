package com.arnav.txnledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TxnLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxnLedgerApplication.class, args);
	}

}
