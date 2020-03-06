package net.uzen.study.repository;

import lombok.Getter;
import lombok.Setter;
import net.uzen.study.domain.OrderStatus;

/**
 * 주문 검색
 */
@Getter @Setter
public class OrderSearch {

    /**
     * 회원 이름
     */
    private String memberName;

    /**
     * 주문 상태
     */
    private OrderStatus orderStatus; // { ORDER, CANCEL }
}