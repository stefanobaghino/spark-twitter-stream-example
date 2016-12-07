name := """spark-twitter-stream-example"""

version := "0.0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-streaming_2.11" % "1.2.1",
  "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.2.1"
)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "activation", xs @ _*)                   => MergeStrategy.first
  case PathList("com", "esotericsoftware", xs @ _*)               => MergeStrategy.first
  case PathList("org", "apache", "hadoop", xs @ _*)               => MergeStrategy.first
  case PathList("org", "apache", "commons", xs @ _*)              => MergeStrategy.first
  case PathList("org", "slf4j", xs @ _*)                          => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "plugin.properties"  => MergeStrategy.first
  case PathList("META-INF", xs @ _*)                              => MergeStrategy.first
  case x                                                          => (assemblyMergeStrategy in assembly).value.apply(x)
}
