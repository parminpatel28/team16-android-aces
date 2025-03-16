-- Remove existing constraints
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pkey CASCADE;

-- Change the column type
ALTER TABLE users ALTER COLUMN user_id TYPE VARCHAR;

-- Add back the primary key constraint
ALTER TABLE users ADD PRIMARY KEY (user_id);

-- Update the sequence if it exists
DROP SEQUENCE IF EXISTS users_user_id_seq; 