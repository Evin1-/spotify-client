package foo.bar.musicplayer.ui.filter

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

    val min = arguments?.getInt(KEY_MINIMUM_BUNDLE) ?: 0
    val max = arguments?.getInt(KEY_MAXIMUM_BUNDLE) ?: 0
    val currentMin = arguments?.getInt(KEY_CURRENT_MIN_BUNDLE) ?: 0
    val currentMax = arguments?.getInt(KEY_CURRENT_MAX_BUNDLE) ?: 0

    initViews(min, max, currentMin, currentMax)
  }

  private fun initViews(min: Int, max: Int, currentMin: Int, currentMax: Int) {
    AppLogger.d("TAG__ $min $max $currentMin $currentMax")
    dialog.setTitle(R.string.set_filters)

    f_filter_seek_max.max = max - min
    f_filter_seek_max.progress = currentMax - min
    f_filter_txt_max.text = currentMax.toString()

    f_filter_seek_min.max = max - min
    f_filter_seek_min.progress = currentMin - min
    f_filter_txt_min.text = currentMin.toString()

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
    private const val KEY_CURRENT_MIN_BUNDLE = "KEY_CURRENT_MIN_BUNDLE"
    private const val KEY_CURRENT_MAX_BUNDLE = "KEY_CURRENT_MAX_BUNDLE"

    fun newInstance(min: Int, max: Int, currentMin: Int, currentMax: Int): FilterFragment {
      val fragment = FilterFragment()
      val args = Bundle()
      args.putInt(KEY_MINIMUM_BUNDLE, min)
      args.putInt(KEY_MAXIMUM_BUNDLE, max)
      args.putInt(KEY_CURRENT_MIN_BUNDLE, currentMin)
      args.putInt(KEY_CURRENT_MAX_BUNDLE, currentMax)
      fragment.arguments = args
      return fragment
    }
  }

  interface FilterCallback {
    fun onRangesSelected(min: Int, max: Int)
  }
}