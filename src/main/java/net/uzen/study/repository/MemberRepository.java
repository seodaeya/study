package net.uzen.study.repository;

import lombok.RequiredArgsConstructor;
import net.uzen.study.Domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * <p>Tip. Ctrl + Shift + T -> Test 생성
 */
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /*
        // Spring Boot 가 PersistenceContext에 있으면, Spring/JPA EntityManager을 spring 이 생성한 EntityManager 에 주입시켜준다.
        @PersistenceContext private EntityManager em;
    //    @PersistenceUnit private EntityManagerFactory emf; // PersistenceUnit Annotation을 이용하여 EntityManagerFactory를 직접 주입받을 수도 있다.
    */
    private final EntityManager em;

    /**
     * Command 와 Query 를 분리해라.
     * <p>저장을 하고 나면 가급적이면 사이드 이펙트를 일으키는 Command성이기 때문에 Return 값을 만들지 않는다.
     * <p>하지만, id 정도는 이후 id로 조회할 수 있기 때문에 Return 해준다.
     *
     * @param member //@return
     */
    public void /*Long*/ save(Member member) {
        em.persist(member); // Command
//        return member.getId();
    }

    /**
     * 회원 조회
     *
     * @param id
     * @return
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * 회원 전체 조회
     *
     * @return
     */
    public List<Member> findAll() {
        return em.createQuery("select m From Member m", Member.class)
                .getResultList();
    }

    /**
     * 이름으로 회원 조회
     *
     * @param name
     * @return
     */
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}