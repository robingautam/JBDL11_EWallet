package org.gfg.TransactionService.repository;

import org.gfg.TransactionService.model.Transaction;
import org.gfg.model.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Modifying
    @Transactional
    @Query("update transaction t set t.txnStatus=:txnStatus, t.txnStatusMessage=:txnStatusMessage where t.txnId=:txnId")
    void updateTransactionDetails(String txnId, TxnStatus txnStatus, String txnStatusMessage);


    List<Transaction> findBySenderIdOrReceiverId(String sender,String receiver);
}
