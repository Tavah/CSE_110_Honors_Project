package entities;

import main.Game;
import utilz.LoadSave;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity{

    private int animTick, animIndex, animSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;

    //offsets the hitbox to the proper player corner
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f* Game.SCALE;
    private boolean inAir = false;

    private BufferedImage[][] animArray;
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 20 * Game.SCALE, 27 * Game.SCALE);
    }

    public void update() {

        updatePos();
        updateAnimTick();
        setAnimation();

    }

    public void render(Graphics g) {

        g.drawImage(animArray[playerAction][animIndex], (int)(hitbox.x - xDrawOffset), (int)(hitbox.y - yDrawOffset), width, height, null);
        //drawHitbox(g); //FOR DEBUG

    }

    private void updateAnimTick() {

        animTick++;
        if(animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if(animIndex >= GetSpriteAmount(playerAction)) {
                animIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {

        int startAnim = playerAction;

        if(moving){
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if(inAir) {
            if(airSpeed < 0)
                playerAction = JUMPING;
            else
                playerAction = FALLING;
        }

        if(attacking){
            playerAction = ATTACK_1;
        }

        if(startAnim != playerAction) {
            resetAnimTick();
        }
    }

    private void resetAnimTick() {
        animTick = 0;
        animIndex = 0;
    }

    private void updatePos() {

        moving = false;

        if(jump)
            jump();

        if(!left && !right && !inAir) return;

        float xSpeed = 0;

        if(left)
            xSpeed -= playerSpeed;

        if (right)
            xSpeed += playerSpeed;

        if(!inAir) {
            if(!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if(inAir) {
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);

            } else {
                hitbox.y = GetEntityYPosBoundaries(hitbox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else updateXPos(xSpeed);
        moving = true;

    }

    private void jump() {
        if(inAir) return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }


    private void loadAnimations() {

        InputStream is = getClass().getResourceAsStream("/player_sprites.png");

            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

            animArray = new BufferedImage[9][6];

            for(int j = 0; j < animArray.length; j++)
                for (int i = 0; i < animArray[j].length; i++)
                    animArray[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

    }

    //Getters & Setters

    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void setAttacking (boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }


}