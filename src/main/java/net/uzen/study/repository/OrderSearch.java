package net.uzen.study.repository;

import lombok.Getter;
import net.uzen.study.Domain.OrderStatus;

@Getter
public class OrderSearch {

    private String memberName; // 회원 이름

    private OrderStatus orderStatus; // 주문 상태 { ORDER, CANCEL }
}
