package net.uzen.study;

import net.uzen.study.Domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Tip. Ctrl + Shift + T -> Test 생성
 */
@Repository
public class MemberRepository {

    /**
     * Spring Boot 가 PersistenceContext에 있으면, EntityManager을 주입시켜준다.
     */
    @PersistenceContext EntityManager em;

    /**
     * Command 와 Query 를 분리해라.
     * <p>저장을 하고 나면 가급적이면 사이드 이펙트를 일으키는 Command성이기 때문에 Return 값을 만들지 않는다.
     * <p>하지만, id 정도는 이후 id로 조회할 수 있기 때문에 Return 해준다.
     *
     * @param member
     * @return
     */
    public Long save(Member member) {
        em.persist(member); // Command
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}