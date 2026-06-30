package in.tech_camp.proto_space.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * comments テーブルに対応するエンティティ。
 * 詳細ページでのコメント投稿者名表示のため、
 * JOINで取得した userName を保持できるようにしている。
 */
@Data
public class Comment {
    private Long id;
    private String content;
    private Long userId;
    private Long prototypeId;
    private LocalDateTime createdAt;

    // JOINで取得するコメント投稿者名（commentsテーブルのカラムではない）
    private String userName;
}