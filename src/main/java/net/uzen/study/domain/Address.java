package net.uzen.study.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

/**
 * Entity 설계
 * <p>값 타입은 변경 불가능하게 하고, 생성자를 통해 값을 모두 초기화해서 변경 불가능하도록 만든다.
 * <p>※ JPA가 이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 프록시, 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문이다.
 */
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /**
     * public 을 사용하면 여러군데에서 호출을 할 수 있기 때문에,
     * <p>JPA 스펙에서는 public, protected 까지 허용을 해준다.
     * <p>※ 정해진 규약은 아니지만, public 이 아닌 protected 면, 개발자들도 호출을 자제하게 된다.
     */
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}