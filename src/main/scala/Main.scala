import BoyerMoore.stringSearch


object Main {
  def main(args: Array[String]): Unit = {
    val TEXT1 =
      "InhisbookseriesTheArtofComputerProgrammingpublishedbyAddisonWesleyDKnuthusesanimaginarycomputertheMIXanditsassociatedmachinecodeandassemblylanguagestoillustratetheconceptsandalgorithmsastheyarepresented"
    val TEXT2 =
      "Nearby farms grew a half acre of alfalfa on the dairy's behalf, with bales of all that alfalfa exchanged for milk."
    val PAT1 = "put"
    val PAT2 = "and"
    val PAT3 = "alfalfa"

    println(s"Found $PAT1 at: ${stringSearch(PAT1, TEXT1)}")
    println(s"Found $PAT2 at: ${stringSearch(PAT2, TEXT1)}")
    println(s"Found $PAT3 at: ${stringSearch(PAT3, TEXT2)}")
  }
}