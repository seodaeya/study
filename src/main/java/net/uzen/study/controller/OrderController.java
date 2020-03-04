package net.uzen.study.controller;

import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.Member;
import net.uzen.study.domain.Order;
import net.uzen.study.domain.item.Item;
import net.uzen.study.repository.OrderSearch;
import net.uzen.study.service.ItemService;
import net.uzen.study.service.MemberService;
import net.uzen.study.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    /**
     * 주문 화면
     *
     * @param model
     * @return
     */
    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findAll();

        model.addAttribute("members", members); // 회원 목록
        model.addAttribute("items", items); // 상품 목록

        return "/order/orderForm";
    }

    /**
     * 주문
     *
     * @param memberId
     * @param itemId
     * @param count
     * @return
     */
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId
            , @RequestParam("itemId") Long itemId
            , @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    /**
     * 주문 목록
     *
     * @param model
     * @return
     */
    @GetMapping("/orders")
    public String list(@ModelAttribute("orderSearch") OrderSearch orderSearch // 계속 OrderSearch 에 값이 담기지 않아서 멘붕이 왔으나, OrderSearch 에 Setter 를 열어주지 않아서 생긴 문제였다.
            , Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);

        model.addAttribute("orders", orders);

        return "/order/orderList";
    }

    /**
     * 주문 취소
     *
     * @param orderId
     * @return
     */
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }
}