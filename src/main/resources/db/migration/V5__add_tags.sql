CREATE TABLE tags (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE prototypes_tags (
    id           BIGSERIAL PRIMARY KEY,
    prototype_id BIGINT NOT NULL REFERENCES prototypes(id) ON DELETE CASCADE,
    tag_id       BIGINT NOT NULL REFERENCES tags(id)       ON DELETE CASCADE,
    UNIQUE (prototype_id, tag_id)
);

INSERT INTO tags (name) VALUES
('Ruby'),('JavaScript'),('TypeScript'),('Python'),('PHP'),
('Java'),('Kotlin'),('Swift'),('Go'),('Rust'),
('C'),('C++'),('C#'),('HTML/CSS'),('SQL');