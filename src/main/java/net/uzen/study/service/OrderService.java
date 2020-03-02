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

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }
}