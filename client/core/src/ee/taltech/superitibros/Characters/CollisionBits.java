package ee.taltech.superitibros.Characters;

public class CollisionBits {
    public static final short CATEGORY_PLAYER = 0x0001; // Unique category bit for player characters
    public static final short CATEGORY_ENEMY = 0x0002;  // Unique category bit for enemies
    public static final short CATEGORY_OBSTACLE = 0x0004; // Unique category bit for obstacles

    public static final short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_OBSTACLE; // Player collides with enemies and obstacles
    public static final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_OBSTACLE;  // Enemy collides with players and obstacles
    public static final short MASK_OBSTACLE = CATEGORY_PLAYER | CATEGORY_ENEMY; // Obstacle collides with players and enemies
}

