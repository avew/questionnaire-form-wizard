package id.avew.library.wizard.interfaces

import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.CompoundButton.OnCheckedChangeListener
import com.rey.material.widget.Switch
import com.yarolegovich.discretescrollview.DiscreteScrollView.OnItemChangedListener
import com.yarolegovich.discretescrollview.DiscreteScrollView.ScrollStateChangeListener
import id.avew.library.wizard.utils.CarouselAdapter


interface CommonListener : OnClickListener,
    OnCheckedChangeListener,
    OnItemSelectedListener,
    Switch.OnCheckedChangeListener,
    OnFieldStateChangeListener,
    ScrollStateChangeListener<CarouselAdapter.ViewHolder?>,
    OnItemChangedListener<CarouselAdapter.ViewHolder?>