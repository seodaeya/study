package net.uzen.study.Domain.item;

import lombok.Getter;
import lombok.Setter;
import net.uzen.study.Domain.Category;

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
}