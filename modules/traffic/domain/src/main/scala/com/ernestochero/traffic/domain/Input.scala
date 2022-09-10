package com.ernestochero.traffic.domain

import com.ernestochero.traffic.domain.Type.{Avenue, Street}

case class Input(
  startAvenue: Avenue,
  startStreet: Street,
  endAvenue: Avenue,
  endStreet: Street
)
