object BoyerMoore {
  def alphabetIndex(c: Char): Int = c.toInt

  def matchLength(S: String, idx1: Int, idx2: Int): Int = {
    val text = S
    var i = idx1
    var j = idx2
    while (i < text.length && j < text.length && text(i) == text(j)) {
      i += 1
      j += 1
    }
    i - idx1
  }

  /** Z(i) is the length of the prefix matching the suffix starting at i. */
  def fundamentalPreprocess(S: String): Array[Int] = {
    val text = S
    val z = new Array[Int](text.length)
    if (text.nonEmpty) {
      z(0) = text.length
      var left = 0
      var right = 0
      for (i <- 1 until text.length) {
        if (i <= right) z(i) = math.min(right - i + 1, z(i - left))
        while (i + z(i) < text.length && text(z(i)) == text(i + z(i))) {
          z(i) += 1
        }
        if (i + z(i) - 1 > right) {
          left = i
          right = i + z(i) - 1
        }
      }
    }
    z
  }

  /** Legacy 256-row table for Latin-1 patterns; search itself supports UTF-16. */
  def badCharacterTable(S: String): Array[Array[Int]] = {
    require(S.forall(c => alphabetIndex(c) < 256),
      "badCharacterTable accepts only Latin-1 characters; use stringSearch for other text")
    val table = Array.fill(256, S.length + 1)(-1)
    for ((character, row) <- badCharacterRows(S)) {
      table(alphabetIndex(character)) = row
    }
    table
  }

  /** Each row contains the last occurrence strictly before a pattern index. */
  private def badCharacterRows(pattern: String): Map[Char, Array[Int]] = {
    pattern.distinct.iterator.map { character =>
      val positions = new Array[Int](pattern.length + 1)
      var last = -1
      for (i <- pattern.indices) {
        positions(i) = last
        if (pattern(i) == character) last = i
      }
      positions(pattern.length) = last
      character -> positions
    }.toMap
  }

  def goodSuffixTable(S: String): Array[Int] = {
    val pattern = S
    val table = Array.fill(pattern.length)(-1)
    // Search compares UTF-16 units, so reverse Char units rather than code points.
    val suffixLengths = fundamentalPreprocess(pattern.toCharArray.reverse.mkString).reverse
    for (j <- 0 until pattern.length - 1) {
      val i = pattern.length - suffixLengths(j)
      if (i != pattern.length) table(i) = j
    }
    table
  }

  def fullShiftTable(S: String): Array[Int] = {
    val pattern = S
    val table = new Array[Int](pattern.length)
    val z = fundamentalPreprocess(pattern)
    var longest = 0
    for (i <- pattern.indices.reverse) {
      if (z(i) == pattern.length - i) longest = z(i)
      table(i) = longest
    }
    table
  }

  /** Find all matches, including overlaps, using zero-based UTF-16 offsets. */
  def stringSearch(P: String, T: String): List[Int] = {
    val pattern = P
    val text = T
    if (pattern.isEmpty || text.length < pattern.length) return List.empty

    val matches = List.newBuilder[Int]
    val badCharacters = badCharacterRows(pattern)
    val goodSuffixes = goodSuffixTable(pattern)
    val fullShifts = fullShiftTable(pattern)
    var end = pattern.length - 1

    while (end < text.length) {
      var i = pattern.length - 1
      var position = end
      while (i >= 0 && pattern(i) == text(position)) {
        i -= 1
        position -= 1
      }
      if (i < 0) {
        matches += end - pattern.length + 1
        val suffix = if (pattern.length > 1) fullShifts(1) else 0
        end += pattern.length - suffix
      } else {
        val previous = badCharacters.get(text(position)).fold(-1)(_(i))
        val characterShift = i - previous
        val suffixShift =
          if (i + 1 == pattern.length) 1
          else if (goodSuffixes(i + 1) == -1) pattern.length - fullShifts(i + 1)
          else pattern.length - 1 - goodSuffixes(i + 1)
        end += math.max(characterShift, suffixShift)
      }
    }
    matches.result()
  }
}
