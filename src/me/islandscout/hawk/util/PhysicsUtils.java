/*
 * This file is part of Hawk Anticheat.
 *
 * Hawk Anticheat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hawk Anticheat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hawk Anticheat.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.islandscout.hawk.util;

/**
 * High accuracy Minecraft physics utility.
 */
public final class PhysicsUtils {

    //Legit, I don't know how else to describe these.
    //This is the amount of kinetic energy that is preserved per tick.
    public static final double KINETIC_PRESERVATION_AIR = 0.91;
    public static final double KINETIC_PRESERVATION_GROUND = 0.546;
    public static final double KINETIC_PRESERVATION_ICE = 0.891801;

    public static final double JUMP_INITIAL_VELOCITY_VERTICAL = 0.42;

    //Fun fact: The client should never reach these values. When
    //graphing the client's velocity on a velocity vs. time graph,
    //the velocity appears to exponentially decay towards one of
    //these specific values.
    public static final double WALK_SPEED = 50D/227D;
    public static final double SPRINT_MULTIPLIER = 1.3;
    public static final double SNEAK_MULTIPLIER = 0.41561;
    public static final double FLY_SPEED = 5D/9D;
    public static final double FLY_SPRINT_MULTIPLIER = 1.3;

    //If moving slow enough, the client may buffer its moves and then send them when
    //they're significant enough. I'm not sure if this is an attempt at network
    //compression, but what I do know is that movements under this value are
    //unpredictable.
    public static final double MOVEMENT_RELIABILITY_THRESHOLD = 0.1;


    private PhysicsUtils() {
    }

    /**
     * Vertical position vs. time functions.
     */
    public static double airYPosFunc(double initVelocityY, long deltaTime) {
        deltaTime++;
        return -3.92 * deltaTime - 50 * Math.pow(0.98, deltaTime) * (3.92 + initVelocityY) + 50 * (3.92 + initVelocityY);
    }

    public static double waterYPosFunc(double initVelocityY, long deltaTime) {
        deltaTime++;
        return -0.1 * deltaTime - 5 * Math.pow(0.8, deltaTime) * (0.1 + initVelocityY) + 5 * (0.1 + initVelocityY);
    }

    /**
     * Vertical velocity vs. time functions.
     */
    public static double airYVelFunc(double initVelocityY, long deltaTime) {
        return (3.92 + initVelocityY) * Math.pow(0.98, deltaTime) - 3.92;
    }

    public static double waterYVelFunc(double initVelocityY, long deltaTime) {
        return (0.1 + initVelocityY) * Math.pow(0.8, deltaTime) - 0.1;
    }

    public static double horizontalSpeedRecursive(double speed, Surface surface, Medium medium, MovementMode mode, byte jumpStatus, byte landStatus) {
        switch (mode) {
            case FLY:

            case WALK:
            case SNEAK:
            case SPRINT:
            case FLY_SNEAK:
        }
        return 0;
    }

    public enum Surface {
        AIR,
        GROUND,
        ICE
    }

    public enum Medium {
        AIR,
        WATER,
        LAVA,
        WEB
    }

    public enum MovementMode {
        WALK, //Yes, I consider swimming in water walking
        SPRINT,
        SNEAK,
        FLY,
        FLY_SNEAK
    }
}
