name := """ScalaDDDForumVer1"""
version := "0.1"

scalaVersion := "2.13.3"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
lazy val root = (project in file(".")).enablePlugins(PlayScala)
scalacOptions ++= Seq(
  //  "-Xfatal-warnings",
  //  "-deprecation",
  //  "-feature",
  //  "-unchecked",
  //  "-language:existentials",
  //  "-language:higherKinds",
  //  "-language:implicitConversions",
  //  "-Ywarn-dead-code",
  "-Ymacro-annotations",
)

//addCompilerPlugin(
//  "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
//)

//addCompilerPlugin(
//  "org.augustjune" %% "context-applied" % "0.1.2"
//)

val circeVersion = "0.12.3"
val doobieVersion = "0.9.0"

libraryDependencies ++= Seq(

  "org.typelevel" %% "cats-core" % "2.1.0",
  "org.typelevel" %% "cats-effect" % "2.1.0",
  "dev.profunktor" %% "console4cats" % "0.8.1",
  //  "org.manatki" %% "derevo-cats" % "0.10.5",
  //
  //  "org.manatki" %% "derevo-cats-tagless" % "0.10.5",
  //  "co.fs2" %% "fs2-core" % "2.2.2",

  //  "com.olegpy" %% "meow-mtl-core" % "0.4.0",
  //  "com.olegpy" %% "meow-mtl-effects" % "0.4.0",


  "eu.timepit" %% "refined" % "0.9.13",
  //"eu.timepit" %% "refined-cats" % Versions.refined

  //"io.chrisdavenport" %% "log4cats-slf4j" % Versions.log4cats
  "io.estatico" %% "newtype" % "0.4.3",


  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-refined" % circeVersion,
  "org.postgresql" % "postgresql" % "42.2.16",
  "org.tpolecat"  %% "doobie-core"      % doobieVersion,
  "org.tpolecat"  %% "doobie-postgres"  % doobieVersion,
  "org.tpolecat"  %% "doobie-refined"   % doobieVersion,

  "org.wvlet.airframe" %% "airframe" % "20.12.1",

  compilerPlugin(
    "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
  ),


  "com.github.cb372" %% "cats-retry" % "2.1.0",
  "io.chrisdavenport" %% "log4cats-core"    % "1.1.1",  // Only if you want to Support Any Backend
  "io.chrisdavenport" %% "log4cats-slf4j"   % "1.1.1",  // Direct Slf4j Support - Recommended

  "org.http4s"    %% "http4s-core"         % "0.21.6",
  "org.http4s"    %% "http4s-dsl"          % "0.21.6",
  "org.http4s"    %% "http4s-blaze-server" % "0.21.6",
  "org.http4s"    %% "http4s-circe"        % "0.21.6",

  "org.tpolecat"    %% "doobie-hikari"        % doobieVersion,
  // Runtime
  //val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

"com.opentable.components" % "otj-pg-embedded" % "0.13.3" % Test,
//  "ru.yandex.qatools.embed" % "postgresql-embedded" % "2.10",
"org.postgresql" % "postgresql" % "42.2.5"  % Test,


)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"

libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.6" % "provided"

libraryDependencies += "com.softwaremill.macwire" %% "macrosakka" % "2.3.6" % "provided"

libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.6"

libraryDependencies += "com.softwaremill.macwire" %% "proxy" % "2.3.6"

libraryDependencies += guice
libraryDependencies += "com.dripower" %% "play-circe" % "2712.0"
