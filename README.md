
start /LOW java.exe -Xmx48g -jar D:\Java\Insert\Insert.jar

SELECT inserted, count(*) FROM public.entries where gameid=1
group by 1 ORDER BY inserted asc

nut

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

1 4
2 25
3 121
4 568
5 1922
6 7120
7 20718    141
8 67190    103
9 174809    57
10 503866   46
11 1161784  30
12 3010664  26
13 6252187  18

1 4
2 25
3 121
4 568
5 2144
6 7638
7 25718
8 77715
9 239959
10 634338
11 1784788
12 4200899
13 10881076


3010664
110768