package com.omarahmed42.components;

import com.omarahmed42.util.AssetPool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(PlayerController playerController) {
        if (!playerController.isSmall()) {
            AssetPool.getSound("assets/sounds/break_block.ogg").play();
            gameObject.destroy();
        }
    }
}
