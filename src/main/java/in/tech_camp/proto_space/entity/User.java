package in.tech_camp.proto_space.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * users テーブルに対応するエンティティ。
 * DBから取得したユーザー情報を保持する。
 * ※ password はハッシュ化された値が入る。
 */
@Data
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String profile;
    private String affiliation;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}