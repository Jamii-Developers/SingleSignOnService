-- Fix the content column type in user_posts table for PostgreSQL
-- This migration addresses the Hibernate schema validation error

-- For PostgreSQL, TEXT type maps to CLOB in JPA/Hibernate
-- If the column was created as VARCHAR, alter it to TEXT
DO $$
BEGIN
    -- Check if column is VARCHAR and change to TEXT if needed
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'user_posts' 
        AND column_name = 'content' 
        AND data_type = 'character varying'
    ) THEN
        ALTER TABLE user_posts ALTER COLUMN content TYPE TEXT;
        RAISE NOTICE 'Changed content column from VARCHAR to TEXT';
    ELSE
        RAISE NOTICE 'Content column is already TEXT or does not need modification';
    END IF;
END $$;

-- Update the comment to reflect the correct type
COMMENT ON COLUMN user_posts.content IS 'The actual post content text stored as TEXT (maps to CLOB in JPA, max 2000 characters)';

-- Note: In PostgreSQL, TEXT type maps to CLOB in JPA/Hibernate
-- This migration ensures the column type is compatible with @Lob annotation
