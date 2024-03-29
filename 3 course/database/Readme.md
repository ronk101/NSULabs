# Практика LibreOffice Base

## Задание

Необходимо спроектировать и запрограммировать приложение.

Решение заданий, использующих объекты базы данных, предполагает выполнение следую-
щих этапов:

    1. Проектирование инфологической модели задачи.
    Определение сущностей, атрибутов сущностей, идентифицирующих атрибутов, связей
    между сущностями.
    При проектировании должны учитываться требования гибкости структур для выполнения
    перечисленных запросов и не избыточного хранения данных.

    2. Проектирование схемы базы данных.

Описание схем таблиц, типов (доменов) атрибутов, определение ограничений целостно-
сти.

В заданиях дана некоторая спецификация решаемой задачи. Спецификация не предполагает
оптимального определения структур данных, но задает полный перечень хранимой в базе данных
информации и выполняемых программой функций.

## Требования к данным

Данные, которыми будут наполняться таблицы БД, должны быть читаемыми и осмыслен-
ными. То есть, не допускается заполнение полей следующим образом:

поле Ф.И.О. – «фывфыв»,
поле «Описание работы» – «апкцуку».
В таблицы баз данных необходимо ввести не менее 7-ми объектов каждого вида.



## Задание №4. «Поваренная книга»

### Структура данных

**Сущности:**

    1. Продукты
        - Идентификатор продукта
        - Название
        - Единица измерения
        - Цена за единицу измерения
        - Калорийность
    2. Рецепты
        - Идентификатор рецепта
        - Название блюда
        - Кол-во персон
        - Идентификатор категории блюда
    3. Рецепты-продукты
        - Идентификатор рецепта
        - Идентификатор продукта
        - Кол-во единиц данного продукта
    4. Категории блюд
        - Идентификатор категории
        - Наименование категории
### Комментарии:
Единицей измерения продуктов могут быть следующие величины: например, 1 кг, 1 литр, 1
куб. см. и т.д.

### Функциональность

Запрограммировать:
1. формы ввода новых и редактирования имеющихся данных в таблицах.
2. вывод на экран рецептов по категориям (отсортировать по категориям).
3. подсчет сметы для каждого блюда на N персон.
4. вывод всех блюд в которых используется заданный продукт.
