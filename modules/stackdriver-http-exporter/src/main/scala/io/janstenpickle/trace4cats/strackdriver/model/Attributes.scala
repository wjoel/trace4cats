package io.janstenpickle.trace4cats.strackdriver.model

import io.circe.Encoder
import io.circe.generic.semiauto._
import io.janstenpickle.trace4cats.model.AttributeValue

case class Attributes(attributeMap: Map[String, SDAttributeValue], droppedAttributesCount: Int)

object Attributes {
  def fromCompleted(attributes: Map[String, AttributeValue]): Attributes = {
    val attrs = attributes.take(32).map {
      case (k, AttributeValue.StringValue(value)) => k -> SDAttributeValue.StringValue(value)
      case (k, AttributeValue.DoubleValue(value)) => k -> SDAttributeValue.IntValue(value.toLong)
      case (k, AttributeValue.BooleanValue(value)) => k -> SDAttributeValue.BoolValue(value)
      case (k, AttributeValue.LongValue(value)) => k -> SDAttributeValue.IntValue(value)
    }

    val attrSize = attrs.size
    val origSize = attributes.size

    val dropped = if (attrSize < origSize) origSize - attrSize else 0

    Attributes(attrs, dropped)
  }

  implicit val encoder: Encoder[Attributes] = deriveEncoder
}
