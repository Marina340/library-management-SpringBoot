package com.example.mendix.Service;

import com.example.mendix.Entity.Member;
import com.example.mendix.Repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Page<Member> getAllMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Member> getMember(Long id) {
        return memberRepository.findById(id);
    }
    public Member updateMember(Long id, Member updatedMember) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setName(updatedMember.getName());
        member.setEmail(updatedMember.getEmail());
        member.setPhone(updatedMember.getPhone());
        member.setAddress(updatedMember.getAddress());

        return memberRepository.save(member);
    }
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
