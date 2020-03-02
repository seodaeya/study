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

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

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
}