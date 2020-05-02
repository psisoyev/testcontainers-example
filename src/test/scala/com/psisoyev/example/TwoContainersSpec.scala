package com.psisoyev.example

import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.{MockServerContainer, PulsarContainer, TestContainersForAll}
import com.sksamuel.pulsar4s.{PulsarClient, Topic}
import munit.FunSuite

class TwoContainersSpec extends FunSuite with TestContainersForAll {
  type Containers = PulsarContainer and MockServerContainer

  def startContainers: Containers = {
    val pulsarContainer = PulsarContainer.Def(ContainerVersion.ApachePulsar).start()
    val mockServerContainer = MockServerContainer.Def(ContainerVersion.MockServer).start()

    pulsarContainer and mockServerContainer
  }

  test("Send and receive a message") {
    withContainers { case pulsarContainer and mockServerContainer =>
      val message = "hello"

      val client: PulsarClient = PulsarClient(pulsarContainer.pulsarBrokerUrl())
      val topic = Topic("non-persistent://sample/standalone/test/123")

      val service = new MyPulsarService(client, topic)

      service.send(message)

      assert(service.receive == Right(message))
    }
  }
}
