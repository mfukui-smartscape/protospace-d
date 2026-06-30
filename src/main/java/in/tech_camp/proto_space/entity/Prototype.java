package in.tech_camp.proto_space.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * prototypes テーブルに対応するエンティティ。
 * 一覧/詳細での投稿者名表示のため、JOINで取得した
 * userName を保持できるようにしている（DBカラムではない）。
 */
@Data
public class Prototype {
    private Long id;
    private String name;
    private String catchCopy;
    private String concept;
    private String imageName;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // JOINで取得する投稿者名（prototypesテーブルのカラムではない）
    private String userName;
}