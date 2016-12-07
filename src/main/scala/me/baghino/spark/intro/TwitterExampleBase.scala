package me.baghino.spark.intro

import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}
import twitter4j.Status

import scala.io.Source

trait TwitterExampleBase {

  // Some type aliases to give a little bit of context
  type Tweet = Status
  type TweetText = String
  type Sentence = Seq[String]

  // First, let's configure Spark
  // We will deploy locally using a thread for each core (that's what the '*' stands for)
  val sparkConfiguration = new SparkConf().
    setAppName("spark-twitter-stream-example").
    setMaster("local[*]")

  // Let's create the Spark Context using the configuration we just created
  val sparkContext = new SparkContext(sparkConfiguration)

  // Now let's wrap the context in a streaming one, passing along the window size
  val streamingContext = new StreamingContext(sparkContext, Seconds(5))

  // Creating a stream from Twitter (see the README to learn how to configure it)
  val tweets: DStream[Status] =
    TwitterUtils.createStream(streamingContext, None)

  // FIXME There's a smarter way to do this: see the "broadcast-var" branch
  val uselessWords = load("/stop-words.dat")
  val positiveWords = load("/pos-words.dat")
  val negativeWords = load("/neg-words.dat")
  
  def wordsOf(tweet: TweetText): Sentence =
    tweet.split(" ")
  
  def toLowercase(sentence: Sentence): Sentence =
    sentence.map(_.toLowerCase)
  
  def keepActualWords(sentence: Sentence): Sentence =
    sentence.filter(_.matches("[a-z]+"))
  
  def keepMeaningfulWords(sentence: Sentence): Sentence =
    sentence.filter(!uselessWords.contains(_))

  def extractWords(sentence: Sentence): Sentence =
    sentence.map(_.toLowerCase).filter(_.matches("[a-z]+"))

  def computeWordScore(word: String): Int =
    if (positiveWords.contains(word))       1
    else if (negativeWords.contains(word)) -1
    else                                    0

  def computeSentenceScore(words: Sentence): Int =
    words.map(computeWordScore).sum

}
