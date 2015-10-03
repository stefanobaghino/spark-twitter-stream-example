package me.baghino.spark.intro

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.Status

object TwitterExample extends App with TwitterExampleBase {

  // Initialization and processing functions are defined in the TwitterExampleBase class

  // Let's extract the words of each tweet
  // We'll carry the tweet along in order to print it in the end
  val textAndSentences: DStream[(TweetText, Sentence)] =
    tweets.
      map(_.getText).
      map(tweetText => (tweetText, wordsOf(tweetText)))

  // Apply several transformations that allow us to keep just meaningful sentences
  val textAndMeaningfulSentences: DStream[(TweetText, Sentence)] =
    textAndSentences.
      mapValues(toLowercase).
      mapValues(keepActualWords).
      mapValues(keepMeaningfulWords).
      filter { case (_, sentence) => sentence.length > 0 }

  // Compute the score of each sentence and keep only the non-neutral ones
  val textAndNonNeutralScore: DStream[(TweetText, Int)] =
    textAndMeaningfulSentences.
      mapValues(computeSentenceScore).
      filter { case (_, score) => score != 0 }

  // Transform the (tweet, score) tuple into a readable string and print it
  textAndNonNeutralScore.map(makeReadable).print

  // Now that the streaming is defined, start it
  streamingContext.start()

  // Let's await the stream to end - forever
  streamingContext.awaitTermination()

}
