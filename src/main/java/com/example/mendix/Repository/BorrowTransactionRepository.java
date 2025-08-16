package com.example.mendix.Repository;

import com.example.mendix.Entity.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {
    List<BorrowTransaction> findByMemberId(Long memberId);
    List<BorrowTransaction> findByBookId(Long bookId);
    boolean existsByBookIdAndStatus(Long bookId, String status);
}
