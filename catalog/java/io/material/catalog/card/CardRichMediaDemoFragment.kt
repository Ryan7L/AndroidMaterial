package io.material.catalog.card

import io.material.catalog.R

class CardRichMediaDemoFragment: CardMainDemoFragment() {

  override val demoTitleResId: Int
    get() = R.string.cat_card_rich_media_demo

  override val cardContent: Int
    get() = R.layout.cat_card_rich_media_demo_fragment
}
