package net.uzen.study.service;

import lombok.RequiredArgsConstructor;
import net.uzen.study.Domain.item.Item;
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
     * @param item
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 상품 조회
     * @param itemId
     * @return
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    /**
     * 상품 목록 조회
     * @return
     */
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}