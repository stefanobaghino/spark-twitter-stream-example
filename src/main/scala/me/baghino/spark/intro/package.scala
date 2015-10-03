package me.baghino.spark

import scala.io.AnsiColor

package object intro {

  private def format(n: Int): String = f"$n%2d"

  private def wrapScore(s: String): String = s"[ $s ] "

  private def makeReadable(n: Int): String =
    if (n > 0)      s"${AnsiColor.GREEN + format(n) + AnsiColor.RESET}"
    else if (n < 0) s"${AnsiColor.RED   + format(n) + AnsiColor.RESET}"
    else            s"${format(n)}"

  private def makeReadable(s: String): String =
    s.takeWhile(_ != '\n').take(80) + "..."

  def makeReadable(sn: (String, Int)): String =
    sn match {
      case (tweetText, score) => s"${wrapScore(makeReadable(score))}${makeReadable(tweetText)}"
    }

}
