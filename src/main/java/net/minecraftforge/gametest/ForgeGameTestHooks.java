/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gametest;

import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Internal class used to glue mods into the game test framework.
 * Modders should use the supplied annotations and DeferredRegister
 */
@SuppressWarnings("unused")
@ApiStatus.Internal
public class ForgeGameTestHooks {
    private static boolean registeredGametests = false;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Type GAME_TEST_HOLDER = Type.getType(GameTestHolder.class);

    public static boolean isGametestEnabled() {
        return !FMLLoader.isProduction() && (SharedConstants.IS_RUNNING_IN_IDE || isGametestServer() || Boolean.getBoolean("forge.enableGameTest"));
    }

    public static boolean isGametestServer() {
        return !FMLLoader.isProduction() && Boolean.getBoolean("forge.gameTestServer");
    }

/*
    @SuppressWarnings("deprecation")
    public static void registerGametests() {
        if (!registeredGametests && isGametestEnabled() && ModLoader.isLoadingStateValid()) {
            Set<String> enabledNamespaces = getEnabledNamespaces();
            LOGGER.info("Enabled Gametest Namespaces: {}", enabledNamespaces);

            Set<Method> gameTestMethods = new HashSet<>();
            RegisterGameTestsEvent event = new RegisterGameTestsEvent(gameTestMethods);

            ModLoader.get().postEvent(event);

            ModList.get().getAllScanData().stream()
                .map(ModFileScanData::getAnnotations)
                .flatMap(Collection::stream)
                .filter(a -> GAME_TEST_HOLDER.equals(a.annotationType()))
                .forEach(a -> addGameTestMethods(a, gameTestMethods));

            for (Method gameTestMethod : gameTestMethods) {
                GameTestRegistry.register(gameTestMethod, enabledNamespaces);
            }

            registeredGametests = true;
        }
    }

    private static Set<String> getEnabledNamespaces() {
        String enabledNamespacesStr = System.getProperty("forge.enabledGameTestNamespaces");
        if (enabledNamespacesStr == null)
            return Set.of();

        return Arrays.stream(enabledNamespacesStr.split(",")).filter(s -> !s.isBlank()).collect(Collectors.toUnmodifiableSet());
    }

    private static void addGameTestMethods(AnnotationData annotationData, Set<Method> gameTestMethods) {
        try {
            Class<?> clazz = Class.forName(annotationData.clazz().getClassName(), true, ForgeGameTestHooks.class.getClassLoader());

            gameTestMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        } catch (ClassNotFoundException e) {
            // Should not be possible
            throw new RuntimeException(e);
        }
    }

    public static String getTestName(Method method, GameTest meta) {
        var name = method.getName().toLowerCase(Locale.ENGLISH);
        return getPrefixed(method, name);
    }

    private static String getPrefixed(Method method, String name) {
        var prefix = getPrefix(method);
        return prefix == null ? name : prefix + '.' + name;
    }

    @Nullable
    private static String getPrefix(Method method) {
        var cls = method.getDeclaringClass();
        var shouldPrefix = !method.isAnnotationPresent(GameTestDontPrefix.class) &&
                           !cls.isAnnotationPresent(GameTestDontPrefix.class);
        if (!shouldPrefix)
            return null;

        return getPrefix(cls);
    }

    private static String getPrefix(Class<?> cls) {
        var prefix = cls.getAnnotation(GameTestPrefix.class);
        if (prefix != null)
            return prefix.value();

        var holder = cls.getAnnotation(GameTestHolder.class);
        if (holder != null && !holder.value().isEmpty())
            return holder.value();

        var mod = cls.getAnnotation(Mod.class);
        if (mod != null)
            return mod.value();

        return cls.getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    private static ResourceLocation key(String namespace, String path) {
        return path.indexOf(':') == -1 ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static Map<ResourceLocation, GameTestInstance> gatherTests(Class<?> root) {
        String prefix = getPrefix(root);
        var seen = new HashSet<ResourceLocation>();

        Class<?> cls = root;
        while (cls != Object.class) {
            for (var method : cls.getDeclaredMethods()) {
                if (cls != root && Modifier.isStatic(method.getModifiers()))
                    continue;

                var gametest = method.getAnnotation(GameTest.class);
                if (gametest == null)
                    continue;

                var owner = method.getDeclaringClass().getName() + "." + method.getName();

                if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != GameTestHelper.class)
                    throw new IllegalStateException("Invalid @GameTest function " + owner + " incorrect arguments");

                var testName = key(prefix, gametest.name().isEmpty() ? method.getName() : gametest.name());
                if (!seen.add(testName)) {
                    if (!Modifier.isStatic(method.getModifiers()))
                        continue;
                    throw new IllegalStateException("Failed to register test, already seen " + testName + ", for " + owner);
                }

                if (gametest.type() == GameTest.Type.FUNCTION) {
                    var funcName = key(prefix, gametest.function());
                    var func = BuiltInRegistries.TEST_FUNCTION.get(funcName).orElse(null);

                    if (func == null)
                        throw new IllegalStateException("Could not find referenced function `" + funcName + "` for " + owner);

                    var data = getTestData(prefix, owner, gametest.data());


                } else if (gametest.type() == GameTest.Type.BLOCK) {

                } else {
                    throw new IllegalStateException("Could not determine GameTest type for " + root.getName() + " -> " + gametest.type());
                }




            }
            cls = cls.getSuperclass();
        }
    }

    private static TestData<?> getTestData(String prefix, String owner, GameTest.Data data) {
        var envName = key(prefix, data.environment());
        var env = BuiltInRegistries.TEST_ENVIRONMENT
    }
*/
}
