package com.psisoyev.example

import com.sksamuel.pulsar4s._
import org.apache.pulsar.client.api.Schema

final class MyPulsarService(client: PulsarClient, topic: Topic) extends Service {
  implicit val schema: Schema[String] = Schema.STRING

  private val producer = client.producer[String](ProducerConfig(topic))
  private val consumer = client.consumer[String](ConsumerConfig(Subscription("test/321"), Seq(topic)))

  override def send(msg: String): Unit = producer.send(msg)
  override def receive: Either[Throwable, String] = consumer.receive.map(_.value).toEither
}