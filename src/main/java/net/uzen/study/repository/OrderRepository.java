package net.uzen.study.repository;

import lombok.RequiredArgsConstructor;
import net.uzen.study.Domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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

    //TODO: 주문 목록 조회
}