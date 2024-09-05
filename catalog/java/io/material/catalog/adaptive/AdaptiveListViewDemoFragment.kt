package io.material.catalog.adaptive

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialContainerTransform
import io.material.catalog.R

class AdaptiveListViewDemoFragment : Fragment() {
  @IdRes
   var detailViewContainerId = 0
  private var emailRv: RecyclerView? = null
  private var currentSelectedEmailId = -1L

  private val adapterListener = EmailAdapterListener { view, emailId ->
    val fragment = AdaptiveListViewDetailDemoFragment.newInstance(emailId)
    if (currentSelectedEmailId != -1L) {
      //当片段并排时突出显示选定的电子邮件
      setEmailSelected(currentSelectedEmailId, false)
      currentSelectedEmailId = emailId
      setEmailSelected(emailId, true)
    }
    //打开电子邮件项目时创建共享元素过渡。
    val enterTransform = MaterialContainerTransform(requireContext(), true)
    fragment.sharedElementEnterTransition = enterTransform

    parentFragmentManager.beginTransaction()
      .addSharedElement(view, ViewCompat.getTransitionName(view)!!)
      .replace(detailViewContainerId, fragment, AdaptiveListViewDetailDemoFragment.TAG).apply {
        if (detailViewContainerId != R.id.list_view_detail_fragment_container) {
          //如果片段并排可见，则不要添加回堆栈。
          addToBackStack(AdaptiveListViewDetailDemoFragment.TAG)
        }
      }
      .commit()

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_adaptive_list_view_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    emailRv = view.findViewById<RecyclerView?>(R.id.email_list)?.apply {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      adapter = EmailAdapter(adapterListener)
    }
    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      currentSelectedEmailId = 0L
    }
  }

  private fun setEmailSelected(emailId: Long, selected: Boolean) {
    emailRv?.let {
      val holder = it.findViewHolderForItemId(emailId) as? EmailViewHolder
      (holder?.container as? MaterialCardView)?.isChecked = selected
      EmailData.getEmailById(emailId).isSelected = selected
    }
  }
}

class EmailAdapter(val listener: EmailAdapterListener) : RecyclerView.Adapter<EmailViewHolder>() {
  private fun updateEmailSelected(holder: EmailViewHolder) {
    if (holder.container.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      val email = EmailData.getEmailById(holder.itemId)
      (holder.container as MaterialCardView).isChecked = email.isSelected
    }
  }

  override fun getItemId(position: Int): Long {
    return EmailData.emailData[position].id
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
    return EmailViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.cat_adaptive_list_view_fragment_item, parent, false)
    )
  }

  override fun onViewAttachedToWindow(holder: EmailViewHolder) {
    super.onViewAttachedToWindow(holder)
    updateEmailSelected(holder)
  }

  override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
    val resources = holder.senderTitle.resources
    val emailId = getItemId(holder.bindingAdapterPosition)
    holder.container.setOnClickListener {
      listener.onEmailClicked(it, emailId)
    }
    holder.senderTitle.text = resources.getString(R.string.cat_list_view_sender_title)
    holder.emailTitle.text = resources.getString(R.string.cat_list_view_email_title)
    holder.emailTitle.append(" " + (emailId + 1))
    holder.emailPreview.text = resources.getString(R.string.cat_list_view_email_text)
    ViewCompat.setTransitionName(holder.container, holder.emailTitle.toString())
    updateEmailSelected(holder)
  }

  override fun getItemCount(): Int {
    return 10
  }
}

class EmailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  val container: View = view.findViewById(R.id.list_view_item_container)
  val senderTitle: TextView = view.findViewById(R.id.sender_title)
  val emailTitle: TextView = view.findViewById(R.id.email_title)
  val emailPreview: TextView = view.findViewById(R.id.email_preview)
}

fun interface EmailAdapterListener {
  fun onEmailClicked(view: View, emailId: Long)
}

data class Email(val id: Long, var isSelected: Boolean)
class EmailData() {
  companion object {
    val emailData = listOf(
      Email(0L, true),
      Email(1L, false),
      Email(2L, false),
      Email(3L, false),
      Email(4L, false),
      Email(5L, false),
      Email(6L, false),
      Email(7L, false),
      Email(8L, false),
      Email(9L, false)
    )
    @JvmStatic
    fun getEmailById(emailId: Long): Email = emailData.first { it.id == emailId }
  }
}
