package org.gfg.TransactionService.controller;

import org.gfg.TransactionService.model.TransactionResponse;
import org.gfg.TransactionService.request.TxnRequest;
import org.gfg.TransactionService.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/txn-service")
public class TransactionController {


    @Autowired
    TransactionService transactionService;

    @PostMapping("/initiate/transaction")
    public String initiateTransaction(@RequestBody TxnRequest txnRequest){
       UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String senderId = userDetails.getUsername();

      return   transactionService.initiateTransaction(txnRequest,senderId);
    }


    @GetMapping("/get/txn/history")
    public List<TransactionResponse> transactionHistory(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mobileNo = userDetails.getUsername();

       return transactionService.getTransactionHistory(mobileNo);
    }
}
