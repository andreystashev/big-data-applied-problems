# Boyer–Moore string search in Scala

A coursework implementation of Boyer–Moore substring search for the **Big Data
Applied Problems** course at the National University of Science and Technology
MISIS. The project explores the bad-character and good-suffix heuristics and
checks the search against a straightforward reference implementation.

**Status:** educational algorithm project. This repository contains a string
search implementation and tests; it does not include a distributed data pipeline
or a production search service.

## Quick start

Install a JDK (Java 17 is used in CI) and [sbt](https://www.scala-sbt.org/).
The repository pins sbt 1.9.8 and Scala 2.13.12. The first build downloads its
compiler and dependencies from Maven repositories.

```sh
git clone https://github.com/andreystashev/big-data-applied-problems.git
cd big-data-applied-problems
sbt test
sbt run
```

`Main` runs three fixed examples and prints the positions of `put`, `and`, and
`alfalfa`. It does not read command-line arguments or input files.

## Using the algorithm

In `sbt console`, call:

```scala
BoyerMoore.stringSearch("aba", "abababa") // List(0, 2, 4)
BoyerMoore.stringSearch("мир", "мир, мир!") // List(0, 5)
BoyerMoore.stringSearch("xyz", "example") // List()
```

The first argument is the pattern and the second is the text. The result contains
all zero-based starting positions in ascending order, including overlapping
matches. An empty pattern returns an empty list. Matching is case-sensitive and
uses Scala/JVM UTF-16 `Char` values: Cyrillic and other non-Latin text are
supported, but positions are UTF-16 offsets, not Unicode code-point or grapheme
indices. No normalization or locale-specific matching is performed.

## Implementation and checks

- `src/main/scala/BoyerMoore.scala` contains Z preprocessing, bad-character,
  good-suffix and full-shift tables, and the search loop.
- `src/main/scala/Main.scala` contains the coursework demonstration.
- `src/test/scala/BoyerMooreSpec.scala` covers the original examples, empty
  inputs, one-character patterns, repeated prefixes, overlapping matches and
  non-Latin characters. It also compares results with a reference search over
  exhaustive short binary and multi-emoji strings, plus seeded mixed-character
  and surrogate-unit examples.
- `.github/workflows/ci.yml` runs `sbt test` for pushes and pull requests.

Search uses an internal bad-character table with one row per distinct pattern
character. Building it takes memory proportional to the pattern length times the
number of distinct characters. The original public helper signatures are
retained, including named arguments such as `stringSearch(P = "aba", T = "abababa")`.
The legacy `badCharacterTable(S)` helper returns a 256-row array and accepts only
Latin-1 patterns; it rejects other characters with an argument error. Call
`stringSearch` for unrestricted UTF-16 input. This implementation prioritizes readable coursework code; it has no
performance benchmark and makes no claim of outperforming JVM string search.

## Background

The assignment and the Python examples that informed the original Scala port
are described at [Rosetta Code: Boyer–Moore string
search](https://rosettacode.org/wiki/Boyer-Moore_string_search). Tests use
[ScalaTest](https://www.scalatest.org/).
