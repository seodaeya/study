package net.uzen.study;

import net.uzen.study.Domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    /**
     * ※ @Test 에 있는 모든 코드는 실행 이후 Rollback 된다.
     * @throws Exception
     */
    @Test
    @Transactional // No EntityManager with actual transaction available for current thread
    @Rollback(false) // @Test지만 Commit 하고 싶을 때 사용하면 된다.
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
    }
}