package com.psisoyev.example

import java.net.URL

import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import scala.io.Source

class MyNginxContainer(tag: String, port: Int) extends GenericContainer(
  dockerImage = s"nginx:$tag",
  exposedPorts = Seq(port),
  waitStrategy = Some(Wait.forHttp("/"))
) {
  def rootUrl: URL = new URL(s"http://$containerIpAddress:${mappedPort(port)}/")
  def rootPage: String = Source.fromInputStream(rootUrl.openConnection().getInputStream).mkString
}

object MyNginxContainer {
  case class Def(
    tag: String = "latest", port: Int = 80
  ) extends GenericContainer.Def[MyNginxContainer](new MyNginxContainer(tag, port))
}