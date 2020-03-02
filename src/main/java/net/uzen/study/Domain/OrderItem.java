package net.uzen.study.Domain;

import lombok.Getter;
import lombok.Setter;
import net.uzen.study.Domain.item.Item;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //==생성 메서드==//
    protected OrderItem() {
    }

    /**
     * @param item
     * @param orderPrice
     * @param count
     * @return
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice); // 추후 할인 적용을 위해 별도로 뺌
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//

    /**
     * 상품 취소
     * <p>주문 수량을 재고수량에 더해준다.
     */
    public void cancel() {
        getItem().addStock(count); // 현재는 전체 취소 밖에 없기 때문에 단순화함.
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     *
     * @return
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}