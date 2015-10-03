package me.baghino.spark.intro

import twitter4j.Status

import scala.io.Source

trait TwitterExampleBase {

  // Some type aliases to give a little bit of context
  type Tweet = Status
  type TweetText = String
  type Sentence = Seq[String]

  // FIXME There's a smarter way to do this: see the "broadcast-var" branch
  private val uselessWords = Source.fromFile("src/main/resources/stop-words.dat").getLines().toList
  private val positiveWords = Source.fromFile("src/main/resources/pos-words.dat").getLines().toList
  private val negativeWords = Source.fromFile("src/main/resources/neg-words.dat").getLines().toList
  
  protected def wordsOf(tweet: TweetText): Sentence =
    tweet.split(" ")
  
  protected def toLowercase(sentence: Sentence): Sentence =
    sentence.map(_.toLowerCase)
  
  protected def keepActualWords(sentence: Sentence): Sentence =
    sentence.filter(_.matches("[a-z]+"))
  
  protected def keepMeaningfulWords(sentence: Sentence): Sentence =
    sentence.filter(!uselessWords.contains(_))

  protected def extractWords(sentence: Sentence): Sentence =
    sentence.map(_.toLowerCase).filter(_.matches("[a-z]+"))

  protected def computeWordScore(word: String): Int =
    if (positiveWords.contains(word))       1
    else if (negativeWords.contains(word)) -1
    else                                    0

  protected def computeSentenceScore(words: Sentence): Int =
    words.map(computeWordScore).sum

}
