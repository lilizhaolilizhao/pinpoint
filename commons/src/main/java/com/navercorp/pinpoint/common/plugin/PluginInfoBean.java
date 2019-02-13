package com.navercorp.pinpoint.common.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 插件信息
 */
@Data
@AllArgsConstructor
public class PluginInfoBean {
    private String className;
    private String methodName;
    private String[] parameterTypes;
}
