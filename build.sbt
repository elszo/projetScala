name := "monprojet"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
  "com.h2database" % "h2" % "1.4.190",
  "mysql" % "mysql-connector-java" % "5.1.+",
  "com.typesafe" % "config" % "1.4.1",
  "org.scala-lang.modules" % "scala-swing_2.13" %"2.1.1",
  "org.typelevel" %% "cats-core" % "2.2.0"
)
