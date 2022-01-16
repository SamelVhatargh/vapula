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