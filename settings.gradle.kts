pluginManagement {
    repositories {

        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-remote-maven.google.com/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/auto-mvn-release-private/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/auto-mvn-snapshot-private/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-repo/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-temp-iFlytekAuto-snapshot/")
        }
        /** 第1组配置 星河Maven (配置不可擅自改动) 结束 **/

        /** 第2组配置 阿里云Maven (配置不可擅自改动) 开始 **/
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://maven.aliyun.com/nexus/content/groups/public")
        }
        /** 第2组配置 阿里云Maven (配置不可擅自改动) 结束 **/

        /** 第3组配置 其他定制化Maven (其他Maven可在此组配置末尾追加) 开始 **/
        /** 第3组配置 其他定制化Maven (其他Maven可在此组配置末尾追加) 结束 **/

        /** 第4组配置 外网Maven (配置不可擅自改动) 开始 **/
        google()
        mavenCentral()
        gradlePluginPortal()
        /** 第4组配置 外网Maven (配置不可擅自改动) 结束 **/

        // 本地maven
        mavenLocal()
//        gradlePluginPortal()
//        google()
//        mavenCentral()
//        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-remote-jcenter.bintray.com/")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://maven.aliyun.com/repository/central") }
//
//    }
    repositories {
        // ######## repositories 配置模板，不允许修改，如有特殊配置，追加在第3组配置中 ############
        /** 第1组配置 星河Maven (配置不可擅自改动) 开始 **/
        /** 星河maven不支持https，需要设置allowInsecureProtocol属性为true **/

        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-remote-maven.google.com/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/auto-mvn-release-private/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/auto-mvn-snapshot-private/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-repo/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-temp-iFlytekAuto-snapshot/")
        }
        /** 第1组配置 星河Maven (配置不可擅自改动) 结束 **/

        /** 第2组配置 阿里云Maven (配置不可擅自改动) 开始 **/
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://maven.aliyun.com/nexus/content/groups/public")
        }
        /** 第2组配置 阿里云Maven (配置不可擅自改动) 结束 **/

        /** 第3组配置 其他定制化Maven (其他Maven可在此组配置末尾追加) 开始 **/
        /** 第3组配置 其他定制化Maven (其他Maven可在此组配置末尾追加) 结束 **/

        /** 第4组配置 外网Maven (配置不可擅自改动) 开始 **/
        google()
        mavenCentral()
        /** 第4组配置 外网Maven (配置不可擅自改动) 结束 **/

        // 本地maven
        mavenLocal()
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://artifacts.gxatek.com/artifactory/mvn-remote-jcenter.bintray.com/")
        }
    }
}

rootProject.name = "FusionPlatform"
include(":app")
 