CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT uuid_generate_v4(),
    uid TEXT,
    username TEXT UNIQUE,
    display_name TEXT NOT NULL,
    email TEXT NOT NULL,
    bio TEXT DEFAULT '',
    genres TEXT[],
    photo_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_auth_tokens (
    id uuid DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    access_token TEXT NOT NULL,
    refresh_token TEXT NOT NULL,
    expired_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),

    UNIQUE(user_id),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS posts (
    id uuid DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    img_url TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS interactions (
    id uuid DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    post_id uuid NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE IF NOT EXISTS playlists (
    id uuid DEFAULT uuid_generate_v4(),
    post_id uuid NOT NULL,
    name TEXT NOT NULL,
    mood TEXT NOT NULL,
    keyword TEXT NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE IF NOT EXISTS musics (
    id uuid DEFAULT uuid_generate_v4(),
    playlist_id uuid NOT NULL,
    title TEXT NOT NULL,
    artists TEXT[] NOT NULL,
    album TEXT NOT NULL,
    track_url TEXT NOT NULL,
    artist_urls TEXT[] NOT NULL,
    album_url TEXT NOT NULL,
    preview_url TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (id),
    FOREIGN KEY (playlist_id) REFERENCES playlists (id)
);

CREATE TABLE IF NOT EXISTS exposed_posts (
    id uuid DEFAULT uuid_generate_v4(),
    post_id uuid NOT NULL,
    playlist_id uuid NOT NULL,
    music_id uuid NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),

    UNIQUE(post_id, playlist_id, music_id),
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (playlist_id) REFERENCES playlists (id),
    FOREIGN KEY (music_id) REFERENCES musics (id)
);

-- function for update timestamptz on updated_at column
CREATE FUNCTION update_timestamptz() RETURNS TRIGGER
    LANGUAGE plpgsql
    AS $$
        BEGIN
            NEW.updated_at = now();
            RETURN NEW;
        END;
       $$;

-- trigger event
CREATE TRIGGER trigger_update_timestamptz
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamptz();

CREATE TRIGGER trigger_update_timestamptz
    BEFORE UPDATE ON user_auth_tokens
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamptz();

CREATE TRIGGER trigger_update_timestamptz
    BEFORE UPDATE ON posts
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamptz();

CREATE TRIGGER trigger_update_timestamptz
    BEFORE UPDATE ON playlists
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamptz();

CREATE TRIGGER trigger_update_timestamptz
    BEFORE UPDATE ON musics
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamptz();

CREATE TRIGGER trigger_update_timestamptz
    BEFORE UPDATE ON exposed_posts
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamptz();
