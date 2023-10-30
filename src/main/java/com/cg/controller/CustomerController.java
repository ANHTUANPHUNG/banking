package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.repository.CustomerRepository;
import com.cg.repository.TransferRepository;
import com.cg.service.customer.CustomerServiceImpl;
import com.cg.service.customer.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.color.ICC_ColorSpace;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerServiceImpl customerService ;
    private CustomerRepository customerRepository;
    private TransferRepository transferRepository;

    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }
    @GetMapping("/transfer")
    public String showTransferHistory(Model model) {
        List<Transfer> transfers = transferRepository.findAll();
        model.addAttribute("transfers", transfers);
        return "customer/transferHistories";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/create";
    }


    @PostMapping("/create")
    public String createCustomer(@ModelAttribute Customer customer, Model model) {
        model.addAttribute("customer", new Customer());

        if (customer.getFullName().length() == 0) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Created unsuccessful");
        }
        else {
            customerService.create(customer);

            model.addAttribute("success", true);
            model.addAttribute("message", "Created successfully");
        }

        return "customer/create";
    }
    @GetMapping("/edit/{id}")
    public String showEditPage(Model model,  @PathVariable Long id) {
        Customer customer= customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customer/edit";
    }
    @PostMapping("/edit/{id}")
    public String editCustomer(@ModelAttribute Customer customer, Model model, @PathVariable Long id) {

        if (customer.getFullName().length() == 0) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Edit unsuccessful");
        }
        else {
            customerService.update(id,customer);

            model.addAttribute("success", true);
            model.addAttribute("message", "Edit successfully");
        }

        return "customer/edit";
    }
    @GetMapping("/delete/{id}")
    public String deleteCustomer( @ModelAttribute Customer customer, Model model,@PathVariable Long id){
        Customer customer1 = customerService.findById(id);

        if (customer1 != null) {
            if (customerService.removeById(id)) {
                model.addAttribute("success", true);
                model.addAttribute("message", "Lock successful");
            } else {
                model.addAttribute("success", false);
                model.addAttribute("message", "Lock unsuccessful");
            }
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "Customer not found");
        }
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }
    @GetMapping("/deposit/{id}")
    public String showDeposit(Model model, @PathVariable Long id){
        Customer customer= customerService.findById(id);
        Deposit deposit= new Deposit();
        deposit.setCustomer(customer);

        model.addAttribute("deposit", deposit);
        return "deposit/deposit";
    }
    @PostMapping("/deposit/{id}")
    public String depositCustomer(@ModelAttribute Deposit deposit, Model model, @PathVariable Long id) {
        if(deposit.getTransactionAmount() == null || deposit.getTransactionAmount().equals(BigDecimal.ZERO)){
            model.addAttribute("success", false);
            model.addAttribute("message", "Deposit unsuccessful");
            model.addAttribute("deposit",deposit);
        } else {
            customerService.updateBalance(id,deposit);

            model.addAttribute("success", true);
            model.addAttribute("message", "Deposit successful");
        }
        Customer customer= customerService.findById(id);

        deposit.setCustomer(customer);

        model.addAttribute("deposit", deposit);
        return "deposit/deposit";
    }
    @GetMapping("/withdraw/{id}")
    public String showWithdraw(Model model, @PathVariable Long id){
        Customer customer= customerService.findById(id);
        Deposit withdraw= new Deposit();
        withdraw.setCustomer(customer);

        model.addAttribute("withdraw", withdraw);
        return "deposit/withdraw";
    }
    @PostMapping("/withdraw/{id}")
    public String WithdrawCustomer(@ModelAttribute Deposit withdraw, Model model, @PathVariable Long id) {
        Customer customer= customerService.findById(id);
        if (withdraw.getTransactionAmount() == null || withdraw.getTransactionAmount().equals(BigDecimal.ZERO)) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Withdraw unsuccessful");
            withdraw.setCustomer(customer);

        } else if (withdraw.getTransactionAmount().compareTo(BigDecimal.ZERO) > 0) {
            if (withdraw.getTransactionAmount().compareTo(customer.getBalance()) <= 0) {
                customerService.updateWithdraw(id, withdraw);
                model.addAttribute("success", true);
                model.addAttribute("message", "Withdraw successful");
            } else {
                model.addAttribute("success", false);
                model.addAttribute("message", "Inappropriate amount");
                withdraw.setCustomer(customer);

            }
        }
        model.addAttribute("withdraw", withdraw);
        return "deposit/withdraw";
    }
    @GetMapping("transfer/{id}")
    public String transferShow(@PathVariable Long id, Model model){
        Customer customer = customerService.findById(id);
        Transfer transfer = new Transfer();
        transfer.setSender(customer);
        transfer.setFees(10L);
        List<Customer> customers = customerRepository.findAllByIdNot(id);
        model.addAttribute("customers", customers);
        model.addAttribute("transfer", transfer);
        return "deposit/transfer";
    }
    @PostMapping("transfer/{id}")
    public String updateTransfer(@PathVariable Long id,Model model ,@ModelAttribute Transfer transfer, @ModelAttribute Customer customer){
        Customer customer1= customerService.findById(id);
        Transfer transfer1 = new Transfer();
        List<Customer> customers = customerRepository.findAllByIdNot(id);

        if (transfer.getRecipient() == null){
            model.addAttribute("success", false);
            model.addAttribute("message", "Enter recipient");
            model.addAttribute("transfer",transfer);
            //        Transfer transfer1 = new Transfer();
            transfer1.setSender(customer1);
            transfer1.setFees(10L);
            model.addAttribute("customers", customers);
            model.addAttribute("transfer", transfer1);
        }
        else if(transfer.getTransferAmount() == null ||transfer.getTransferAmount().equals(BigDecimal.ZERO)){
            model.addAttribute("success", false);
            model.addAttribute("message", "Enter transfer amount");
            model.addAttribute("transfer",transfer);
            //        Transfer transfer1 = new Transfer();
            transfer1.setSender(customer1);
            transfer1.setFees(10L);
            model.addAttribute("customers", customers);
            model.addAttribute("transfer", transfer1);
        } else if (transfer.getTransferAmount().compareTo(BigDecimal.ZERO) > 0) {
            if (transfer.getTransferAmount().add(transfer.getTransferAmount().multiply(new BigDecimal("0.1"))).compareTo(customer1.getBalance()) <= 0) {
                customerService.updateTransfer1(id, transfer, customer);
                model.addAttribute("success", true);
                model.addAttribute("message", "Transfer successful");
                model.addAttribute("customers", customers);

            } else {
                model.addAttribute("success", false);
                model.addAttribute("message", "Insufficient account");
                //        Transfer transfer1 = new Transfer();
                transfer1.setSender(customer1);
                transfer1.setFees(10L);
                model.addAttribute("customers", customers);
                model.addAttribute("transfer", transfer1);
            }

        }

        return "deposit/transfer";
    }
}
