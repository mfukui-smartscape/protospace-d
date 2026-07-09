package in.tech_camp.proto_space.form;

import java.util.List;

import lombok.Data;

@Data
public class PrototypeSearchForm {
    private String name;          // プロトタイプ名（部分一致）
    private String userName;      // 投稿者名（部分一致）
    private List<Long> tagIds;    // タグ（完全一致・AND）

    public boolean isEmpty() {
        return (name == null || name.isBlank())
            && (userName == null || userName.isBlank())
            && (tagIds == null || tagIds.isEmpty());
    }
}