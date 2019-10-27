package id.avew.library.wizard.interfaces

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/10/17.
 */

interface OnFieldStateChangeListener {
    fun onInitialValueSet(
        parentKey: String?,
        childKey: String?,
        value: String?
    )

    fun onValueChange(
        parentKey: String?,
        childKey: String?,
        value: String?
    )

    fun onVisibilityChange(
        key: String?,
        o: String?,
        b: Boolean
    )
}