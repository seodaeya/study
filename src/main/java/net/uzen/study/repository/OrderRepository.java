package net.uzen.study.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.Order;
import net.uzen.study.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    /**
     * 주문
     *
     * @param order
     */
    public void save(Order order) {
        em.persist(order);
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId
     * @return
     */
    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    //주문 목록 조회 - JPQL
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
    //주문 목록 조회 - JPA 표준 스펙 제공
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        /*
            * 치명적인 단점!
                - 유지보수성이 떨어진다.
                - 전체 쿼리가 머릿 속에 떠오르지 않는다.
         */
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    /**
     * 진정한 QueryDSL 의 힘이여! 보여주거라! ㅋㅋㅋ
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByQuerDSL(OrderSearch orderSearch) {
        JPAQuery<?> query = new JPAQuery<Void>(em);
        final QOrder qOrder = QOrder.order;
        final QMember qMember = QMember.member;

        // 1. 기본적인 쿼리
        query.select(qOrder)
                .from(qOrder)
                .innerJoin(qMember);

        // 2.1. 조건에 해당하는 where 문 설정
        OrderStatus searchOrderStatus = orderSearch.getOrderStatus();
        if (null != searchOrderStatus) {
            query.where(qOrder.status.eq(searchOrderStatus));
        }

        // 2.2. 조건에 해당하는 where 문 설정 - 회원명
        String searchMemberName = orderSearch.getMemberName();
        if (null != searchMemberName) {
            query.where(qMember.name.like(searchMemberName));
        }

        return (List<Order>) query.fetch();
    }
}