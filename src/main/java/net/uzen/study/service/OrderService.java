package net.uzen.study.service;

import lombok.RequiredArgsConstructor;
import net.uzen.study.Domain.Delivery;
import net.uzen.study.Domain.Member;
import net.uzen.study.Domain.Order;
import net.uzen.study.Domain.OrderItem;
import net.uzen.study.Domain.item.Item;
import net.uzen.study.repository.ItemRepository;
import net.uzen.study.repository.MemberRepository;
import net.uzen.study.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 엔티티가 핵심 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 "도메인 모델 패턴"이라 한다.
 * <p>service 에서는 단순히 엔티티를 조회하고, 엔티티에 구현된 비즈니스 로직을 호출한 뒤, 마지막에 repository 를 호출하는 게 전부이다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     * @param memberId
     * @param itemId
     * @param count
     * @return
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 회원 조회
        Member member = memberRepository.findOne(memberId);
        // 상품 조회
        Item item = itemRepository.findOne(itemId);

        // 배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성 - OrderItem.createOrderItem 이 외의 주문 상품 생성을 막아야 한다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
//        OrderItem orderItem1 = new OrderItem(); // 생성자를 protected 로 변경하면, 이와 같은 주문상품 생성을 하지 않을 것이다.

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // 주문만 저장했으나, cascade 옵션에 의해 delivery 와 orderItem 은 자동으로 persist 되면서 DB에 들어간다.

        return order.getId();
    }

    /**
     * 주문 취소
     * @param orderId
     */
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order findOrder = orderRepository.findOne(orderId);
        // 주문 취소
        findOrder.cancel();
    }

    //TODO: 검색
}