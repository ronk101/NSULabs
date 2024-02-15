# Мини проекты.
Из этого раздела достаточно сделать одну задачу на оценку, на которую вы
претендуете. (я выбрал вариант на `отлично`)

## Блок задач на "удовлетворительно"

### Многопоточный cp -R (вариант Дмитрия Валентиновича)
Реализуйте многопоточную программу рекурсивного копирования дерева
подкаталогов, функциональный аналог команды cp(1) с ключом -R. Программа должна
принимать два параметра – полное путевое имя корневого каталога исходного дерева
и полное путевое имя целевого дерева. Программа должна обходить исходное дерево
каталогов при помощи opendir(3C)/readdir_r(3С) и определять тип каждого найденного
файла при помощи stat(2). Для определения размера буфера для readdir_r
используйте pathconf(2) (sizeof (struct dirent) + pathconf(directory)+1).
Для каждого подкаталога должен создаваться одноименный каталог в целевом дереве
и запускаться отдельная нить, обходящая этот подкаталог. Для каждого регулярного
файла должна запускаться нить, копирующая этот файл в одноименный файл
целевого дерева при помощи open(2)/read(2)/write(2). Файлы других типов
(символические связи, именованные трубы и др.) следует игнорировать.
При копировании больших деревьев каталогов возможны проблемы с исчерпанием
лимита открытых файлов. Очень важно закрывать дескрипторы обработанных файлов
и каталогов при помощи close(2)/closedir(3C). Тем не менее, для очень больших
деревьев этого может оказаться недостаточно. Допускается обход этой проблемы при
помощи холостого цикла с ожиданием (если open(2) или readdir(3C) завершается с
ошибкой EMFILE, то допускается сделать sleep(3C) и повторить попытку открытия
через некоторое время).
Обратите также внимание, что значения дескрипторов открытых файлов могут
переиспользоваться, т.е. в разные моменты времени один и тот же дескриптор может
указывать на разные файлы. Чтобы избежать связанных с этим проблем, избегайте
передачи дескрипторов между нитями. Вся работа с дескриптором от создания до
закрытия должна происходить в одной нити.
Дополнительное упражнение: при помощи команды time(1) сравните ресурсы,
потребляемые вашей программой и командой cp -R при копировании одного и того же
дерева каталогов. Объясните наблюдаемые различия. Каким образом их можно
устранить? Следует ли вообще реализовать копирование файлов таким способом и
если да, то в каких условиях?

## Блок задач на "хорошо"

### Реализуйте многопоточный HTTP-proxy (версия HTTP 1.0).

Прокси должен принимать соединения на 80 порту и перенаправлять их на требуемый сервер. Вся обработка соединения должна происходить в отдельном потоке.

## [Блок задач на "отлично"](./http-proxy)

### Реализуйте многопоточный `кэширующий` HTTP-proxy (версия HTTP 1.0). 

Прокси должен принимать соединения на 80 порту и возвращать данные из кэша. В случае если для запроса нет записей в кэше, то должен быть создан отдельный поток, который загрузит в кэш требуемые данные. Данные должны пересылаться клиенту как только они начали появляться в кэше.