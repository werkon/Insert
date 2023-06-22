

delete FROM public.entries where gameid=1

SELECT *  FROM public.entries where gameid=1
ORDER BY inserted asc

SELECT inserted, count(*) FROM public.entries where gameid=1
group by 1 ORDER BY inserted asc

SELECT * FROM public.entries where gameid=1
ORDER BY inserted asc limit 10000

SELECT result, count(*) FROM public.entries where gameid=1
group by 1 order by 1

SELECT inserted, result FROM public.entries where gameid=1
order by 1, 2
limit 100

SELECT inserted, result, count(*) FROM public.entries where gameid=1
group by 1,2
order by 1, 2
limit 100

C:\Install\jdk-19.0.2\bin\java.exe -jar D:\Java\Insert\Insert.jar -Xmx48g -Xms12g

start /BELOWNORMAL C:\Install\jdk-19.0.2\bin\java.exe -Xmx48g -jar D:\Java\Insert\Insert.jar
