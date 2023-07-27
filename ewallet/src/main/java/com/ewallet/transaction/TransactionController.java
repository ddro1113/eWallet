package com.ewallet.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
	
	private final TransactionRepository transactionRepository;
	
	@Autowired
	public TransactionController(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	@GetMapping("test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok().body("Transaction: This is a test endpoint.");
	}
	
	@GetMapping("find-all")
	public List<Transaction> findAllTransactions() {
		return transactionRepository.findAll();
	}
}
