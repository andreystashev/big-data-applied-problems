
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class BoyerMooreSpec extends AnyFlatSpec with Matchers {

  it should " поиск вхождения 'put' в тексте" in {
    val text = "inputoutput"
    val pattern = "put"
    val result = BoyerMoore.stringSearch(pattern, text)
    result shouldEqual List(2, 8)
  }

  it should "поиск вхождения 'and' в тексте" in {
    val text = "randomtextandmoretext"
    val pattern = "and"
    val result = BoyerMoore.stringSearch(pattern, text)
    result shouldEqual List(1, 10)
  }

  it should "поиск вхождения 'alfalfa' в тексте" in {
    val text = "falfalfaisgoodfora"
    val pattern = "alfalfa"
    val result = BoyerMoore.stringSearch(pattern, text)
    result should contain allElementsOf List(1)
  }

  it should "возврат пустого списка, если образец не найден в тексте" in {
    val text = "nocorrespondence"
    val pattern = "xyz"
    val result = BoyerMoore.stringSearch(pattern, text)
    result shouldEqual List.empty
  }

  it should "возврат пустого списка для пустого образца" in {
    val text = "anytext"
    val pattern = ""
    val result = BoyerMoore.stringSearch(pattern, text)
    result shouldEqual List.empty
  }

  it should "возврат пустого списка при пустом тексте" in {
    val text = ""
    val pattern = "any"
    val result = BoyerMoore.stringSearch(pattern, text)
    result shouldEqual List.empty
  }

  it should "возврат пустого списка, если текст короче образца" in {
    val text = "abc"
    val pattern = "abcd"
    val result = BoyerMoore.stringSearch(pattern, text)
    result shouldEqual List.empty
  }
}