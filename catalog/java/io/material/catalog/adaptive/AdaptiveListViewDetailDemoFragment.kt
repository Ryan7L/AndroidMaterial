package io.material.catalog.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import io.material.catalog.R

class AdaptiveListViewDetailDemoFragment : Fragment() {
  companion object {
    const val TAG = "AdaptiveListViewDetailDemoFragment"
    const val EMAIL_ID_KEY = "email_id_key"
    fun newInstance(emailId: Long): AdaptiveListViewDetailDemoFragment {
      val args = Bundle()
      args.putLong(EMAIL_ID_KEY, emailId)
      val fragment = AdaptiveListViewDetailDemoFragment()
      fragment.arguments = args
      return fragment
    }
  }

  private val emailId: Long
    get() = arguments?.getLong(EMAIL_ID_KEY, 0L) ?: 0L

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_adaptive_list_view_detail_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val emailTitle = view.findViewById<TextView>(R.id.email_title)
    emailTitle.append(" " + (emailId + 1))
    val container = view.findViewById<View>(R.id.list_view_detail_container)
    ViewCompat.setTransitionName(container, emailTitle.toString())
  }

  private fun updateEmailSelected(selected: Boolean) {
    val email = EmailData.getEmailById(emailId)
    email.isSelected = selected
  }

  override fun onDestroy() {
    super.onDestroy()
    updateEmailSelected(false)
  }

  override fun onStart() {
    super.onStart()
    updateEmailSelected(true)
  }
}
