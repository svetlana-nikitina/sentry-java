import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    jacoco
}

android {
    compileSdkVersion(Config.Android.compileSdkVersion)
    buildToolsVersion(Config.Android.buildToolsVersion)

    defaultConfig {
        targetSdkVersion(Config.Android.targetSdkVersion)
        minSdkVersion(Config.Android.minSdkVersion)

        javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        versionName = "$version"
    }

    buildTypes {
        getByName("debug")
        getByName("release") {
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // due https://github.com/gradle/gradle/issues/11083
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_1_8.toString()
//    }
    withGroovyBuilder {
        "kotlinOptions" {
            setProperty("jvmTarget", JavaVersion.VERSION_1_8.toString())
        }
    }

    testOptions {
        animationsDisabled = true
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
            all(KotlinClosure1<Any, Test>({
                (this as Test).also { testTask ->
                    testTask.extensions
                        .getByType(JacocoTaskExtension::class.java)
                        .isIncludeNoLocationClasses = true
                }
            }, this))
        }
    }

    lintOptions {
        isWarningsAsErrors = true
        isCheckDependencies = true

        // We run a full lint analysis as build part in CI, so skip vital checks for assemble tasks.
        isCheckReleaseBuilds = false
    }
}

dependencies {
    api(project(":sentry-core"))

    // libs
    implementation(Config.Libs.gson)

    compileOnly(Config.CompileOnly.annotations)

    // tests
    testImplementation(kotlin(Config.kotlinStdLib, KotlinCompilerVersion.VERSION))
    testImplementation(Config.TestLibs.robolectric)
    testImplementation(Config.TestLibs.kotlinTestJunit)
    testImplementation(Config.TestLibs.androidxCore)
    testImplementation(Config.TestLibs.androidxRunner)
    testImplementation(Config.TestLibs.androidxJunit)
    testImplementation(Config.TestLibs.mockitoKotlin)
}