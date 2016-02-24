package com.dyonovan.neotech.tools.upgradeitems

import com.dyonovan.neotech.managers.ItemManager
import com.dyonovan.neotech.tools.modifier.{ModifierFortune, ModifierSilkTouch}

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 2/23/2016
  */
object UpgradeItemManager {

    val upgradeSilkTouch = new BaseUpgradeItem(ModifierSilkTouch.SILK, 1)
    val upgradeFortune = new BaseUpgradeItem(ModifierFortune.FORTUNE, 5)

    def preInit(): Unit = {
        ItemManager.registerItem(upgradeSilkTouch, upgradeSilkTouch.getUpgradeName)
        ItemManager.registerItem(upgradeFortune, upgradeFortune.getUpgradeName)
    }
}
