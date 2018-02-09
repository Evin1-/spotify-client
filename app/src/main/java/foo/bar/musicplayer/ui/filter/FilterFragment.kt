package foo.bar.musicplayer.ui.filter

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import foo.bar.musicplayer.R
import foo.bar.musicplayer.util.AppLogger
import kotlinx.android.synthetic.main.fragment_filter.*

/**
 * Created by evin on 2/9/18.
 */

class FilterFragment : DialogFragment() {

  private var filterCallback: FilterCallback? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (parentFragment is FilterCallback) {
      filterCallback = parentFragment as FilterCallback
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_filter, container, false)
  }

  override fun onStart() {
    super.onStart()

    dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val max = arguments?.getInt(KEY_MAXIMUM_BUNDLE) ?: 0
    val min = arguments?.getInt(KEY_MINIMUM_BUNDLE) ?: 0

    initViews(max, min)
  }

  private fun initViews(max: Int, min: Int) {
    dialog.setTitle(R.string.set_filters)

    f_filter_seek_max.max = max - min
    f_filter_seek_max.progress = max - min
    f_filter_txt_max.text = max.toString()

    f_filter_seek_min.max = max - min
    f_filter_txt_min.text = min.toString()

    f_filter_seek_max.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onStopTrackingTouch(p0: SeekBar?) = Unit
      override fun onStartTrackingTouch(p0: SeekBar?) = Unit
      override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        f_filter_txt_max.text = (p1 + min).toString()
        filterResults()
      }
    })

    f_filter_seek_min.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onStopTrackingTouch(p0: SeekBar?) = Unit
      override fun onStartTrackingTouch(p0: SeekBar?) = Unit
      override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        f_filter_txt_min.text = (p1 + min).toString()
        filterResults()
      }
    })

    f_filter_accept.setOnClickListener { dismiss() }
  }

  private fun filterResults() {
    val min = f_filter_txt_min.text.toString().toInt()
    val max = f_filter_txt_max.text.toString().toInt()

    if (max < min) {
      return
    }

    filterCallback?.onRangesSelected(min, max)
  }

  companion object {
    private const val KEY_MINIMUM_BUNDLE = "KEY_MINIMUM_BUNDLE"
    private const val KEY_MAXIMUM_BUNDLE = "KEY_MAXIMUM_BUNDLE"

    fun newInstance(min: Int, max: Int): FilterFragment {
      val fragment = FilterFragment()
      val args = Bundle()
      args.putInt(KEY_MINIMUM_BUNDLE, min)
      args.putInt(KEY_MAXIMUM_BUNDLE, max)
      fragment.arguments = args
      return fragment
    }
  }

  interface FilterCallback {
    fun onRangesSelected(min: Int, max: Int)
  }
}