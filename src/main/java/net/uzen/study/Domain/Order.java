package net.uzen.study.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * # cascade 설정
 * <p>- Order 가 Delivery 와 OrderItem 을 관리 -> 주인이 private owner 인 경우에만 사용.
 * <p>- Delivery 와 OrderItem 은 order 말고, 다른 곳에서 사용하지 않는다.
 * <p>※ OrderItem 이 다른 엔티티를 참조하지만, OrderItem 을 다른 엔티티에서 참조하지 않는다.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    /**
     * <pre>@Column(name = "order_id")
     * - order_id 과 같은 네이밍룰 방식은 DBA가 선호하는 네이밍룰 방식이다.
     * </pre>
     */
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /**
     * #참고: 외래 키가 있는 곳을 연관관계의 주인(JoinColumn 은 주인)으로 정해라.
     * <p>XToOne(ManyToOne, OneToOne)은 무조건 LAZY로 설정하자.
     * <p>EAGER를 사용할 경우, N + 1 문제가 발생하게 된다.
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * mappedBy 는 읽기 전용
     * <p>CascadeType.ALL 해주면, Order만 persist 해서 OrderItem은 별도로 담지 않아도 된다.
     * <p>각 Object 별로 persist 해주어야 하나, cascade 설정을 해주면, order만 persist 해줘도 cascade 된다.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * # fetch = LAZY
     * <p> LAZY 로딩이 트랜잭션 밖에서 안되는 이슈로 EAGER 로 바꾸는 경우도 있으나, EAGER 로 바꿔서 해결하면 안되고,
     * <p>1) 트랜잭션을 빨리 가져온다거나
     * <p>2) 오픈세션인뷰를 사용한다.
     * <p>1), 2) 방법 외에 fetch 조인으로 다 해결이 된다.
     * <p># CascadeType.ALL
     * <p>CascadeType.ALL 해주면, Order만 persist 해서 OrderItem은 별도로 담지 않아도 된다.
     * <p>각 Object 별로 persist 해주어야 하나, cascade 설정을 해주면, order만 persist 해줘도 cascade 된다.
     */
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //==연관관계 편의 메서드==//

    /**
     * 연관관계 편의 메서드 = 양방향 편의 메서드
     * <p>양방향 편의 메서드의 위치는 컨트롤 하는 쪽에 있는 것이 좋다.
     *
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); // ManyToOne
    }

    /**
     * 연관관계 편의 메서드 = 양방향 편의 메서드
     * <p>양방향 편의 메서드의 위치는 컨트롤 하는 쪽에 있는 것이 좋다.
     *
     * @param orderItem
     */
    public void addOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); // OneToMany
    }

    /**
     * 연관관계 편의 메서드 = 양방향 편의 메서드
     * <p>양방향 편의 메서드의 위치는 컨트롤 하는 쪽에 있는 것이 좋다.
     *
     * @param delivery
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this); // OneToOne
    }

    //==생성 메서드==/

    /**
     * 주문 생성
     * <p>OrderItem 은 생성 시, 재고가 빠져서 주문이 들어온다.
     *
     * @param member
     * @param delivery
     * @param OrderItmes
     * @return
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... OrderItmes) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : OrderItmes) {
            order.addOrderItems(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (DeliveryStatus.COMP.equals(delivery.getStatus())) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     *
     * @return
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}