package com.psisoyev.example

trait Service {
  def send(msg: String): Unit
  def receive: Either[Throwable, String]
}