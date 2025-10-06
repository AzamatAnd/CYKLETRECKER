plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
	id("com.google.devtools.ksp")
}

android {
		namespace = "com.example.cycletracker"
		compileSdk = 35

	defaultConfig {
		applicationId = "com.example.cycletracker"
		minSdk = 26
		targetSdk = 35
		versionCode = 7
		versionName = "2.0.0"

		vectorDrawables {
			useSupportLibrary = true
		}
	}

	signingConfigs {
		create("release") {
			val storeFileProp = project.findProperty("RELEASE_STORE_FILE") as String?
			val storePasswordProp = project.findProperty("RELEASE_STORE_PASSWORD") as String?
			val keyAliasProp = project.findProperty("RELEASE_KEY_ALIAS") as String?
			val keyPasswordProp = project.findProperty("RELEASE_KEY_PASSWORD") as String?
			if (!storeFileProp.isNullOrBlank() && !storePasswordProp.isNullOrBlank() && !keyAliasProp.isNullOrBlank() && !keyPasswordProp.isNullOrBlank()) {
				storeFile = file(storeFileProp)
				storePassword = storePasswordProp
				keyAlias = keyAliasProp
				keyPassword = keyPasswordProp
			}
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
			// Используем release-подпись только если заданы все поля, иначе fallback на debug
			val releaseConfig = signingConfigs.getByName("release")
			signingConfig = if (releaseConfig.storeFile != null) {
				releaseConfig
			} else {
				logger.warn("Release keystore не задан — используем debug signingConfig")
				signingConfigs.getByName("debug")
			}
		}
		debug {
			isMinifyEnabled = false
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
		isCoreLibraryDesugaringEnabled = true
	}

	kotlin {
		jvmToolchain(17)
	}

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.14"
	}

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

ksp {
	arg("room.schemaLocation", "${projectDir}/schemas")
	arg("room.generateKotlin", "true")
}

dependencies {
	implementation(platform("androidx.compose:compose-bom:2024.09.02"))
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
	implementation("androidx.navigation:navigation-compose:2.8.1")

	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.material:material")
	implementation("androidx.compose.material:material-icons-extended")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-tooling-preview")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")

	implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
	implementation("com.google.accompanist:accompanist-permissions:0.36.0")

	implementation("androidx.datastore:datastore-preferences:1.1.1")

	// WorkManager for reminders
	implementation("androidx.work:work-runtime-ktx:2.9.1")

	implementation("androidx.room:room-runtime:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")

	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

	// 🚀 Advanced AI/ML for 2025
	implementation("com.google.ai.client.generativeai:generativeai:0.2.2")
	implementation("com.google.mlkit:smart-reply:17.0.4")
	implementation("com.google.mlkit:pose-detection-accurate:18.0.0-beta4")
	implementation("com.google.mlkit:image-labeling:17.0.8")

	// Web3 & Blockchain (simplified for 2024 compatibility)
	implementation("org.web3j:core:4.9.8")

	// Quantum-Safe Encryption (removed to avoid conflicts)
	// implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")

	// AR/VR & Spatial Computing
	implementation("com.google.ar:core:1.42.0")

	// Edge AI & On-Device ML
	implementation("org.tensorflow:tensorflow-lite:2.16.1")

	// Advanced Health Integration
	implementation("androidx.health.connect:connect-client:1.1.0-alpha11")

	// Firebase for cloud features (simplified)
	implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
	implementation("com.google.firebase:firebase-analytics")
	implementation("com.google.firebase:firebase-crashlytics")

	// Health Connect for health data integration
	implementation("androidx.health.connect:connect-client:1.1.0-alpha11")

	// Speech-to-text for voice input
	implementation("androidx.activity:activity-compose:1.9.2")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.2.1")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
