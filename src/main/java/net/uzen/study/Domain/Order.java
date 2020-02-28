package net.uzen.study.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
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
     * #참고: 외래 키가 있는 곳을 연관관계의 주인으로 정해라.
     * <p>JoinColumn 은 주인
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * mappedBy 는 읽기 전용
     */
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * #참고: 외래 키가 있는 곳을 연관관계의 주인으로 정해라.
     * <p>JoinColumn 은 주인
     */
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
