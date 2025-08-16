package com.example.mendix.Service;

import com.example.mendix.Entity.Book;
import com.example.mendix.Entity.BorrowTransaction;
import com.example.mendix.Entity.Member;
import com.example.mendix.Repository.BookRepository;
import com.example.mendix.Repository.BorrowTransactionRepository;
import com.example.mendix.Repository.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;

@Service
public class BorrowTransactionService {
    private final BorrowTransactionRepository borrowRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowTransactionService(BorrowTransactionRepository borrowRepository,
                                    BookRepository bookRepository,
                                    MemberRepository memberRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public BorrowTransaction borrowBook(Long memberId, Long bookId, LocalDateTime dueDate) {
        // Fetch member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Fetch book with a pessimistic lock to prevent race conditions
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the book is already borrowed
        boolean isBorrowed = borrowRepository.existsByBookIdAndStatus(bookId, "BORROWED");
        if (isBorrowed) {
            throw new RuntimeException("Book is already borrowed by another member");
        }

        // Create transaction
        BorrowTransaction transaction = new BorrowTransaction();
        transaction.setMember(member);
        transaction.setBook(book);
        transaction.setBorrowedDate(LocalDateTime.now());
        transaction.setDueDate(dueDate);
        transaction.setStatus("BORROWED");

        return borrowRepository.save(transaction);
    }

    @Transactional
    public BorrowTransaction returnBook(Long transactionId) {
        BorrowTransaction transaction = borrowRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setReturnDate(LocalDateTime.now());
        transaction.setStatus("RETURNED");
        return borrowRepository.save(transaction);
    }

    public Page<BorrowTransaction> getAllTransactions(Pageable pageable) {
        return borrowRepository.findAll(pageable);
    }
}
