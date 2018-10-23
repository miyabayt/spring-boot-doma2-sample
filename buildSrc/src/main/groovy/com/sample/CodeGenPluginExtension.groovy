package com.sample

class CodeGenPluginExtension {
    String srcDirName = "src/main/java/"
    String testDirName = "src/test/groovy/"
    String sqlDirName = "src/main/resources/META-INF/"
    String htmlDirName = "src/main/resources/templates/modules/"
    String domainProjectName
    String webProjectName

    String daoPackageName
    String dtoPackageName
    String commonDtoPackageName
    String repositoryPackageName
    String servicePackageName
    String commonServicePackageName
    String exceptionPackageName
    String webBasePackageName
    String baseControllerPackageName
    String controllerPackageName
    String baseValidatorPackageName
}
