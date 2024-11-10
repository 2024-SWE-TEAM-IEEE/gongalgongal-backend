package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


네, Repository 인터페이스는 기본 키 타입이 Long에서 BIGINT로 변경되어도 그대로 유지됩니다. JPA에서 기본 키 타입이 Long인 엔티티는 MySQL의 BIGINT와 호환되기 때문에, Repository는 변경 없이 그대로 사용해도 됩니다.

변경된 SQL 스키마에 맞춰 각 엔티티 클래스를 조정한 코드는 다음과 같습니다.

1. User 엔티티
java
코드 복사
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String email;

    private String password;

    // 기본 생성자
    public User() {}

    // 생성자
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
