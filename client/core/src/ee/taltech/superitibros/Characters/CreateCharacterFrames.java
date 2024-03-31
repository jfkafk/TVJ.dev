package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Objects;

import static ee.taltech.superitibros.Characters.SkinNames.SKELETON;

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

    public void makeFrames() {
        determineWhatPathToUse();
        createFramesIdle();
        createFramesWalking();
        createFramesFalling();
        createFramesJumping();
    }

    public void determineWhatPathToUse() {
//        if (Objects.equals(characterSkinName, "Skeleton")) {
//            walkAnimationPath = "Characters/Skeleton sprites/WALK 64 frames.png";
//            idleAnimationPath = "Characters/Skeleton sprites/IDLE 64 frames.png";
//            jumpAnimationPath = "Characters/Skeleton sprites/JUMP 64 frames.png";
//            fallAnimationPath = "Characters/Skeleton sprites/FALL 64 frames.png";
//        }
//        else {
//            walkAnimationPath = "Characters/TestCharacter.png";
//            idleAnimationPath = "Characters/TestCharacter.png";
//            jumpAnimationPath = "Characters/TestCharacter.png";
//            fallAnimationPath = "Characters/TestCharacter.png";
//        }
        walkAnimationPath = "Characters/Skeleton sprites/WALK 64 frames.png";
        idleAnimationPath = "Characters/Skeleton sprites/IDLE 64 frames.png";
        jumpAnimationPath = "Characters/Skeleton sprites/JUMP 64 frames.png";
        fallAnimationPath = "Characters/Skeleton sprites/FALL 64 frames.png";
    }

    private void createFramesIdle() {
        // Sprite
        // Idle
        Texture idlesheet = new Texture(idleAnimationPath);
        TextureRegion[][] tmpIdle = TextureRegion.split(idlesheet, idlesheet.getWidth() / 8,
                idlesheet.getHeight() / 8);
        TextureRegion[] idleFramesRight = new TextureRegion[64];
        TextureRegion[] idleFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpIdle[i][j], 90, 0, croppedWidth, croppedHeight);
                idleFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpIdle[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                idleFramesLeft[index] = croppedRegionLeft;
            }
        }
        // making IDLE ANIMATION
        idleAnimationRight = new Animation<TextureRegion>(0.025f, idleFramesRight);
        idleAnimationLeft = new Animation<TextureRegion>(0.025f, idleFramesLeft);
    }

    private void createFramesWalking() {
        // Sprite

        // Walking
        Texture walksheet = new Texture(walkAnimationPath);
        TextureRegion[][] tmpwalk = TextureRegion.split(walksheet, walksheet.getWidth() / 8, walksheet.getHeight() / 8);

        TextureRegion[] walkFramesRight = new TextureRegion[64];
        TextureRegion[] walkFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpwalk[i][j], 90, 0, croppedWidth, croppedHeight);
                walkFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpwalk[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                walkFramesLeft[index] = croppedRegionLeft;
            }
        }
        walkAnimationRight = new Animation<TextureRegion>(0.015f, walkFramesRight);
        walkAnimationLeft = new Animation<TextureRegion>(0.015f, walkFramesLeft);
    }

    private void createFramesFalling() {
        // Sprite
        // Idle
        Texture fallSheet = new Texture(fallAnimationPath);
        TextureRegion[][] tmpFall = TextureRegion.split(fallSheet, fallSheet.getWidth() / 8,
                fallSheet.getHeight() / 8);
        TextureRegion[] fallFramesRight = new TextureRegion[64];
        TextureRegion[] fallFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpFall[i][j], 90, 0, croppedWidth, croppedHeight);
                fallFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpFall[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                fallFramesLeft[index] = croppedRegionLeft;
            }
        }
        // making IDLE ANIMATION
        fallAnimationRight = new Animation<TextureRegion>(0.025f, fallFramesRight);
        fallAnimationLeft = new Animation<TextureRegion>(0.025f, fallFramesLeft);
    }

    private void createFramesJumping() {
        // Sprite
        // Idle
        Texture jumpSheet = new Texture(jumpAnimationPath);
        TextureRegion[][] tmpJump = TextureRegion.split(jumpSheet, jumpSheet.getWidth() / 8,
                jumpSheet.getHeight() / 8);
        TextureRegion[] jumpFramesRight = new TextureRegion[64];
        TextureRegion[] jumpFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpJump[i][j], 90, 0, croppedWidth, croppedHeight);
                jumpFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpJump[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                jumpFramesLeft[index] = croppedRegionLeft;
            }
        }
        // making IDLE ANIMATION
        jumpAnimationRight = new Animation<TextureRegion>(0.025f, jumpFramesRight);
        jumpAnimationLeft = new Animation<TextureRegion>(0.025f, jumpFramesLeft);
    }

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
}
