# Autocomplete Search System (VK internship)

## Постановка задачи

Нужно реализовать систему автоматического дополнения запросов для поисковой системы. Она должна на введённый пользователем запрос
возвращать не более 5 наилучших подсказок.

Система инициализируется списком фраз на естественном языке, это может быть что угодно, например,
лог запросов, заголовки фильмов, названия товаров.

Кроме текста фраз инициализацию можно обогащать дополнительной информацией, например, количеством упоминаний этой фразы,
рейтингом фильма, ценой товара и так далее.

## Идея решения

Понятное дело, что делать полный перебор асимптотически неэффективно, соответственно, лучше сделать индекс по которому можно производить быстрый поиск.

Это решение основано на префиксном дереве с оптимизацией памяти ([radix tree](https://en.wikipedia.org/wiki/Radix_tree)).
Поскольку мы базируемся на изначальном списке поисковых запросов, фильмов, книг и т.д. (в коде для таких сущностей подобрано имя content)
дерево можно делать на словах в контенте. В каждой вершине дерева хранить весь контент,
который содержит слово соответствующее вершине (переходы по ребрам не учитывают регистр слов).

Обходить такое дерево очень легко и быстро ибо мы стразу "отрезаем" вершины, которые не соответствуют искомому слову.
Более того, можно делать это по некоторому [автомату](https://en.wikipedia.org/wiki/Automata_theory), благодаря чему можно понимать было ли слово введенное пользователем с опечаткой.

Ниже приведен пример индекса основанного на контенте "Apple", "Apple tree", "Traffic", "App"

![](img/tree_example.png)

Так в вершине 1 будет храниться контент "App", в вершине 2 - "Apple" и "Apple tree", 3 - "Apple tree", 4 - "Traffic".  

## Реализация

Код можно разбить на 3 основных компонента:
* [automata](https://github.com/priamoryki/Autocomplete-Search-System-VK/tree/main/src/search/autocomplete/automata) - пакет реализующий автоматы для поиска
* [index](https://github.com/priamoryki/Autocomplete-Search-System-VK/tree/main/src/search/autocomplete/index) - пакет реализующий структуру индекса (префиксное дерево) и поиск в нем
* [content](https://github.com/priamoryki/Autocomplete-Search-System-VK/tree/main/src/search/content) - пакет для упорядочивания всего контента по релевантности (кол-ву запросов, рейтингу и т.д.)

Так же реализован простой [пример](https://github.com/priamoryki/Autocomplete-Search-System-VK/blob/main/src/Main.java) использования DataIndex, который парсит заголовки фильмов из файла и записывает их.  

Замечание: Реализация не поддерживает многопоточный запуск, если мы хотим это сделать, требуется добавить mutex на запись и чтение.

### Примеры запросов с ответами:
```
Enter your query: Romeo
Suggestions to your query: 
1) Romeo and Juliet
2) Julieta y Romeo
3) Romeo is Bleeding
4) Romeo und Julia im Schnee
5) William Shakespeare's Romeo and Juliet
```

```
Enter your query: Romeo Juliet
Suggestions to your query: 
1) Romeo and Juliet
2) William Shakespeare's Romeo and Juliet
```

### Пример запроса с опечаткой:
```
Enter your query: Julie
Suggestions to your query: 
1) Romeo and Juliet
2) Julius Ceasar
3) Celine and Julie Go Boating
4) Julie
5) Miss Julie
```

Замечу, что, например, "Miss Julie" идет выше "William Shakespeare's Romeo and Juliet" ибо имеет наибольшее совпадение по буквам с изначальным запросом.
Однако, если изменить приоритет добавив в индекс еще несколько запросов "William Shakespeare's Romeo and Juliet" ситуация резко меняется:
```
Enter your query: Julie
Suggestions to your query: 
1) Romeo and Juliet
2) William Shakespeare's Romeo and Juliet
3) Julius Ceasar
4) Celine and Julie Go Boating
5) Julie
```
