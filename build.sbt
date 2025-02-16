name := "joern-sample-extension"
ThisBuild / organization := "io.joern"
ThisBuild / scalaVersion := "3.4.3"

enablePlugins(JavaAppPackaging)

lazy val schema = project.in(file("schema"))
lazy val domainClasses = project.in(file("domain-classes"))
lazy val schemaExtender = project.in(file("schema-extender"))

dependsOn(domainClasses)
libraryDependencies ++= Seq(
  // "io.shiftleft" %% "semanticcpg" % Versions.cpg,
  // "io.shiftleft" %% """|fuzzyc2cpg-tests""".stripMargin % Versions.cpg % Test classifier "tests"
  // "org.scalatest" %% "scalatest" % "3.1.1" % Test,

  // The eclipse.jgit dependency is specific to this example
  // "org.eclipse.jgit" % "org.eclipse.jgit" % "5.7.0.202003110725-r"
)

// We exclude a few jars that the main joern distribution already includes
Universal / mappings := (Universal / mappings).value.filterNot {
  case (_, path) =>
    path.contains("org.scala") ||
    path.contains("net.sf.trove4") ||
    path.contains("com.google.guava") ||
    path.contains("org.apache.logging") ||
    path.contains("com.google.protobuf") ||
    path.contains("com.lihaoyi.u") ||
    path.contains("io.shiftleft") ||
    path.contains("org.typelevel") ||
    path.contains("io.undertow") ||
    path.contains("org.json4s") ||
    path.contains("com.chuusai") ||
    path.contains("io.get-coursier") ||
    path.contains("io.circe") ||
    path.contains("net.java.dev") ||
    path.contains("com.github.javaparser") ||
    path.contains("org.javassist") ||
    // Also include the classes generated from the custom schema
    // We will add these via the schema-extender so that multiple
    // plugins can modify the schema used in a joern installation
    path.contains("io.joern.schema")
}

lazy val createDistribution =
  taskKey[Unit]("Create binary distribution of extension")
createDistribution := {
  val pkgBin = (Universal / packageBin).value
  val dstArchive = file("./plugin.zip")
  IO.copyFile(
    pkgBin,
    dstArchive,
    CopyOptions(
      overwrite = true,
      preserveLastModified = true,
      preserveExecutable = true
    )
  )
  println(s"created distribution - resulting files: $dstArchive")
}

ThisBuild / Compile / scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions"
)

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/public"
)

maintainer := "michael@shiftleft.io"
