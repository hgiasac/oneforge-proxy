name := "forex"
version := "1.0.0"

scalaVersion := "2.12.6"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-language:experimental.macros",
  "-language:implicitConversions",
)

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)
  

val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "com.github.pureconfig"          %% "pureconfig"           % "0.9.2",
  "com.softwaremill.quicklens"     %% "quicklens"            % "1.4.11",
  "com.typesafe.akka"              %% "akka-actor"           % "2.4.19",
  "com.typesafe.akka"              %% "akka-http"            % "10.0.10",
  "de.heikoseeberger"              %% "akka-http-circe"      % "1.18.1",
  "io.circe"                       %% "circe-core"           % circeVersion,
  "io.circe"                       %% "circe-parser"         % circeVersion,
  "io.circe"                       %% "circe-generic"        % circeVersion,
  "io.circe"                       %% "circe-generic-extras" % circeVersion,
  "io.circe"                       %% "circe-java8"          % circeVersion,
  "io.circe"                       %% "circe-jawn"           % circeVersion,
  "org.atnos"                      %% "eff"                  % "5.3.0",
  "org.atnos"                      %% "eff-monix"            % "5.3.0",
  "org.typelevel"                  %% "cats-core"            % "0.9.0",
  "org.zalando"                    %% "grafter"              % "2.6.1",
  "ch.qos.logback"                 % "logback-classic"       % "1.2.3" % Runtime,
  "com.github.cb372"               %% "scalacache-caffeine"  % "0.24.3",
  "com.github.cb372"               %% "scalacache-monix"     % "0.24.3",
  "com.typesafe.scala-logging"     %% "scala-logging"        % "3.9.0",
  "org.scalacheck"                 %% "scalacheck"           % "1.14.0" % "test",
  compilerPlugin("org.spire-math"  %% "kind-projector"       % "0.9.4"),
  compilerPlugin("org.scalamacros" %% "paradise"             % "2.1.0" cross CrossVersion.full)
)
