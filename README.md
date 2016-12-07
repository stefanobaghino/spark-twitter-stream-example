Twitter example
===============

A few lines of code to demo how streaming works.
To make it work on your machine, be sure to add a `twitter4j.properties` under `src/main/resources` that includes the following information:

    oauth.consumerKey=***
    oauth.consumerSecret=***
    oauth.accessToken=***
    oauth.accessTokenSecret=***

Visit [apps.twitter.com](https://apps.twitter.com) to get your own API keys.

To submit the job to an existing Spark installation you can package the job with the following command

    sbt assembly

and then submit it

    $SPARK_HOME/bin/spark-submit \
      --class me.baghino.spark.intro.TwitterExample \
      --master $SPARK_MASTER \
      target/scala-2.11/spark-twitter-stream-example-assembly-0.0.1.jar
