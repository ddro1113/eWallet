package com.ewallet.transaction;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ewallet.transaction.Transaction.TransactionType;
import com.ewallet.user.User;

@Service
public class TransactionService {
	
	private TransactionRepository transactionRepository;
	
	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	public Transaction recordAddMoney(User user, double amount) {
		Transaction transaction = new Transaction();
		
	    transaction.setDate(LocalDateTime.now());
	    transaction.setAmount(amount);
	    transaction.setTransactionType(TransactionType.ADD_MONEY);
	    transaction.setUser(user);
	    return transactionRepository.save(transaction);
	}
	
	public void recordSendMoney(User sender, User receiver, double amount) {

		Transaction senderTransaction = new Transaction();
		
        senderTransaction.setDate(LocalDateTime.now());
        senderTransaction.setAmount(-amount);
        senderTransaction.setTransactionType(TransactionType.SEND_MONEY);
        senderTransaction.setUser(sender);
        transactionRepository.save(senderTransaction);

        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setDate(LocalDateTime.now());
        receiverTransaction.setAmount(amount);
        receiverTransaction.setTransactionType(TransactionType.RECEIVE_MONEY);
        receiverTransaction.setUser(receiver);
        transactionRepository.save(receiverTransaction);
	}
}
