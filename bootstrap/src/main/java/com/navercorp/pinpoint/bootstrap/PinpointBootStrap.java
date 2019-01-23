package com.navercorp.pinpoint.bootstrap;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import com.navercorp.pinpoint.ProductInfo;
import com.navercorp.pinpoint.bootstrap.agentdir.AgentDirBaseClassPathResolver;
import com.navercorp.pinpoint.bootstrap.agentdir.AgentDirectory;
import com.navercorp.pinpoint.bootstrap.agentdir.BootDir;
import com.navercorp.pinpoint.bootstrap.agentdir.ClassPathResolver;
import com.navercorp.pinpoint.bootstrap.agentdir.JavaAgentPathResolver;

/**
 * @author emeroad
 * @author netspider
 */
public class PinpointBootStrap {

    private static final BootLogger logger = BootLogger.getLogger(PinpointBootStrap.class.getName());

    private static final LoadState STATE = new LoadState();

    /**
     * -javaagent启动方式
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        main(agentArgs, instrumentation);
    }

    /**
     * attach启动方式
     * @param agentArgs
     * @param instrumentation
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        main(agentArgs, instrumentation);
    }

    private static void main(String agentArgs, Instrumentation instrumentation) {
        final boolean success = STATE.start();
        if (!success) {
            logger.warn("pinpoint-bootstrap already started. skipping agent loading.");
            return;
        }

        logger.info(ProductInfo.NAME + " agentArgs:" + agentArgs);
        logger.info("getClassLoader():" + PinpointBootStrap.class.getClassLoader());
        logger.info("getContextClassLoader():" + Thread.currentThread().getContextClassLoader());

        JavaAgentPathResolver javaAgentPathResolver = JavaAgentPathResolver.newJavaAgentPathResolver();
        String agentPath = javaAgentPathResolver.resolveJavaAgentPath();
        logger.info("JavaAgentPath:" + agentPath);

        if (Object.class.getClassLoader() != PinpointBootStrap.class.getClassLoader()) {
            // TODO bug : location is null
            logger.warn("Invalid pinpoint-bootstrap.jar:" + agentArgs);
            return;
        }

        final Map<String, String> agentArgsMap = argsToMap(agentArgs);

        final ClassPathResolver classPathResolver = new AgentDirBaseClassPathResolver(agentPath);

        final AgentDirectory agentDirectory = resolveAgentDir(classPathResolver);
        if (agentDirectory == null) {
            logger.warn("Agent Directory Verify fail. skipping agent loading.");
            logPinpointAgentLoadFail();
            return;
        }
        BootDir bootDir = agentDirectory.getBootDir();
        appendToBootstrapClassLoader(instrumentation, bootDir);

        ClassLoader parentClassLoader = getParentClassLoader();
        final ModuleBootLoader moduleBootLoader = loadModuleBootLoader(instrumentation, parentClassLoader);
        PinpointStarter bootStrap = new PinpointStarter(parentClassLoader, agentArgsMap, agentDirectory, instrumentation, moduleBootLoader);
        if (!bootStrap.start()) {
            logPinpointAgentLoadFail();
        }
    }

    private static ModuleBootLoader loadModuleBootLoader(Instrumentation instrumentation, ClassLoader parentClassLoader) {
        if (!ModuleUtils.isModuleSupported()) {
            return null;
        }
        logger.info("java9 module detected");
        logger.info("ModuleBootLoader start");
        ModuleBootLoader moduleBootLoader = new ModuleBootLoader(instrumentation, parentClassLoader);
        moduleBootLoader.loadModuleSupport();
        return moduleBootLoader;
    }

    private static AgentDirectory resolveAgentDir(ClassPathResolver classPathResolver) {
        try {
            AgentDirectory agentDir = classPathResolver.resolve();
            return agentDir;
        } catch(Exception e) {
            logger.warn("AgentDir resolve fail Caused by:" + e.getMessage(), e);
            return null;
        }
    }


    private static ClassLoader getParentClassLoader() {
        final ClassLoader classLoader = getPinpointBootStrapClassLoader();
        if (classLoader == Object.class.getClassLoader()) {
            logger.info("parentClassLoader:BootStrapClassLoader:" + classLoader );
        } else {
            logger.info("parentClassLoader:" + classLoader);
        }
        return classLoader;
    }

    private static ClassLoader getPinpointBootStrapClassLoader() {
        return PinpointBootStrap.class.getClassLoader();
    }


    private static Map<String, String> argsToMap(String agentArgs) {
        ArgsParser argsParser = new ArgsParser();
        Map<String, String> agentArgsMap = argsParser.parse(agentArgs);
        if (!agentArgsMap.isEmpty()) {
            logger.info("agentParameter:" + agentArgs);
        }
        return agentArgsMap;
    }

    private static void appendToBootstrapClassLoader(Instrumentation instrumentation, BootDir bootDir) {
        List<JarFile> jarFiles = bootDir.openJarFiles();
        for (JarFile jarFile : jarFiles) {
            logger.info("appendToBootstrapClassLoader:" + jarFile.getName());
            instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
        }
    }



    private static void logPinpointAgentLoadFail() {
        final String errorLog =
                "*****************************************************************************\n" +
                        "* Pinpoint Agent load failure\n" +
                        "*****************************************************************************";
        System.err.println(errorLog);
    }


}
