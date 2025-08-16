package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Entity.BorrowTransaction;
import com.example.mendix.Service.BorrowTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    private final BorrowTransactionService borrowService;

    public BorrowController(BorrowTransactionService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrowBook")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "BORROW", entity = "BorrowTransaction")
    public BorrowTransaction borrowBook(@RequestParam Long memberId,
                                        @RequestParam Long bookId,
                                        @RequestParam String dueDate) {
        LocalDateTime due = LocalDateTime.parse(dueDate);
        return borrowService.borrowBook(memberId, bookId, due);
    }

    @PostMapping("/returnBook")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "RETURN", entity = "BorrowTransaction")
    public BorrowTransaction returnBook(@RequestParam Long transactionId) {
        return borrowService.returnBook(transactionId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "VIEW", entity = "BorrowTransaction")
    public Page<BorrowTransaction> allTransactions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return borrowService.getAllTransactions(pageable);
    }
}
