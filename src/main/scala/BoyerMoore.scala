object BoyerMoore {

  private val ALPHABET_SIZE = 256

  def alphabetIndex(c: Char): Int = c.toInt

  def matchLength(S: String, idx1: Int, idx2: Int): Int = {
    var i = idx1
    var j = idx2
    var matchCount = 0

    while (i < S.length && j < S.length && S(i) == S(j)) {
      matchCount += 1
      i += 1
      j += 1
    }

    matchCount
  }

  def fundamentalPreprocess(S: String): Array[Int] = {
    if (S.isEmpty) {
      Array.empty
    } else if (S.length == 1) {
      Array(1)
    } else {
      val z = new Array[Int](S.length)
      z(0) = S.length
      z(1) = matchLength(S, 0, 1)

      for (i <- 2 to 1 + z(1)) {
        z(i) = z(1) - i + 1
      }

      var l = 0
      var r = 0

      for (i <- 2 + z(1) until S.length) {
        if (i <= r) {
          val k = i - l
          val b = z(k)
          val a = r - i + 1

          if (b < a) {
            z(i) = b
          } else {
            z(i) = a + matchLength(S, a, r + 1)
            l = i
            r = i + z(i) - 1
          }
        } else {
          z(i) = matchLength(S, 0, i)

          if (z(i) > 0) {
            l = i
            r = i + z(i) - 1
          }
        }
      }

      z
    }
  }

  def badCharacterTable(S: String): Array[Array[Int]] = {
    if (S.isEmpty) {
      Array.fill(ALPHABET_SIZE)(Array(-1))
    } else {
      val R = Array.fill(ALPHABET_SIZE)(Array(-1))
      val alpha = Array.fill(ALPHABET_SIZE)(-1)

      for ((c, i) <- S.zipWithIndex) {
        alpha(alphabetIndex(c)) = i

        for (j <- alpha.indices) {
          R(j) :+= alpha(j)
        }
      }

      R
    }
  }

  def goodSuffixTable(S: String): Array[Int] = {
    val L = Array.fill(S.length)(-1)
    val N = fundamentalPreprocess(S.reverse).reverse

    for (j <- 0 until S.length - 1) {
      val i = S.length - N(j)
      if (i != S.length) {
        L(i) = j
      }
    }

    L
  }

  def fullShiftTable(S: String): Array[Int] = {
    val F = new Array[Int](S.length)
    val Z = fundamentalPreprocess(S)

    var longest = 0

    for ((zv, i) <- Z.reverse.zipWithIndex) {
      longest = math.max(zv, longest)
      F(S.length - i - 1) = longest
    }

    F
  }

  def stringSearch(P: String, T: String): List[Int] = {
    if (P.isEmpty || T.isEmpty || T.length < P.length) {
      List.empty
    } else {
      var matches = List.empty[Int]
      val R = badCharacterTable(P)
      val L = goodSuffixTable(P)
      val F = fullShiftTable(P)

      var k = P.length - 1
      var previousK = -1

      while (k < T.length) {
        var i = P.length - 1
        var h = k

        while (i >= 0 && h > previousK && P(i) == T(h)) {
          i -= 1
          h -= 1
        }
        if (i == -1 || h == previousK) {
          matches :+= k - P.length + 1
          k += P.length - F(1)
          if (P.length > 1) {
            k = math.max(k, previousK + 1)
          }
        } else {
          val charShift = i - R(alphabetIndex(T(h)))(i)

          val suffixShift =
            if (i + 1 == P.length) {
              1
            } else if (L(i + 1) == -1) {
              P.length - F(i + 1)
            } else {
              P.length - 1 - L(i + 1)
            }

          val shift = math.max(charShift, suffixShift)
          previousK = if (shift >= i + 1) k else previousK
          k += shift
          k = math.max(k, previousK + 1)  // Добавлен этот блок для учета значения из таблицы L

        }
      }

      matches
    }
  }
}
