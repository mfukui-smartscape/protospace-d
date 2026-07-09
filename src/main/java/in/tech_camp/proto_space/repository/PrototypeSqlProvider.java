package in.tech_camp.proto_space.repository;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import in.tech_camp.proto_space.form.PrototypeSearchForm;

public class PrototypeSqlProvider {

    public String search(PrototypeSearchForm form) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.*, u.name AS user_name
            FROM prototypes p
            JOIN users u ON u.id = p.user_id
            WHERE 1=1
            """);

        if (form.getName() != null && !form.getName().isBlank()) {
            sql.append(" AND p.name ILIKE '%' || #{name} || '%' ");
        }
        if (form.getUserName() != null && !form.getUserName().isBlank()) {
            sql.append(" AND u.name ILIKE '%' || #{userName} || '%' ");
        }
        if (form.getTagIds() != null && !form.getTagIds().isEmpty()) {
            String placeholders = IntStream.range(0, form.getTagIds().size())
                    .mapToObj(i -> "#{tagIds[" + i + "]}")
                    .collect(Collectors.joining(","));
            sql.append(" AND p.id IN ( ")
               .append("   SELECT pt.prototype_id FROM prototypes_tags pt ")
               .append("   WHERE pt.tag_id IN (").append(placeholders).append(") ")
               .append("   GROUP BY pt.prototype_id ")
               .append("   HAVING COUNT(DISTINCT pt.tag_id) = ")
               .append(form.getTagIds().size())   // ← AND検索。ORにするならこの HAVING 行ごと削除
               .append(" ) ");
        }
        sql.append(" ORDER BY p.created_at DESC ");
        return sql.toString();
    }
}