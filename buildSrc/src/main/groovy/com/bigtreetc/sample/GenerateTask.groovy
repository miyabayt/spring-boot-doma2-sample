package com.bigtreetc.sample

import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

import java.nio.file.Path
import java.nio.file.Paths

class GenerateTask extends DefaultTask {

    @TaskAction
    def codegen() {
        if (!project.hasProperty("physicalFunctionName")) {
            println("physicalFunctionName must not be null")
            return
        }

        if (!project.hasProperty("physicalMultiFunctionName")) {
            println("physicalMultiFunctionName must not be null")
            return
        }

        if (!project.hasProperty("logicalFunctionName")) {
            println("logicalFunctionName must not be null")
            return
        }

        def target = null
        if (project.hasProperty("target")) {
            target = project.target
        }

        def ext = getProject().getExtensions().findByType(CodeGenPluginExtension)
        def camelPhysicalFunctionName = StringUtils.capitalize(project.physicalFunctionName.toString())
        def lowerPhysicalFunctionName = StringUtils.lowerCase(camelPhysicalFunctionName)
        def lowerPhysicalMultiFunctionName = StringUtils.lowerCase(project.physicalMultiFunctionName.toString())
        def logicalFunctionName = project.logicalFunctionName.toString()

        def objects = extensionsToMap()
        objects.put("camelPhysicalFunctionName", camelPhysicalFunctionName)
        objects.put("upperPhysicalFunctionName", camelPhysicalFunctionName.toUpperCase())
        objects.put("lowerPhysicalFunctionName", lowerPhysicalFunctionName)
        objects.put("lowerPhysicalMultiFunctionName", lowerPhysicalMultiFunctionName)
        objects.put("logicalFunctionName", logicalFunctionName)

        def domainTarget = "${ext.domainProjectName}/${ext.srcDirName}"
        def testDomainTarget = "${ext.domainProjectName}/${ext.testDirName}"
        if (target == null || target.equals("entity")) {
            generate("templates/src/Entity.txt", getTargetPath(domainTarget, "/${ext.dtoPackageName}/${camelPhysicalFunctionName}", ".java"), objects)
            generate("templates/src/Criteria.txt", getTargetPath(domainTarget, "/${ext.dtoPackageName}/${camelPhysicalFunctionName}Criteria", ".java"), objects)
        }
        if (target == null || target.equals("dao")) {
            generate("templates/src/Dao.txt", getTargetPath(domainTarget, "/${ext.daoPackageName}/${camelPhysicalFunctionName}Dao", ".java"), objects)
            generate("templates/src/TestDao.txt", getTargetPath(testDomainTarget, "/${ext.daoPackageName}/${camelPhysicalFunctionName}DaoTest", ".groovy"), objects)
        }
        if (target == null || target.equals("repository")) {
            generate("templates/src/Repository.txt", getTargetPath(domainTarget, "/${ext.repositoryPackageName}/${camelPhysicalFunctionName}Repository", ".java"), objects)
        }
        if (target == null || target.equals("service")) {
            generate("templates/src/Service.txt", getTargetPath(domainTarget, "/${ext.servicePackageName}/${lowerPhysicalFunctionName}/${camelPhysicalFunctionName}Service", ".java"), objects)
        }

        if (target == null || target.equals("dao")) {
            def sqlTarget = "${ext.domainProjectName}/${ext.sqlDirName}"
            generate("templates/sql/select.txt", getTargetPath(sqlTarget, "/${ext.daoPackageName}/${camelPhysicalFunctionName}Dao/select", ".sql"), objects)
            generate("templates/sql/selectAll.txt", getTargetPath(sqlTarget, "/${ext.daoPackageName}/${camelPhysicalFunctionName}Dao/selectAll", ".sql"), objects)
            generate("templates/sql/selectById.txt", getTargetPath(sqlTarget, "/${ext.daoPackageName}/${camelPhysicalFunctionName}Dao/selectById", ".sql"), objects)
        }

        if (target == null || target.equals("controller")) {
            def controllerTarget = "${ext.webProjectName}/${ext.srcDirName}/${ext.controllerPackageName}"
            generate("templates/src/controller/Csv.txt", getTargetPath(controllerTarget, "/${lowerPhysicalFunctionName}/${camelPhysicalFunctionName}Csv", ".java"), objects)
            generate("templates/src/controller/Form.txt", getTargetPath(controllerTarget, "/${lowerPhysicalFunctionName}/${camelPhysicalFunctionName}Form", ".java"), objects)
            generate("templates/src/controller/SearchForm.txt", getTargetPath(controllerTarget, "/${lowerPhysicalFunctionName}/Search${camelPhysicalFunctionName}Form", ".java"), objects)
            generate("templates/src/controller/Validator.txt", getTargetPath(controllerTarget, "/${lowerPhysicalFunctionName}/${camelPhysicalFunctionName}FormValidator", ".java"), objects)
            generate("templates/src/controller/Controller.txt", getTargetPath(controllerTarget, "/${lowerPhysicalFunctionName}/${camelPhysicalFunctionName}Controller", ".java"), objects)
        }

        if (target == null || target.equals("html")) {
            def htmlTarget = "${ext.webProjectName}/${ext.htmlDirName}"
            generate("templates/html/find.txt", getTargetPath(htmlTarget, "/${lowerPhysicalFunctionName}/find", ".html"), objects)
            generate("templates/html/new.txt", getTargetPath(htmlTarget, "/${lowerPhysicalFunctionName}/new", ".html"), objects)
            generate("templates/html/show.txt", getTargetPath(htmlTarget, "/${lowerPhysicalFunctionName}/show", ".html"), objects)
        }
    }

    Path getTargetPath(String _target, String _fileName, String suffix) {
        def target = StringUtils.replace(_target, ".", "/")
        def sb = new StringBuilder().append(target)
        def fileName = StringUtils.replace(_fileName, ".", "/")
        return Paths.get(sb.toString(), "${fileName}${suffix}")
    }

    void generate(String template, Path path, Map<String, Object> objects) {
        def body = processTemplate(template, objects)
        def f = path.toFile()
        f.parentFile.mkdirs()
        f.createNewFile()
        f.write(body, "UTF-8")
    }

    String processTemplate(String template, Map<String, Object> objects) {
        def resolver = new ClassLoaderTemplateResolver()
        resolver.setTemplateMode("TEXT")
        resolver.setCharacterEncoding("UTF-8")

        def templateEngine = new TemplateEngine()
        templateEngine.setTemplateResolver(resolver)

        def context = new Context()
        if (objects != null && !objects.isEmpty()) {
            objects.each { key, value ->
                context.setVariable(key, value)
            }
        }

        return templateEngine.process(template, context)
    }

    Map<String, Object> extensionsToMap() {
        def ext = getProject().getExtensions().findByType(CodeGenPluginExtension)

        Map<String, Object> objects = new HashMap<>()
        objects.put("commonDtoPackageName", ext.commonDtoPackageName)
        objects.put("dtoPackageName", ext.dtoPackageName)
        objects.put("daoPackageName", ext.daoPackageName)
        objects.put("repositoryPackageName", ext.repositoryPackageName)
        objects.put("servicePackageName", ext.servicePackageName)
        objects.put("commonServicePackageName", ext.commonServicePackageName)
        objects.put("exceptionPackageName", ext.exceptionPackageName)
        objects.put("webBasePackageName", ext.webBasePackageName)
        objects.put("baseControllerPackageName", ext.baseControllerPackageName)
        objects.put("controllerPackageName", ext.controllerPackageName)
        objects.put("baseValidatorPackageName", ext.baseValidatorPackageName)

        return objects
    }
}
