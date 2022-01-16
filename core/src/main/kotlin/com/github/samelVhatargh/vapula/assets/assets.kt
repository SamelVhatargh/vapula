package com.github.samelVhatargh.vapula.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.getAsset

enum class SoundAsset(
    val fileName: String,
    val descriptor: AssetDescriptor<Sound> = AssetDescriptor("sounds/$fileName", Sound::class.java)
) {
    EMPTY(""),
    STEP_1("steps/footstepDirt01.wav"),
    STEP_2("steps/footstepDirt02.wav"),
    STEP_3("steps/footstepDirt03.wav"),
    STEP_4("steps/footstepDirt04.wav"),
    STEP_5("steps/footstepDirt05.wav"),
    STEP_6("steps/footstepDirt06.wav"),
    STEP_7("steps/footstepDirt07.wav"),
    STEP_8("steps/footstepDirt08.wav"),
    STEP_9("steps/footstepDirt09.wav"),
    STEP_10("steps/footstepDirt10.wav"),
    MELEE_ATTACK_01("attacks/melee01.wav"),
    MELEE_ATTACK_02("attacks/melee02.wav"),
    MELEE_ATTACK_03("attacks/melee03.wav"),
    CRUSH_HIT_01("hits/crushHit01.wav"),
    CRUSH_HIT_02("hits/crushHit02.wav"),
    CRUSH_HIT_03("hits/crushHit03.wav"),
    CRUSH_HIT_04("hits/crushHit04.wav"),
    CRUSH_HIT_05("hits/crushHit05.wav"),
    CRUSH_HIT_06("hits/crushHit06.wav"),
    CRUSH_HIT_07("hits/crushHit07.wav"),
    ARROW_HIT_01("hits/arrowHit01.wav"),
    ARROW_HIT_02("hits/arrowHit02.wav"),
    MAGIC_HIT_01("hits/magicHit01.wav"),
    SWORD_HIT_01("hits/swordHit01.wav"),
    SWORD_HIT_02("hits/swordHit02.wav"),
}

enum class TextureAtlasAsset(
    val fileName: String,
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor(fileName, TextureAtlas::class.java)
) {
    SPRITES("graphics/sprites.atlas"),
    UI("graphics/ui.atlas"),
}

enum class FontAsset(
    val fileName: String,
    val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor(
        fileName,
        BitmapFont::class.java,
        BitmapFontLoader.BitmapFontParameter().apply {
            atlasName = TextureAtlasAsset.UI.descriptor.fileName
        })
) {
    SIZE_24("fonts/DejaVu24.fnt"),
}

operator fun AssetManager.get(asset: SoundAsset) = getAsset<Sound>(asset.fileName)
operator fun AssetManager.get(asset: TextureAtlasAsset) = getAsset<TextureAtlas>(asset.fileName)
operator fun AssetManager.get(asset: FontAsset) = getAsset<BitmapFont>(asset.fileName)