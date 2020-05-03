package com.psisoyev.example

import java.io.File
import java.net.URL

import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, TestContainerForAll}
import munit.FunSuite
import org.testcontainers.containers.wait.strategy.Wait

import scala.io.Source

class DockerComposeSpec extends FunSuite with TestContainerForAll {
  val dockerComposeFile: File = new File("src/test/resources/docker-compose.yml")
  val nginxPort = 80
  val nginxContainerName = "nginx_1"
  val exposedService: ExposedService = ExposedService(nginxContainerName, nginxPort, Wait.forHttp("/"))

  override val containerDef: DockerComposeContainer.Def = DockerComposeContainer.Def(dockerComposeFile, Seq(exposedService))

  test("succesfully start Nginx with docker-compose") {
    withContainers { containers =>
      val expectedText = "If you see this page, the nginx web server is successfully installed"

      val nginxContainer =
        containers
          .getContainerByServiceName(nginxContainerName)
          .getOrElse(fail(s"Couldn't locate Nginx container"))

      val rootUrl = new URL(s"http://${nginxContainer.getContainerIpAddress}:$nginxPort/")
      val rootPage: String = Source.fromInputStream(rootUrl.openConnection().getInputStream).mkString

      assert(rootPage.contains(expectedText))
    }
  }
}