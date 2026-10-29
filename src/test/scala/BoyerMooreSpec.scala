import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BoyerMooreSpec extends AnyFlatSpec with Matchers {
  private def reference(pattern: String, text: String): List[Int] = {
    if (pattern.isEmpty) List.empty
    else (0 to text.length - pattern.length).filter(text.startsWith(pattern, _)).toList
  }

  "BoyerMoore.stringSearch" should "find the original coursework examples" in {
    BoyerMoore.stringSearch("put", "inputoutput") shouldEqual List(2, 8)
    BoyerMoore.stringSearch("and", "randomtextandmoretext") shouldEqual List(1, 10)
    BoyerMoore.stringSearch("alfalfa", "falfalfaisgoodfora") shouldEqual List(1)
  }

  it should "return an empty list for absent, empty, or oversized patterns" in {
    BoyerMoore.stringSearch("xyz", "nocorrespondence") shouldEqual List.empty
    BoyerMoore.stringSearch("", "anytext") shouldEqual List.empty
    BoyerMoore.stringSearch("any", "") shouldEqual List.empty
    BoyerMoore.stringSearch("abcd", "abc") shouldEqual List.empty
  }

  it should "handle single-character and overlapping repeated-prefix matches" in {
    BoyerMoore.stringSearch("a", "banana") shouldEqual List(1, 3, 5)
    BoyerMoore.stringSearch("aa", "aaaaa") shouldEqual List(0, 1, 2, 3)
    BoyerMoore.stringSearch("aba", "abababa") shouldEqual List(0, 2, 4)
    BoyerMoore.stringSearch("abcab", "abcabcab") shouldEqual List(0, 3)
  }

  it should "handle characters outside the Latin-1 alphabet" in {
    BoyerMoore.stringSearch("мир", "мир, мир!") shouldEqual List(0, 5)
    BoyerMoore.stringSearch("a", "界a界") shouldEqual List(1)
    BoyerMoore.stringSearch("🙂", "a🙂🙂") shouldEqual List(1, 3)
  }

  it should "match a reference search for all short binary strings" in {
    def words(length: Int): Seq[String] =
      if (length == 0) Seq("")
      else for (prefix <- words(length - 1); c <- "ab") yield prefix + c

    val texts = (0 to 7).flatMap(words)
    val patterns = (0 to 5).flatMap(words)
    for (text <- texts; pattern <- patterns) {
      withClue(s"pattern=$pattern text=$text: ") {
        BoyerMoore.stringSearch(pattern, text) shouldEqual reference(pattern, text)
      }
    }
  }

  it should "match a reference search with multiple supplementary characters" in {
    val alphabet = Seq("a", "🙂", "🙃")
    def words(length: Int): Seq[String] =
      if (length == 0) Seq("")
      else for (prefix <- words(length - 1); c <- alphabet) yield prefix + c

    for (text <- (0 to 4).flatMap(words); pattern <- (0 to 3).flatMap(words)) {
      BoyerMoore.stringSearch(pattern, text) shouldEqual reference(pattern, text)
    }

    // Reversing this pattern by code points previously skipped its match.
    BoyerMoore.stringSearch("\ud83d\ude42\ude42", "a\ud83d\ude42\ude42") shouldEqual List(1)

    // A JVM String may contain unpaired surrogate units as well.
    val random = new scala.util.Random(43)
    val units = "ab\ud83d\ude42\ude43"
    for (_ <- 0 until 1000) {
      def word(length: Int): String =
        (0 until length).map(_ => units(random.nextInt(units.length))).mkString
      val text = word(random.nextInt(40))
      val pattern = word(random.nextInt(8))
      BoyerMoore.stringSearch(pattern, text) shouldEqual reference(pattern, text)
    }
  }

  it should "match a reference search on reproducible mixed-character inputs" in {
    val random = new scala.util.Random(42)
    val alphabet = "abcаб界\u0000"
    for (_ <- 0 until 1000) {
      def word(length: Int): String =
        (0 until length).map(_ => alphabet(random.nextInt(alphabet.length))).mkString
      val text = word(random.nextInt(80))
      val pattern = word(random.nextInt(12))
      BoyerMoore.stringSearch(pattern, text) shouldEqual reference(pattern, text)
    }
  }

  "The public helpers" should "retain their original signatures and named arguments" in {
    BoyerMoore.stringSearch(P = "aba", T = "abababa") shouldEqual List(0, 2, 4)
    BoyerMoore.alphabetIndex(c = 'é') shouldEqual 233
    BoyerMoore.matchLength(S = "ababa", idx1 = 0, idx2 = 2) shouldEqual 3
    BoyerMoore.fundamentalPreprocess(S = "aaaa").toList shouldEqual List(4, 3, 2, 1)
    BoyerMoore.goodSuffixTable(S = "aba").toList shouldEqual List(-1, -1, 0)
    BoyerMoore.fullShiftTable(S = "aba").toList shouldEqual List(3, 1, 1)

    val table: Array[Array[Int]] = BoyerMoore.badCharacterTable(S = "aba")
    table.length shouldEqual 256
    table('a'.toInt).toList shouldEqual List(-1, 0, 0, 2)
    table('b'.toInt).toList shouldEqual List(-1, -1, 1, 1)
    table('x'.toInt).toList shouldEqual List(-1, -1, -1, -1)
    BoyerMoore.badCharacterTable(S = "").forall(_.toList == List(-1)) shouldBe true
    intercept[IllegalArgumentException] {
      BoyerMoore.badCharacterTable(S = "мир")
    }
  }

  "BoyerMoore.fundamentalPreprocess" should "bound repeated-prefix Z values" in {
    BoyerMoore.fundamentalPreprocess("").toList shouldEqual List.empty
    BoyerMoore.fundamentalPreprocess("a").toList shouldEqual List(1)
    BoyerMoore.fundamentalPreprocess("aaaa").toList shouldEqual List(4, 3, 2, 1)
  }
}
