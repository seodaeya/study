package net.uzen.study.service;

import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.Member;
import net.uzen.study.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Transactional Annotation 에 readOnly 속성에 true을 줄 경우, DB 성능 최적화에 도움이 된다.
 * <p>RequiredArgsConstructor Annotation 은 final 로 생성된 필드만 가지고, 생성자를 만들어준다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

/*
    // 1) 가장 기본적으로 사용하는 방법
    @Autowired
    private MemberRepository memberRepository;

    // 2) 테스트할 때 좋다. 중간에 바뀔 수 있다.
    private MemberRepository memberRepository;
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 3) 생성자 2)의 단점을 보완해준다.
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 4) 모든 필드를 기준으로 생성자를 만들어준다.
...
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberService {
...
    private final MemberRepository memberRepository;

    @Autowired // spring 상위 버전을 사용하면 생성자가 하나면 Autowired를 사용하지 않아도 된다.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*/
    /**
     * Injection 하는 부분에서 끝나는 필드는 final로 잡아준다. 중간에 혹시 필드가 필요한 것들이 있다?
     */
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     * <p>Transactional 이 전체 Service 대상으로 readOnly 처리 되어 있지만,
     * <p>쓰기가 필요할 경우, 속성 없이 Transactional 로 쓰기 처리할 수 있다.
     *
     * @param member
     * @return
     */
    @Transactional
    public long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member); // TODO: 멀티쓰레드로 동접시, memberA라는 회원이 등록될 수 있다. => 최
        return member.getId();
    }

    /**
     * 중복 회원 조회
     * <p>TODO: 중복 회원 등록을 막기 위해, member.name을 유니크 제약 조건을 주는 것이 좋다.
     *
     * @param member
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회
     * @param memberId
     * @return
     */
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}