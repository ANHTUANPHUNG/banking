package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final DepositRepository depositRepository;
    private final TransferRepository transferRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Long aLong) {

        return customerRepository.findAllById(aLong);
    }

    @Override
    public void create(Customer customer) {
        customer.setDeleted(false);
        customer.setBalance(BigDecimal.ZERO);
        customerRepository.save(customer);
    }

    @Override
    public void update(Long aLong, Customer customer) {
        Customer customer1 = findById(aLong);
        customer1.setFullName(customer.getFullName());
        customer1.setEmail(customer.getEmail());
        customer1.setAddress(customer.getAddress());
        customer1.setPhone(customer.getPhone());
        customerRepository.save(customer1);
    }

    @Override
    public boolean removeById(Long aLong) {
        Customer customer = findById(aLong);
        customer.setDeleted(!customer.getDeleted());
        customerRepository.save(customer);
        return true;
    }
    public void updateBalance(Long id,Deposit deposit){
        Customer customer1 = findById(id);
        deposit.setCustomer(customer1);
        deposit.setTime(LocalDateTime.now());
        depositRepository.save(deposit);
        customer1.setBalance(customer1.getBalance().add(deposit.getTransactionAmount()) );

        customerRepository.save(customer1);
    }
    public void updateWithdraw(Long id,Deposit deposit){
        Customer customer1 = findById(id);
        deposit.setCustomer(customer1);
        deposit.setTime(LocalDateTime.now());
        depositRepository.save(deposit);
        customer1.setBalance(customer1.getBalance().subtract(deposit.getTransactionAmount()) );

        customerRepository.save(customer1);
    }
    public void updateTransfer1(Long id, Transfer transfer, Customer customer){
        Customer customer1= findById(id);
        transfer.setSender(customer1);
        customer = findById(customer.getId());
        transfer.setRecipient(customer);
        transfer.setFees(10L);
        transfer.setFeesAmount(transfer.getTransferAmount().multiply(new BigDecimal("0.1")));
        customer1.setBalance(customer1.getBalance().subtract(transfer.getTransferAmount().add(transfer.getFeesAmount())));
        transfer.setTransactionAmount(transfer.getTransferAmount().add(transfer.getFeesAmount()));
        customer.setBalance(customer.getBalance().add(transfer.getTransferAmount()));
        transfer.setLocalDateTime(LocalDateTime.now());
        customerRepository.save(customer);
        customerRepository.save(customer1);
        transferRepository.save(transfer);
    }
}
