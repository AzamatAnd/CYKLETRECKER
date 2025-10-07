plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.cycletracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cycletracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Оптимизации для Android 14 и новых устройств
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    signingConfigs {
        create("release") {
            // Эти значения должны быть установлены в GitHub Secrets
            // KEYSTORE_BASE64 - base64 encoded keystore file
            // KEYSTORE_PASSWORD - keystore password
            // KEY_ALIAS - key alias
            // KEY_PASSWORD - key password

            storeFile = file("../keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "default_store_password"
            keyAlias = System.getenv("KEY_ALIAS") ?: "default_key_alias"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "default_key_password"

            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }

        create("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Оптимизации для современного приложения
            android {
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        excludes += "/META-INF/DEPENDENCIES"
                        excludes += "/META-INF/NOTICE*"
                        excludes += "/META-INF/LICENSE*"
                        excludes += "/META-INF/*.properties"
                        excludes += "kotlin/**"
                        excludes += "DebugProbesKt.bin"
                    }
                }
            }

            // BuildConfig настройки для релиза
            buildConfigField("String", "BUILD_TYPE", "\"release\"")
            buildConfigField("boolean", "ENABLE_CRASHLYTICS", "true")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "true")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android dependencies - стабильные версии
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Compose BOM - стабильная версия для Kotlin 1.9.x
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")

    // Room database
    implementation("androidx.room:room-runtime:2.7.0-alpha08")
    implementation("androidx.room:room-ktx:2.7.0-alpha08")
    kapt("androidx.room:room-compiler:2.7.0-alpha08")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // Hilt - temporarily removed for build stability
    // implementation("com.google.dagger:hilt-android:2.50")
    // kapt("com.google.dagger:hilt-compiler:2.50")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Biometric
    implementation("androidx.biometric:biometric:1.1.0")

    // ML Kit (for AI features)
    implementation("com.google.mlkit:translate:17.0.2")

    // Health Connect (for wearable integration)
    implementation("androidx.health.connect:connect-client:1.1.0-alpha11")

    // Glance (for widgets)
    implementation("androidx.glance:glance-appwidget:1.0.0")

    // Modern UI components for 2025
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.foundation:foundation-layout")


    // Better async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Enhanced gesture support for 2025 - стабильная версия
    implementation("androidx.compose.foundation:foundation:1.6.8")

    // Swipe to refresh and advanced gestures - стабильная версия
    implementation("androidx.compose.material:material-ripple:1.6.8")

    // Better pointer input handling - стабильная версия
    implementation("androidx.compose.ui:ui:1.6.8")

    // VR/AR Integration for 2025 (ARCore)
    implementation("com.google.ar:core:1.40.0")
    implementation("com.google.ar.sceneform:core:1.17.1")

    // Blockchain health integration
    implementation("org.web3j:core:4.9.8")
    implementation("org.web3j:crypto:4.9.8")

    // Decentralized storage (IPFS) - временно отключено для сборки
    // implementation("com.github.ipfs:java-ipfs-api:v1.2.3")

    // NFT health data tokens - временно отключено для сборки
    // implementation("io.github.nftstorage:nftstorage-java:0.1.0")

    // 3D Graphics for cycle visualization
    implementation("androidx.graphics:graphics-shapes:1.0.0-alpha05")

    // Advanced animations for 3D
    implementation("androidx.compose.animation:animation-graphics:1.6.0-beta01")

    // Lottie animations for ultra modern effects
    implementation("com.airbnb.android:lottie-compose:6.3.0")

    // Advanced graphics for particle effects
    implementation("androidx.graphics:graphics-core:1.0.0-alpha05")

    // Enhanced animations for 2025
    implementation("androidx.compose.animation:animation:1.6.0")

    // Better performance monitoring
    implementation("androidx.metrics:metrics-performance:1.0.0-alpha04")

    // Advanced text rendering
    implementation("androidx.compose.ui:ui-text:1.6.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
