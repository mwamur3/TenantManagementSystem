
plugins {
    alias(libs.plugins.android.application) apply false

    id("org.jetbrains.kotlin.android") version "2.2.10" apply false

    id("androidx.navigation.safeargs.kotlin") version "2.9.8" apply false

    id("com.google.devtools.ksp") version "2.3.0" apply false
}
