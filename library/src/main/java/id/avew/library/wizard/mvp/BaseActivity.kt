package id.avew.library.wizard.mvp

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<VS : ViewState?> :
    AppCompatActivity() {
    // @Icicle
    var viewState: VS? = null
        protected set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Icepick.restoreInstanceState(this, savedInstanceState);


        if (savedInstanceState == null) {
            viewState = createViewState()
            viewState!!.isSavedInstance = false
        } else {
            viewState!!.isSavedInstance = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Icepick.saveInstanceState(this, outState);

    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
    }

    override fun setContentView(
        view: View,
        params: LayoutParams
    ) {
        super.setContentView(view, params)
    }

    override fun addContentView(
        view: View,
        params: LayoutParams
    ) {
        super.addContentView(view, params)
    }

    protected abstract fun createViewState(): VS
}