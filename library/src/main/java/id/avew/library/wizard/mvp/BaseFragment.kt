package id.avew.library.wizard.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseFragment<VS : ViewState?> :
    Fragment() {
    // @Icicle
    var viewState: VS? = null
        protected set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Icepick.restoreInstanceState(this, savedInstanceState);


        if (savedInstanceState == null || viewState == null) {
            viewState = createViewState()
            viewState!!.isSavedInstance = false
        } else {
            viewState?.isSavedInstance = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutRes = layoutRes
        return if (layoutRes == 0) {
            throw IllegalArgumentException(
                "getLayoutRes() returned 0, which is not allowed. "
                        + "If you don't want to use getLayoutRes() but implement your own view for this "
                        + "fragment manually, then you have to override onCreateView();"
            )
        } else {
            inflater.inflate(layoutRes, container, false)
        }
    }

    /**
     * Return the layout resource like R.layout.my_layout
     *
     * @return the layout resource or null, if you don't want to have an UI
     */
    private val layoutRes: Int
        get() = 0

    protected abstract fun createViewState(): VS
}