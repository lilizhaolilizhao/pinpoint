/**
 * Copyright 2014 NAVER Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.plugin.php;

import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;
import com.navercorp.pinpoint.common.plugin.PluginInfoBean;

import java.util.List;


/**
 * @author chenguoxi
 *
 */
public class PhpPlugin implements ProfilerPlugin, TransformTemplateAware {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());

    private TransformTemplate transformTemplate;

    /*
     * (non-Javadoc)
     *
     * @see com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin#setUp(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext)
     */
    @Override
    public void setup(ProfilerPluginSetupContext context, List<PluginInfoBean> pluginInfoBeans) {

    }


    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}

