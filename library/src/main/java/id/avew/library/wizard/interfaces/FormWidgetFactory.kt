package id.avew.library.wizard.interfaces

import android.content.Context
import android.view.View
import id.avew.library.wizard.expression.JsonExpressionResolver
import id.avew.library.wizard.i18n.JsonFormBundle
import org.json.JSONObject

/**
 * Created by vijay on 24-05-2015.
 */
interface FormWidgetFactory {
    @Throws(Exception::class)
    fun getViewsFromJson(
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?,
        bundle: JsonFormBundle?,
        resolver: JsonExpressionResolver?,
        visualizationMode: Int
    ): List<View?>?
}