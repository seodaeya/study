package net.uzen.study.controller;

import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.item.Book;
import net.uzen.study.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 등록 화면
     *
     * @param model
     * @return
     */
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    /**
     * 상품 등록
     *
     * @param bookForm
     * @return
     */
    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        // 저장이 되지 않아서 당황했지만, Service에 @Transactional 꼭 확인하자!
        itemService.saveItem(book);

        return "redirect:/";
    }

    /**
     * 상품 목록 조회
     *
     * @param model
     * @return
     */
    @GetMapping("/items")
    public String list(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "items/itemList";
    }

    /**
     * 상품 수정 화면
     *
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, Model model) {

        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);

        return "items/updateItemForm";
    }

    /**
     * 상품 수정
     *
     * @param itemId
     * @param form
     * @return
     */
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, BookForm form) {

//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

//        itemService.saveItem(book);
        itemService.updateItem(form.getId(), form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());

        return "redirect:/items";
    }
}