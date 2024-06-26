### README файл:

#### Заголовок:
**Реализация алгоритма поиска подстроки Бойера-Мура**

#### Описание проекта:
Этот проект создан в рамках выполнения задания по курсу "Прикладные задачи BigData" в университете НИТУ МИСИС. В проекте реализован алгоритм поиска подстроки Бойера-Мура, который разработан для эффективного поиска шаблонов, особенно на устройствах, где нежелательно использование отката (backtracking), таких как магнитные ленты и жесткие диски. Алгоритм применяет эвристический подход для уменьшения количества сравнений символов в процессе поиска.

#### Описание задачи:
Текст задачи доступен по ссылке: [Rosetta Code - Boyer-Moore String Search](https://rosettacode.org/wiki/Boyer-Moore_string_search). При реализации на Scala за основу брались решения на Python. Для проверки работоспособности реализованы модульные тесты с использованием библиотеки ScalaTest.

#### Структура репозитория:
- **/src/main/**: Исходный код алгоритма на Scala.
- **/src/test/**: Модульные тесты, реализованные с использованием библиотеки ScalaTest.
- **README.md**: Описание проекта и инструкции по использованию.
- **build.sbt**: Файл конфигурации SBT (Scala Build Tool).

#### Инструкции по использованию:
1. **Сборка проекта:**
   - Убедитесь, что у вас установлен SBT (Scala Build Tool).
   - Клонируйте репозиторий на ваше локальное устройство.
   - Перейдите в директорию проекта и выполните команду `sbt compile` для сборки проекта.
   
2. **Запуск тестов:**
   - Для запуска модульных тестов используйте команду `sbt test`.
   
3. **Использование алгоритма:**
   - В исходном коде (в директории `src`) реализован алгоритм поиска подстроки Бойера-Мура. Вы можете использовать его для своих нужд, изменяя входные данные и параметры.

#### Дополнительные материалы:
- [Документация по SBT](https://www.scala-sbt.org/1.x/docs/index.html)
- [ScalaTest](http://www.scalatest.org/)

#### Цель проекта:
Этот репозиторий предназначен для демонстрации навыков программирования и работы с алгоритмами, а также использования языка Scala и библиотеки ScalaTest. Проект показывает умение реализовать и тестировать сложные алгоритмы в различных языковых средах.
