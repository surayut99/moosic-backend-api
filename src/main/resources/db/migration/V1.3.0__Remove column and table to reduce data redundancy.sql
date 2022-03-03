-- replace track url with its spotify id
update musics
set track_url = sq.spotify_id
from (select id, split_part(m.track_url, 'track/', 2) as spotify_id from musics m) as sq
where musics.id = sq.id;

-- rename track_url column to spotify id
alter table musics
rename column track_url to spotify_id;

-- drop unnecessary columns
alter table musics
drop column if exists title,
drop column if exists artists,
drop column if exists album,
drop column if exists artist_urls,
drop column if exists album_url,
drop column if exists preview_url,
drop column if exists album_cover_url;
