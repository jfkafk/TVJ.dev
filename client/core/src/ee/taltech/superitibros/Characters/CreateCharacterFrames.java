package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.GameInfo.ClientWorld;
import ee.taltech.superitibros.GameInfo.GameClient;

import java.util.Objects;
import java.util.Random;

public class CreateCharacterFrames {

    String walkAnimationPath;
    String idleAnimationPath;
    String jumpAnimationPath;
    String fallAnimationPath;
    Animation<TextureRegion> walkAnimationRight;
    Animation<TextureRegion> walkAnimationLeft;
    Animation<TextureRegion> idleAnimationRight;
    Animation<TextureRegion> idleAnimationLeft;
    Animation<TextureRegion> jumpAnimationRight;
    Animation<TextureRegion> jumpAnimationLeft;
    Animation<TextureRegion> fallAnimationRight;
    Animation<TextureRegion> fallAnimationLeft;
    private Integer playerSize;
    private String characterName = "Goblin";
    private GameClient gameClient;

    public CreateCharacterFrames(ClientWorld clientWorld) {
        characterName =  clientWorld.getClientConnection().getGameClient().getCharacterName();
    }

    public void makeFrames(GameCharacter character) {
        determineWhatPathToUse(character);
        createFramesIdle();
        createFramesWalking();
        createFramesFalling();
        createFramesJumping();
    }

    /**
     * determine what path to take; right now a skeleton path is default later it is changed
     */
    public void determineWhatPathToUse(GameCharacter character) {
        if (character instanceof Enemy) {
            System.out.println("Creating frames for -> " + character.nameOfSkin);
            walkAnimationPath = "Characters/Skeleton sprites/WALK 64 frames.png";
            idleAnimationPath = "Characters/Skeleton sprites/IDLE 64 frames.png";
            jumpAnimationPath = "Characters/Skeleton sprites/JUMP 64 frames.png";
            fallAnimationPath = "Characters/Skeleton sprites/FALL 64 frames.png";
            playerSize = 54;
        }
        else {
            if (Objects.equals(characterName, "Rambo")) {
                System.out.println("Creating frames for -> " + characterName);
                walkAnimationPath = "Characters/Enemy sprites/WALK.png";
                idleAnimationPath = "Characters/Enemy sprites/IDLE.png";
                jumpAnimationPath = "Characters/Enemy sprites/JUMP.png";
                fallAnimationPath = "Characters/Enemy sprites/FALL.png";
                playerSize = 50;
            }
            if (Objects.equals(characterName, "Wizard")) {
                System.out.println("Creating frames for -> " + characterName);
                walkAnimationPath = "Characters/Wizard sprites/Wizard WALK.png";
                idleAnimationPath = "Characters/Wizard sprites/Wizard IDLE.png";
                jumpAnimationPath = "Characters/Wizard sprites/Wizard JUMP.png";
                fallAnimationPath = "Characters/Wizard sprites/Wizard FALL.png";
                playerSize = 50;
            }
            if (Objects.equals(characterName, "Goblin")) {
                System.out.println("Creating frames for -> " + characterName);
                walkAnimationPath = "Characters/Goblin Sprites/Goblin WALK.png";
                idleAnimationPath = "Characters/Goblin Sprites/Goblin IDLE.png";
                jumpAnimationPath = "Characters/Goblin Sprites/Goblin JUMP.png";
                fallAnimationPath = "Characters/Goblin Sprites/Goblin FALL.png";
                playerSize = 50;
            }
        }
    }

    private void createFrames(String animationPath) {
        Texture sheet = new Texture(animationPath);
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 8, sheet.getHeight() / 8);
        TextureRegion[] framesRight = new TextureRegion[64];
        TextureRegion[] framesLeft = new TextureRegion[64];

        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int index = i * 8 + j;
                TextureRegion croppedRegionRight = new TextureRegion(tmp[i][j], 90, 0, croppedWidth, croppedHeight);
                framesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmp[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                framesLeft[index] = croppedRegionLeft;
            }
        }

        // Assign animations even if the parameters are null
        if (animationPath.contains("WALK")) {
            walkAnimationRight = new Animation<TextureRegion>(0.015f, framesRight);
            walkAnimationLeft = new Animation<TextureRegion>(0.015f, framesLeft);
        } if (animationPath.contains("IDLE")) {
            idleAnimationRight = new Animation<TextureRegion>(0.025f, framesRight);
            idleAnimationLeft = new Animation<TextureRegion>(0.025f, framesLeft);
        } if (animationPath.contains("JUMP")) {
            jumpAnimationRight = new Animation<TextureRegion>(0.025f, framesRight);
            jumpAnimationLeft = new Animation<TextureRegion>(0.025f, framesLeft);
        } if (animationPath.contains("FALL")) {
            fallAnimationRight = new Animation<TextureRegion>(0.025f, framesRight);
            fallAnimationLeft = new Animation<TextureRegion>(0.025f, framesLeft);
        }
    }


    private void createFramesFalling() {
        createFrames(fallAnimationPath);
    }

    private void createFramesJumping() {
        createFrames(jumpAnimationPath);
    }

    private void createFramesIdle() {
        createFrames(idleAnimationPath);
    }

    private void createFramesWalking() {
        createFrames(walkAnimationPath);
    }



    /**
     *
     * @return all the getters for animations
     */
    public Animation<TextureRegion> getWalkAnimationRight() {
        return walkAnimationRight;
    }

    public Animation<TextureRegion> getWalkAnimationLeft() {
        return walkAnimationLeft;
    }

    public Animation<TextureRegion> getIdleAnimationRight() {
        return idleAnimationRight;
    }

    public Animation<TextureRegion> getIdleAnimationLeft() {
        return idleAnimationLeft;
    }

    public Animation<TextureRegion> getJumpAnimationRight() {
        return jumpAnimationRight;
    }

    public Animation<TextureRegion> getJumpAnimationLeft() {
        return jumpAnimationLeft;
    }

    public Animation<TextureRegion> getFallAnimationRight() {
        return fallAnimationRight;
    }

    public Animation<TextureRegion> getFallAnimationLeft() {
        return fallAnimationLeft;
    }

    public Integer getPlayerSize() {
        return playerSize;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
