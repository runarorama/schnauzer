name := "schnauzer"

scalaVersion in Global := "2.10.4"

scalacOptions ++= Seq(
  "-Ywarn-value-discard",
  "-Xlint",
  "-language:existentials",
  "-language:higher-kinds"
)

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "org.scalaz.stream" %% "scalaz-stream" % "0.8"
)

