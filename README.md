

delete FROM public.entries where gameid=1

SELECT *  FROM public.entries where gameid=1
ORDER BY inserted asc

SELECT inserted, count(*) FROM public.entries where gameid=1
group by 1 ORDER BY inserted asc

SELECT * FROM public.entries where gameid=1
ORDER BY inserted asc limit 10000
