package com.psisoyev.example

import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.{PulsarContainer, TestContainersForAll}
import com.sksamuel.pulsar4s.{PulsarClient, Topic}
import munit.FunSuite

class DifferentVersionsSpec extends FunSuite with TestContainersForAll {
  type Containers = PulsarContainer and PulsarContainer

  override def startContainers(): and[PulsarContainer, PulsarContainer] = {
    val pulsarContainer = PulsarContainer.Def("2.2.0").start()
    val pulsarContainerNew = PulsarContainer.Def("2.5.0").start()

    pulsarContainer and pulsarContainerNew
  }

  val message = "hello"

  test("Send and receive a message") {
    withContainers { case pulsarContainer and pulsarContainerNew =>
      List(pulsarContainer, pulsarContainerNew)
        .map(container => scenario(container.pulsarBrokerUrl()))
        .foreach(result => assert(result == Right(message)))
    }
  }

  def scenario(brokerUrl: String): Either[Throwable, String] = {
    val client: PulsarClient = PulsarClient(brokerUrl)
    val topic = Topic("non-persistent://sample/standalone/test/123")

    val service = new MyPulsarService(client, topic)

    service.send(message)

    service.receive
  }
}
