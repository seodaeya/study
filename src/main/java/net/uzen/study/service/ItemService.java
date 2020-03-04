package net.uzen.study.service;

import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.item.Book;
import net.uzen.study.domain.item.Item;
import net.uzen.study.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 상품 등록
     *
     * @param item
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 상품 수정
     *
     * @param itemId
     * @param book
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity, String author, String isbn) {
        Book findItem = (Book) itemRepository.findOne(itemId);
        // 엔티티에 Setter 를 열어두면 어디에서 변경되었는 지 찾기가 매우 힘들기 때문에 유지보수 차원에서라도 생성자를 통해서 변경하기를 권장한다.
        // TODO: 엔티티에 Setter 를 제거하고, 도메인 모델 패턴으로 변경하자.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        findItem.setAuthor(author);
        findItem.setIsbn(isbn);
    }

    /**
     * 상품 조회
     *
     * @param itemId
     * @return
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    /**
     * 상품 목록 조회
     *
     * @return
     */
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}