package com.cg.service.deposit;

import com.cg.model.Customer;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.service.customer.CustomerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class DepositServiceImpl implements IDepositService {

    private final DepositRepository depositRepository;
    private final CustomerRepository customerRepository;
    private final CustomerServiceImpl customerService;
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
}
