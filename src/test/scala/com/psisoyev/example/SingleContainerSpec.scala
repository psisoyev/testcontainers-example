package com.psisoyev.example

import com.dimafeng.testcontainers.{PulsarContainer, TestContainerForAll}
import com.sksamuel.pulsar4s.{PulsarClient, Topic}
import munit.FunSuite

class SingleContainerSpec extends FunSuite with TestContainerForAll {
  override val containerDef: PulsarContainer.Def = PulsarContainer.Def(ContainerVersion.ApachePulsar)

  test("Send and receive a message") {
    withContainers { pulsarContainer =>
      val message = "hello"

      val client: PulsarClient = PulsarClient(pulsarContainer.pulsarBrokerUrl())
      val topic = Topic("non-persistent://sample/standalone/test/123")

      val service = new MyPulsarService(client, topic)

      service.send(message)

      assert(service.receive == Right(message))
    }
  }
}
