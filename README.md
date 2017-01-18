Twitter example
===============

A few lines of code to demo how streaming works with Spark, in particular using the extensions provided by [Apache Bahir](https://bahir.apache.org/) to read a live stream of tweets.
To make it work on your machine, be sure to add a `twitter4j.properties` under `src/main/resources` that includes the following information:

    oauth.consumerKey=***
    oauth.consumerSecret=***
    oauth.accessToken=***
    oauth.accessTokenSecret=***

Visit [apps.twitter.com](https://apps.twitter.com) to get your own API keys.

To submit the job to an existing Spark installation you can package the job with the following command

    sbt package

and then submit it

    $SPARK_HOME/bin/spark-submit \
      --master $SPARK_MASTER \
      --class me.baghino.spark.streaming.twitter.example.TwitterSentimentScore \
      target/scala-2.11/spark-twitter-stream-example-assembly-0.0.1.jar
      
The Spark classpath should include `org.apache.bahir/spark-streaming-twitter_2.11:2.0.1`, `org.twitter4j/twitter4j-core:4.0.4` and `org.twitter4j/twitter4j-stream:4.0.4`.

You can either put those JARs in your Spark installation `jars` directory or provide them while submitting with the `--jar` command line option.