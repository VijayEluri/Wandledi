import sbt._
import Keys._

object Wandledi extends Build {
  import Dependencies._

  val description = SettingKey[String]("description")

  val wandlediSettings = Defaults.defaultSettings ++ Seq (
    version             := "0.6.0-SNAPSHOT",
    organization        := "org.wandledi",
    scalaVersion        := "2.8.1",
    crossScalaVersions  := Seq("2.8.0", "2.8.1", "2.9.0", "2.9.1"),
    publishTo           <<= (version) { version: String =>
      if (version.trim.endsWith("SNAPSHOT")) Some(
        "Sonatype Nexus Snapshots" at
        "https://oss.sonatype.org/content/repositories/snapshots"
      ) else Some(
        "Sonatype Nexus Release Staging" at
        "https://oss.sonatype.org/service/local/staging/deploy/maven2"
      )
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    pomExtra <<= (pomExtra, name, description) { (extra, name, desc) => extra ++ Seq(
      <name>{name}</name>,
      <description>{desc}</description>,
      <url>http://wandledi.org</url>,
      <licenses>
        <license>
          <name>MIT</name>
          <url>https://raw.github.com/machisuji/Wandledi/HEAD/LICENSE</url>
          <distribution>repo</distribution>
        </license>
      </licenses>,
      <scm>
        <url>https://github.com/machisuji/Wandledi</url>
        <connection>scm:git:git://github.com/machisuji/Wandledi.git</connection>
      </scm>,
      <developers>
        <developer>
          <id>machisuji</id>
          <name>Markus Kahl</name>
          <url>https://github.com/machisuji</url>
        </developer>
      </developers>
    )}
  ) ++ Unidoc.settings

  val javaSettings = wandlediSettings ++ Seq (
    javacOptions      := Seq("-target", "5", "-Xlint:unchecked"),
    autoScalaLibrary  := false,
    crossPaths        := false
  )

  val scalaSettings = wandlediSettings ++ Seq (
    scalacOptions       ++= Seq("-unchecked", "-deprecation")
  )

  lazy val wandledi = Project (
    "wandledi-project",
    file("."),
    settings = wandlediSettings ++ Seq (
      description := "An HTML transformation library",
      publishArtifact in Compile := false
    )
  ).aggregate(wandlediCore, wandlediScala, wandletCore, wandletScala)

  lazy val wandlediCore = Project (
    "wandledi",
    file("core"),
    settings = javaSettings ++ Seq (
      description := "Wandledi Java Core",
      libraryDependencies ++= Seq(htmlparser, testng, scalatest)
    )
  )

  lazy val wandlediScala = Project (
    "wandledi-scala",
    file("scala-lib"),
    settings = scalaSettings ++ Seq (
      description := "Scala API for Wandledi",
      libraryDependencies ++= Seq(scalatest)
    )
  ).dependsOn(wandlediCore % "compile;provided->provided;test->test")

  lazy val wandletCore = Project (
    "wandlet",
    file("wandlet"),
    settings = javaSettings ++ Seq (
      description := "Wandledi utilities for Servlet API based Java applications",
      libraryDependencies ++= Seq(servletApi)
    )
  ).dependsOn(wandlediCore % "compile;provided->provided;test->test")

  lazy val wandletScala = Project (
    "wandlet-scala",
    file("wandlet-scala"),
    settings = scalaSettings ++ Seq (
      description := "Wandledi utilities for Servlet API based Scala applications"
    )
  ).dependsOn(
    wandlediScala % "compile;provided->provided;test->test",
    wandletCore % "compile;provided->provided;test->test"
  )
}

object Dependencies {
  val htmlparser = "nu.validator.htmlparser" % "htmlparser" % "1.2.1" % "compile"
  val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided"

  val testng = "org.testng" % "testng" % "5.12.1" % "test" // for Java only
  val scalatest = "org.scalatest" %% "scalatest" % "1.5.+" % "test"
}