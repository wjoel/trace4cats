package io.janstenpickle.trace4cats.opentelemetry.otlp

import java.time.Instant

import cats.effect.IO
import io.janstenpickle.trace4cats.`export`.SemanticTags
import io.janstenpickle.trace4cats.model.Batch
import io.janstenpickle.trace4cats.test.jaeger.BaseJaegerSpec

class OpenTelemetryOtlpGrpcSpanExporterSpec extends BaseJaegerSpec {
  it should "Send a batch of spans to jaeger" in forAll { batch: Batch =>
    val updatedBatch =
      Batch(
        batch.process.copy(attributes = Map.empty),
        batch.spans.map(_.copy(start = Instant.now(), end = Instant.now()))
      )

    testExporter(
      OpenTelemetryOtlpGrpcSpanExporter[IO](blocker, "localhost", 55680),
      updatedBatch,
      batchToJaegerResponse(updatedBatch, SemanticTags.kindTags, SemanticTags.statusTags("", requireMessage = false))
    )
  }
}
