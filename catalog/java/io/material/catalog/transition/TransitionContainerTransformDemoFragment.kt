//package io.material.catalog.transition
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.Menu
//import android.view.MenuInflater
//import android.view.MenuItem
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.view.ViewCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
//import io.material.catalog.R
//import io.material.catalog.feature.DemoFragment
//
//private const val END_FRAGMENT_TAG = "END_FRAGMENT_TAG"
//
//class TransitionContainerTransformDemoFragment : DemoFragment() {
//  private var configurationHelper: ContainerTransformConfigurationHelper? = null
//
//  override fun onAttach(context: Context) {
//    super.onAttach(context)
//    configurationHelper = ContainerTransformConfigurationHelper()
//  }
//  override fun onCreateDemoView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//  ): View? {
//    return inflater.inflate(R.layout.cat_transition_container_transform_fragment,container,false)
//  }
//
//  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    childFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentLifecycleCallbacks(){
//      override fun onFragmentViewCreated(
//        fm: FragmentManager,
//        f: Fragment,
//        v: View,
//        savedInstanceState: Bundle?
//      ) {
//        addTransitionableTarget(v,R.id.start_fab)
//        addTransitionableTarget(v,R.id.single_line_list_item)
//        addTransitionableTarget(v,R.id.vertical_card_item)
//        addTransitionableTarget(v,R.id.horizontal_card_item)
//        addTransitionableTarget(v,R.id.grid_card_item)
//        addTransitionableTarget(v,R.id.grid_tall_card_item)
//      }
//    },true)
//  }
//
//  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//    inflater.inflate(R.menu.configure_menu,menu)
//    super.onCreateOptionsMenu(menu, inflater)
//  }
//
//  override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    if(item.itemId == R.id.configure){
//      configurationHelper?.showConfigurationChooser(requireContext()){
//        val endFragment = childFragmentManager.findFragmentByTag(END_FRAGMENT_TAG)
//        endFragment?.let {
//          configureTransitions(it)
//        }
//      }
//    }
//    return super.onOptionsItemSelected(item)
//  }
//  private fun addTransitionableTarget(view: View,id:Int){
//    view.findViewById<View>(id)?.let {
//      ViewCompat.setTransitionName(it,id.toString())
//      it.setOnClickListener {
//        showEndFragment()
//      }
//    }
//  }
//  private fun showEndFragment(){
//
//  }
//}
