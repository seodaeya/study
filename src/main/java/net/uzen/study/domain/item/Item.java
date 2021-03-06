package net.uzen.study.domain.item;

import lombok.Getter;
import lombok.Setter;
import net.uzen.study.domain.Category;
import net.uzen.study.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    /**
     * 실무에서는 多:多 는 거의 사용하기 힘들다.
     * <p>테이블 데이터 갱신이라도 되면, updateDt 라도 갱신해주어야 하지만 컬럼을 추가할 수 없다.
     * <p>또한 세밀하게 쿼리를 실행하기 어렵다.
     * <p>결론, 多:多 관계는 OneToMany, ManyToOne 으로 풀어서 사용하는 것이 좋다.
     */
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    /**
     * 엔티티가 핵심 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 "도메인 모델 패턴"이라 한다.
     * <p>재고 증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     * @param quantity
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}