create or replace view top_5_posts as

with like_counts as (
select ep.id, pl.mood, count(i) as like_count, ep.updated_at from exposed_posts ep
left join playlists pl on ep.playlist_id = pl.id
left join interactions i on ep.post_id = i.post_id
group by ep.id, pl.mood)

select ep.* FROM
	(select *, row_number() over(partition by mood order by updated_at desc) rn
	from like_counts lc
	where lc.like_count = (
		select max(lc2.like_count)
		from like_counts lc2
		where lc2.mood = lc.mood
	)) as max_counts
inner join exposed_posts ep on ep.id = max_counts.id
where max_counts.rn = 1
order by max_counts.like_count desc
limit 5
