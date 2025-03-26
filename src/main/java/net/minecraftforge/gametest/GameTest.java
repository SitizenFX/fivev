package net.minecraftforge.gametest;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.minecraft.world.level.block.Rotation;

@Retention(RUNTIME)
@Target(METHOD)
public @interface GameTest {
    String name() default "";
    String function() default "";
    Type type() default Type.BLOCK;
    Data data() default @Data;



    int rotationSteps() default 0;


    public enum Type {
        BLOCK,
        FUNCTION;
    }

    /**
     * Annotation definitions for {@link net.minecraft.gametest.framework.TestData}
     */
    public @interface Data {
        String environment() default "";
        String structure() default "forge:empty3x3";
        int maxTicks() default Integer.MAX_VALUE;
        int setupTicks() default 0;
        boolean required() default false;
        Rotation rotation() default Rotation.NONE;
        boolean manualOnly() default false;
        int maxAttempts() default 1;
        int requiredSuccesses() default 1;
        boolean skyAccess() default false;
    }
}
