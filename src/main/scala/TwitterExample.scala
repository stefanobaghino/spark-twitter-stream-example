package sparkIntro

import org.apache.spark.{SparkConf, SparkContext}

object TwitterExample extends App {

  // First, let's configure Spark
  // We will deploy locally using a thread-per-core (that's what the '*' stands for)
  val conf = new SparkConf().
    setAppName("spark-twitter-stream-example").
    setMaster("local[*]")

  // Let's create the Spark Context using the configuration we just created
  val sc = new SparkContext(conf)

}
