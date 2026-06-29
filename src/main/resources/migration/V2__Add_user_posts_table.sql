-- Add user posts table for social media functionality
-- Based on UserPostsTBL JPA entity definition

CREATE TABLE user_posts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    creation_date TIMESTAMP(6) NOT NULL,
    last_updated TIMESTAMP(6) NOT NULL,
    status VARCHAR(20) NOT NULL,
    likes_count INTEGER NOT NULL,
    comments_count INTEGER NOT NULL,
    CONSTRAINT fk_posts_user_login FOREIGN KEY (user_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_user_posts_user_id ON user_posts(user_id);
CREATE INDEX idx_user_posts_status ON user_posts(status);
CREATE INDEX idx_user_posts_creation_date ON user_posts(creation_date);
CREATE INDEX idx_user_posts_last_updated ON user_posts(last_updated);
CREATE INDEX idx_user_posts_user_status ON user_posts(user_id, status);
CREATE INDEX idx_user_posts_likes_count ON user_posts(likes_count);
CREATE INDEX idx_user_posts_comments_count ON user_posts(comments_count);

-- Create full-text index for content search (PostgreSQL specific)
-- Note: This requires PostgreSQL and the pg_trgm extension for efficient text search
-- CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- CREATE INDEX idx_user_posts_content_gin ON user_posts USING gin(content gin_trgm_ops);

-- Add comments for documentation
COMMENT ON TABLE user_posts IS 'Table for storing user-generated posts with engagement metrics';
COMMENT ON COLUMN user_posts.id IS 'Primary key for the post';
COMMENT ON COLUMN user_posts.user_id IS 'Foreign key referencing the user who created the post';
COMMENT ON COLUMN user_posts.content IS 'The actual post content text stored as TEXT (maps to CLOB in JPA, max 2000 characters)';
COMMENT ON COLUMN user_posts.creation_date IS 'Timestamp when the post was created';
COMMENT ON COLUMN user_posts.last_updated IS 'Timestamp when the post was last modified';
COMMENT ON COLUMN user_posts.status IS 'Post status (ACTIVE, DELETED, DRAFT)';
COMMENT ON COLUMN user_posts.likes_count IS 'Number of likes the post has received';
COMMENT ON COLUMN user_posts.comments_count IS 'Number of comments the post has received';
