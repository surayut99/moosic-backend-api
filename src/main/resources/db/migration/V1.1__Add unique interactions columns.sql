ALTER TABLE interactions
ADD UNIQUE (user_id, post_id);