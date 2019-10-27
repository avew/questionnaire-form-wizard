package id.avew.library.wizard.interfaces

import id.avew.library.wizard.expression.ExternalContentResolver
import id.avew.library.wizard.expression.JsonExpressionResolver
import id.avew.library.wizard.i18n.JsonFormBundle
import org.json.JSONException
import org.json.JSONObject
import java.util.*

interface JsonApi {
    fun getStep(stepName: String?): JSONObject?
    @Throws(JSONException::class)
    fun writeValue(
        stepName: String?,
        key: String?,
        value: String?
    )

    @Throws(JSONException::class)
    fun writeValue(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    fun currentJsonState(): String?
    val count: String?
    val visualizationMode: Int
    fun getBundle(locale: Locale?): JsonFormBundle?
    val expressionResolver: JsonExpressionResolver?
    val externalContentResolver: ExternalContentResolver?
}