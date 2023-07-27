package com.ewallet.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewallet.config.PasswordEncoder;
import com.ewallet.dto.AddMoneyDTO;
import com.ewallet.dto.SendMoneyDTO;
import com.ewallet.dto.UserDTO;
import com.ewallet.transaction.Transaction;
import com.ewallet.transaction.Transaction.TransactionType;
import com.ewallet.transaction.TransactionRepository;
import com.ewallet.transaction.TransactionService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, TransactionRepository transactionRepository, TransactionService transactionService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User authenticateUser(UserDTO userDTO) {
    	String email = userDTO.getEmail();
    	String password = userDTO.getPassword();
    	
    	Optional<User> optionalUser = userRepository.findByEmail(email);
    	
    	if (optionalUser != null) {
    		User user = optionalUser.get();
    		
    		if (passwordEncoder.matches(password, user.getPassword())) {
    			return user;
    		}
    	}
		return null;
    }

    public User registerUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        User newUser = new User();
        newUser.setEmail(email);

        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);

        return userRepository.save(newUser);
    }
    
    public User updateUser(Long id, UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }

            if (password != null && !password.isEmpty()) {
                String hashedPassword = passwordEncoder.encode(password);
                user.setPassword(hashedPassword);
            }

            return userRepository.save(user);
        }

        return null;
    }
    
    public boolean deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return true;
        }

        return false;
    }
    
    public User addMoney(Long id, AddMoneyDTO addMoneyDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBalance(user.getBalance() + addMoneyDTO.getAmount());
            userRepository.save(user);

            transactionService.recordAddMoney(user, addMoneyDTO.getAmount());

            return user;
        } else {
            return null;
        }
    }

    public User sendMoney(Long id, SendMoneyDTO sendMoneyDTO) {
        Optional<User> optionalSender = userRepository.findById(id);
        Optional<User> optionalReceiver = userRepository.findById(sendMoneyDTO.getReceiver());

        if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
            User sender = optionalSender.get();
            User receiver = optionalReceiver.get();
            double amount = sendMoneyDTO.getAmount();

            if (sender.getBalance() >= amount) {
                sender.setBalance(sender.getBalance() - amount);
                receiver.setBalance(receiver.getBalance() + amount);

                userRepository.save(sender);
                userRepository.save(receiver);

                transactionService.recordSendMoney(sender, receiver, amount);
            } else {
                return null;
            }

            return sender;
        } else {
            return null;
        }
    }
}
