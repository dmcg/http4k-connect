package org.http4k.connect.kafka.rest.model

import org.apache.avro.generic.GenericContainer
import org.http4k.connect.model.Base64Blob
import org.http4k.core.ContentType
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.KAFKA_AVRO_v2
import org.http4k.core.KAFKA_BINARY_v2
import org.http4k.core.KAFKA_JSON_V2

class Records private constructor(
    val records: List<Record<*, Any>>,
    val contentType: ContentType = APPLICATION_JSON,
    val key_schema: String? = null,
    val value_schema: String? = null
) {
    companion object {
        fun Json(records: List<Record<*, *>>) = Records(records, ContentType.KAFKA_JSON_V2)
        fun Avro(records: List<Record<GenericContainer, GenericContainer>>) = Records(
            records,
            ContentType.KAFKA_AVRO_v2,
            records.first().key?.schema?.toString(),
            records.first().value.schema.toString()
        )

        fun Binary(records: List<Record<Base64Blob, Base64Blob>>) = Records(records, ContentType.KAFKA_BINARY_v2)
    }
}
