package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Entity.Member;
import com.example.mendix.Service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "CREATE", entity = "Member")
    public Member createMember(@RequestBody Member member) {
        return memberService.saveMember(member);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET", entity = "Member")
    public Page<Member> getMembers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return memberService.getAllMembers(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET BY MEMBER ID", entity = "Member")
    public Member getMemberById(@PathVariable Long id) {
        return memberService.getMember(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found with id: " + id
                ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "UPDATE", entity = "Member")
    public Member updateMember(@PathVariable Long id, @RequestBody Member member) {
        return memberService.updateMember(id, member);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "DELETE", entity = "Member")
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
