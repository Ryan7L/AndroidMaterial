package io.material.catalog.transition

import android.os.Bundle
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.android.material.transition.MaterialFadeThrough
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TransitionFadeThroughDemoFragment : DemoFragment() {
  private val LAYOUT_RES_MAP = SparseIntArray().apply {
    append(R.id.action_albums, R.layout.cat_transition_fade_through_albums_fragment)
    append(R.id.action_photos, R.layout.cat_transition_fade_through_photos_fragment)
    append(R.id.action_search, R.layout.cat_transition_fade_through_search_fragment)
  }
  private val onItemSelectedListener =
    OnItemSelectedListener { item ->
      replaceFragment(item.itemId, true)
      true
    }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_transition_fade_through_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomnavigation)
    bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener)
    requireActivity().supportFragmentManager.registerFragmentLifecycleCallbacks(
      object : FragmentLifecycleCallbacks(){
        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
          super.onFragmentStarted(fm, f)
          val itemId = try {
              f.tag?.toInt()
          }catch (e: Exception){
            null
          }
          itemId?.let {
            if (bottomNavigationView.selectedItemId != it){
              //解决方法是通过在背面重新创建片段来避免破坏演示，因为 FragmentManager 会处理替换片段
              bottomNavigationView.setOnItemSelectedListener(null)
              bottomNavigationView.selectedItemId = it
              bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener)
            }
          }
        }
      },true
    )
    replaceFragment(R.id.action_albums,false)
  }
  private fun replaceFragment(itemId: Int, addToBackStack: Boolean) {
    val fragment = TransitionSimpleLayoutFragment.newInstance(LAYOUT_RES_MAP[itemId])
    //将过渡设置为片段的进入过渡。当将片段添加到容器中时将使用它，并在从容器中删除片段时重新使用该片段。
    fragment.enterTransition = createTransition()
    requireActivity().supportFragmentManager.beginTransaction().setReorderingAllowed(true)
      .replace(R.id.fragment_container, fragment, itemId.toString()).let {
        if (addToBackStack) {
          it.addToBackStack(itemId.toString())
        }
        it.commit()
      }
  }

  private fun createTransition(): MaterialFadeThrough {
    return MaterialFadeThrough().apply {
      //添加此转换的目标以仅在这些视图上显式运行转换。如果没有目标，则将对片段布局中的每个视图运行 MaterialFadeThrough。
      addTarget(R.id.albums_fragment)
      addTarget(R.id.photos_fragment)
      addTarget(R.id.search_fragment)
    }
  }
}
