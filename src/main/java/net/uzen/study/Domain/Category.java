package net.uzen.study.Domain;

import lombok.Getter;
import lombok.Setter;
import net.uzen.study.Domain.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /**
     * ManyToMany 는 중간 테이블에 컬럼을 추가할 수 없고, 세밀하게 쿼리를 실행하기 어렵기 때문에 실무에서는 사용하는데 한계가 있다.
     * <p>결국 1:多, 多:1 매핑으로 변경해서 사용하는 것을 권장한다.
     */
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"), // category 쪽 연결 컬럼
            inverseJoinColumns = @JoinColumn(name = "item_id") // item 쪽 연결 컬럼
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}