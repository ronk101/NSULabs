# 2. Управление адресным пространством:
### a. Напишите программу, которая:
    i. выводит pid процесса;
    ii. ждет одну секунду;
    iii. делает exec(2) самой себя;
    iv. выводит сообщение “Hello world”
### b. Понаблюдайте за выводом программы и содержимым соответствующего файла `/proc/<pid>/maps`. Объясните происходящее.
### c. Напишите программу, которая:
    i. выводит pid процесса;
    ii. ждет 10 секунд (подберите паузу чтобы успеть начать мониторить
    адресное пространство процесса, например, watch cat
    /proc/<pid>/maps);
    iii. напишите функцию, которая будет выделять на стеке массив
    (например, 4096 байт) и рекурсивно вызывать себя;
    iv. понаблюдайте как изменяется адресное пространство процесса
    (стек);
    v. напишите цикл, в котором на каждой итерации будет выделяться
    память на куче (подберите размер буфера сами). Используйте
    секундную паузу между итерациями.
    vi. понаблюдайте как изменится адресное пространство процесса
    (heap);
    vii. освободите занятую память.
    viii. присоедините к процессу еще один регион адресов размером в 10
    страниц (используйте mmap(2) с флагом ANONYMOUS).
    ix. понаблюдайте за адресным пространством.
    x. измените права доступа к созданному региону и проверьте какая
    будет реакция, если их нарушить:
        1. запретите читать данные и попробуйте прочитать из региона.
        2. запретите писать и попробуйте записать.
    xi. попробуйте перехватить сигнал SIGSEGV.
    xii. отсоедините страницы с 4 по 6 в созданном вами регионе.
    xiii. понаблюдайте за адресным пространством.

# FAQ 

`ulimit -a` - посмотреть доступные ресурсы для программы

`watch -d -n1 cat @/proc/<pid>/maps` - 
мониторинг адресного пространства в реальном времени

``exec(2)`` - системный вызов, который:

    1. Создает новое адресное пространство внутри нашего процесса
    2. Сохраняет pid процесса
    3. Сохраняет все открытые дескрипторы

Разновидности `exec(2)`:

 * `l` (список). Аргументы командной строки передаются в форме списка arg0, arg1.... argn, NULL. Эту форму используют, если количество аргументов известно;
 * `v` (vector). Аргументы командной строки передаются в форме вектора argv[]. Отдельные аргументы адресуются через argv [0], argv [1]... argv [n]. Последний аргумент (argv [n]) должен быть указателем NULL;
 * `p` (path). если указанное имя файла не содержать символ косой черты `(/)`, то путь ищется в переменной среде окружения PATH. Если эта переменная не определена, список путей по умолчанию представляет собой список, включающий каталоги;
 * `e` (среда). Функция ожидает список переменных среды в виде вектора (envp []) и не использует текущей среды.
 