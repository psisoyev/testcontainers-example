package com.psisoyev.example

import com.dimafeng.testcontainers.TestContainerForAll
import munit.FunSuite

class MyNginxContainerSpec extends FunSuite with TestContainerForAll {
  override val containerDef: MyNginxContainer.Def = MyNginxContainer.Def(ContainerVersion.Nginx)

  test("Access to Nginx root") {
    withContainers { nginxContainer =>
      val expectedText = "If you see this page, the nginx web server is successfully installed"

      assert(nginxContainer.rootPage.contains(expectedText))
    }
  }
}
