package sparkIntro

import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

object TwitterExample extends App {

  // First, let's configure Spark
  // We will deploy locally using a thread-per-core (that's what the '*' stands for)
  val conf = new SparkConf().
    setAppName("spark-twitter-stream-example").
    setMaster("local[*]")

  // Let's create the Spark Context using the configuration we just created
  val sc = new SparkContext(conf)

  // Now let's wrap the context in a streaming one, passing along the window size
  val ssc = new StreamingContext(sc, Seconds(5))

  // Creating a stream from Twitter, easy as it comes
  val twitterStream = TwitterUtils.createStream(ssc, None)

  // Let's load the words of interest in our "sentiment analysis"
  // FIXME There's a smarter way to do this ;)
  val stopWords = Source.fromFile("src/main/resources/stop-words.dat").getLines().toList
  val positiveWords = Source.fromFile("src/main/resources/pos-words.dat").getLines().toList
  val negativeWords = Source.fromFile("src/main/resources/neg-words.dat").getLines().toList

  // Get the text of a tweet and produce a list of "actual words"
  def clean(tweetText: String): List[String] =
    tweetText.split(" ").
      map(_.toLowerCase).
      filter(_.matches("[a-z]+")).toList

  // Let's remove stop words (`the`, `a`, `it`, etc.)
  def pass(words: List[String]): List[String] =
    words.filter(!stopWords.contains(_))

  // Rating a single word, if it appears on any of the interesting lists we loaded
  def rate(word: String): Int =
    if (positiveWords.contains(word))       1
    else if (negativeWords.contains(word)) -1
    else                                    0

  // Rating a list of words, summing up individual ratings
  def rate(words: List[String]): Int =
    words.foldRight(0) {
      (word, score) =>
        score + rate(word)
    }

  // Here's the actual processing, using the functions we defined above
  twitterStream.
    map { tweet => (tweet.getId(), tweet.getText()) }.
    map { case (id, text) => (id, clean(text)) }.
    map { case (id, words) => (id, pass(words)) }.
    map { case (id, words) => (id, rate(words)) }.
    print()

  // Start the streaming
  ssc.start()

  // Let's await the stream to end - forever
  ssc.awaitTermination()

}
