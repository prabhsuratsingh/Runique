plugins {
    alias(libs.plugins.runique.android.feature.ui)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}