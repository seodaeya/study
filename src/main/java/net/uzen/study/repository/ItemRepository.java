package net.uzen.study.repository;

import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.item.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 상품 등록
     *
     * @param item
     */
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    /**
     * 상품 조회
     *
     * @param itemId
     * @return
     */
    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }

    /**
     * 전체 상품 조회
     *
     * @return
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}