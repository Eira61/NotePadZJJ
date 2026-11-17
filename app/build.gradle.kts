plugins {
  id("com.android.application")
}

android {
  namespace = "com.example.notepadzjj"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.example.notepadzjj"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  // 老师给的资源包 activity_main.xml 用的是 RecyclerView，所以必须引入这一个依赖
  implementation("androidx.recyclerview:recyclerview:1.3.2")
}
