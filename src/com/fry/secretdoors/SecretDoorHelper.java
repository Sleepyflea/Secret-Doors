package com.fry.secretdoors;

import org.bukkit.Material;
import static org.bukkit.Material.FURNACE;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;

public class SecretDoorHelper {


    public static boolean isTopHalf(Block door) {
        if (door.getType() != Material.OAK_DOOR)
            throw new IllegalArgumentException("Incorrect Block type, expected WOODEN_DOOR but got " + door.getType());
        return (door.getData() & 0x8) == 0x8;
    }

    public static Block getKeyFromBlock(Block block) {
        Block door = null;

        if (block.getType() == Material.OAK_DOOR) {
            // return lower half only
            if (isTopHalf(block))
                door = block.getRelative(BlockFace.DOWN);
            else {
                door = block;
            }
        }
        return door;
    }
    public static boolean isValidBlock(Block block) {

        if (block != null) {

            if (isAttachableItem(block.getType())) {
                return false;
            }
            Material bt = block.getType();
switch(bt) {
  case FURNACE: // This accounts for both the lit and unlit variants
  case REDSTONE_TORCH: // This also accounts for both the lit and unlit variants
  case DISPENSER:
  case CHEST:
  case CRAFTING_TABLE:
  case REDSTONE:
  case IRON_DOOR:
      return false;
  default:
      // The line below, checks if the material is tagged as a door, pressure plate or carpet
      if(Tag.DOORS.isTagged(bt) || Tag.PRESSURE_PLATES.isTagged(bt) || Tag.CARPETS.isTagged(bt)) {
        return false;
      }
      return true;
}

    /**
* checks if the item can be attached to a block
*/
    public static boolean isAttachableItem(Material item) {

        if (item != null) {
            switch (item) {
                case TORCH:
                case OAK_SIGN:
                case OAK_WALL_SIGN:
                case LEVER:
                case STONE_BUTTON: // NOTE: for whatever reason, Material enum doesn't include wooden buttons
                case LADDER:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public static Attachable getAttachableFromBlock(Block block) {

        return isAttachableItem(block.getType()) ? (Attachable) block.getState().getData() : null;
    }

    /**
* Checks if door block can be a secret door
*/
    public static boolean canBeSecretDoor(Block door) {
        
        Material bt = block.getType();
        if (Tag.DOORS.isTagged(bt))
            return false;
        BlockFace face = getDoorFace(door);
        door = getKeyFromBlock(door);

        Block bottom = door.getRelative(face);
        Block top = bottom.getRelative(BlockFace.UP);
        // This is done to avoid creating a door with AIR blocks after a door is opened.
        // It's handled this way instead of adding Material.AIR to the black list so that doors can still be created
        // when only one block is used.
        if (bottom.getType() != Material.AIR || top.getType() != Material.AIR)
            if (isValidBlock(bottom) && isValidBlock(top)) // AIR is considered `valid` in this case
                return true;
        return false;

    }

    /**
* Returns the direction the door is facing while closed
*/
    public static BlockFace getDoorFace(Block door) {

        door = getKeyFromBlock(door);
        byte data = door.getData();
        if ((data & 0x3) == 0x3) return BlockFace.SOUTH;
        if ((data & 0x1) == 0x1) return BlockFace.NORTH;
        if ((data & 0x2) == 0x2) return BlockFace.EAST;
        return BlockFace.WEST;
    }

    /**
* Determines if the Block was clicked or the Door was clicked.
* TODO: review if this is even necessary - if so, consider re-naming. `Direction` doesn't accurately describe it's use.
*/
    public static enum Direction {
        BLOCK_FIRST, DOOR_FIRST
    }
}