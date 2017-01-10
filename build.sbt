
lazy val root = (project in file("Main"))
  .settings(
      name := "Main",
      scalaVersion := "2.12.1",
      javacOptions ++= Seq("-bootclasspath", "D:/Program Files/Java/jdk1.8.0_102/jre/lib/rt.jar", "-XDignore.symbol.file","-encoding", "windows-1250", "-source", "8", "-target", "8"),
      javaSource in Compile := baseDirectory.value / "src",
      unmanagedBase in Compile := baseDirectory.value)
