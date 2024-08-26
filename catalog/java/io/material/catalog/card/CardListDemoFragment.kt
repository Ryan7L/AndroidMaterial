package io.material.catalog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.google.android.material.card.MaterialCardView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import java.util.Locale

class CardListDemoFragment : DemoFragment() {
  private val CARD_COUNT = 30
  override val demoTitleResId: Int
    get() = R.string.cat_card_list
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_card_list_fragment, container, false)
    initRv(view)
    return view
  }

  private fun initRv(view: View) {
    val rv = view.findViewById<RecyclerView>(R.id.cat_card_list_recycler_view)
    val adapter = CardAdapter(generateCardNumbers())
    val itemTouchHelper = ItemTouchHelper(CardItemTouchHelperCallback(adapter))
    adapter.itemTouchHelper = itemTouchHelper
    rv.layoutManager = LinearLayoutManager(activity)
    rv.adapter = adapter
    rv.setAccessibilityDelegateCompat(object : RecyclerViewAccessibilityDelegate(rv) {
      override fun getItemDelegate(): AccessibilityDelegateCompat {
        return object : ItemDelegate(this) {
          override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfoCompat
          ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            val position = rv.getChildLayoutPosition(host)
            if (position != 0) {
              info.addAction(
                AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                  R.id.move_card_up_action,
                  host.resources.getString(R.string.cat_card_action_move_up)
                )
              )
            }
            if (position != CARD_COUNT - 1) {
              info.addAction(
                AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                  R.id.move_card_down_action,
                  host.resources.getString(R.string.cat_card_action_move_down)
                )
              )
            }
          }

          override fun performAccessibilityAction(host: View, action: Int, args: Bundle?): Boolean {
            val position = rv.getChildLayoutPosition(host)
            return when (action) {
              R.id.move_card_down_action -> {
                swapCards(position, position + 1, adapter)
                true
              }

              R.id.move_card_up_action -> {
                swapCards(position, position - 1, adapter)
                true
              }

              else -> super.performAccessibilityAction(host, action, args)
            }
          }
        }
      }
    })
    itemTouchHelper.attachToRecyclerView(rv)
  }

  private fun generateCardNumbers(): IntArray {
    val cardNums = IntArray(30)
    for (i in 0 until 30) {
      cardNums[i] = i + 1
    }
    return cardNums
  }

  companion object {


    class CardAdapter(val cardNums: IntArray) : RecyclerView.Adapter<CardViewHolder>() {

      var itemTouchHelper: ItemTouchHelper? = null

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
          LayoutInflater.from(parent.context).inflate(R.layout.cat_card_list_item, parent, false)
        )
      }


      override fun getItemCount(): Int {
        return cardNums.size
      }


      override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardNums[position], itemTouchHelper!!)
      }

    }

    class CardViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
      val titleView = itemView.findViewById<TextView>(R.id.cat_card_list_item_title)
      val dragHandleView = itemView.findViewById<View>(R.id.cat_card_list_item_drag_handle)

      fun bind(cardNum: Int, itemTouchHelper: ItemTouchHelper) {
        titleView.text = String.format(Locale.getDefault(), "Card #%02d", cardNum)
        dragHandleView.setOnTouchListener { v, event ->
          return@setOnTouchListener if (event.action == MotionEvent.ACTION_DOWN) {
            itemTouchHelper.startDrag(this)
            true
          } else false
        }
      }
    }

    class CardItemTouchHelperCallback(val adapter: CardAdapter) : ItemTouchHelper.Callback() {


      private val DRAG_FLAGS = ItemTouchHelper.UP or ItemTouchHelper.DOWN

      private val SWIPE_FLAGS = 0

      private var dragCardView: MaterialCardView? = null

      /**
       * Should return a composite flag which defines the enabled move directions in each state
       * (idle, swiping, dragging).
       *
       *
       * Instead of composing this flag manually, you can use [.makeMovementFlags]
       * or [.makeFlag].
       *
       *
       * This flag is composed of 3 sets of 8 bits, where first 8 bits are for IDLE state, next
       * 8 bits are for SWIPE state and third 8 bits are for DRAG state.
       * Each 8 bit sections can be constructed by simply OR'ing direction flags defined in
       * [ItemTouchHelper].
       *
       *
       * For example, if you want it to allow swiping LEFT and RIGHT but only allow starting to
       * swipe by swiping RIGHT, you can return:
       * <pre>
       * makeFlag(ACTION_STATE_IDLE, RIGHT) | makeFlag(ACTION_STATE_SWIPE, LEFT | RIGHT);
      </pre> *
       * This means, allow right movement while IDLE and allow right and left movement while
       * swiping.
       *
       * @param recyclerView The RecyclerView to which ItemTouchHelper is attached.
       * @param viewHolder   The ViewHolder for which the movement information is necessary.
       * @return flags specifying which movements are allowed on this ViewHolder.
       * @see .makeMovementFlags
       * @see .makeFlag
       */
      override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
      ): Int {
        return makeMovementFlags(DRAG_FLAGS, SWIPE_FLAGS)
      }

      /**
       * Called when ItemTouchHelper wants to move the dragged item from its old position to
       * the new position.
       *
       *
       * If this method returns true, ItemTouchHelper assumes `viewHolder` has been moved
       * to the adapter position of `target` ViewHolder
       * ([ ViewHolder#getAdapterPositionInRecyclerView()][ViewHolder.getAbsoluteAdapterPosition]).
       *
       *
       * If you don't support drag & drop, this method will never be called.
       *
       * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
       * @param viewHolder   The ViewHolder which is being dragged by the user.
       * @param target       The ViewHolder over which the currently active item is being
       * dragged.
       * @return True if the `viewHolder` has been moved to the adapter position of
       * `target`.
       * @see .onMoved
       */
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        swapCards(fromPosition, toPosition, adapter)
        return true
      }

      /**
       * Called when a ViewHolder is swiped by the user.
       *
       *
       * If you are returning relative directions ([.START] , [.END]) from the
       * [.getMovementFlags] method, this method
       * will also use relative directions. Otherwise, it will use absolute directions.
       *
       *
       * If you don't support swiping, this method will never be called.
       *
       *
       * ItemTouchHelper will keep a reference to the View until it is detached from
       * RecyclerView.
       * As soon as it is detached, ItemTouchHelper will call
       * [.clearView].
       *
       * @param viewHolder The ViewHolder which has been swiped by the user.
       * @param direction  The direction to which the ViewHolder is swiped. It is one of
       * [.UP], [.DOWN],
       * [.LEFT] or [.RIGHT]. If your
       * [.getMovementFlags]
       * method
       * returned relative flags instead of [.LEFT] / [.RIGHT];
       * `direction` will be relative as well. ([.START] or [                   ][.END]).
       */
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
      }

      override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
          dragCardView = viewHolder.itemView as MaterialCardView
          dragCardView?.isDragged = true
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && dragCardView != null) {
          dragCardView?.isDragged = false
          dragCardView = null
        }
      }
    }

    private fun swapCards(fromPosition: Int, toPosition: Int, adapter: CardAdapter) {
      val fromNumber = adapter.cardNums[fromPosition]
      adapter.cardNums[fromPosition] = adapter.cardNums[toPosition]
      adapter.cardNums[toPosition] = fromNumber
      adapter.notifyItemMoved(fromPosition, toPosition)
    }
  }


}
